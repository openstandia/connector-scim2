package com.exclamationlabs.connid.base.scim2.driver.rest;

import static com.exclamationlabs.connid.base.scim2.model.response.fault.ErrorResponseCode.*;

import com.exclamationlabs.connid.base.connector.driver.exception.DriverRenewableTokenExpiredException;
import com.exclamationlabs.connid.base.connector.driver.rest.RestFaultProcessor;
import com.exclamationlabs.connid.base.connector.logging.Logger;
import com.exclamationlabs.connid.base.scim2.model.response.fault.Scim2ErrorResponse;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import org.apache.commons.codec.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.identityconnectors.framework.common.exceptions.AlreadyExistsException;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.exceptions.InvalidAttributeValueException;

public class Scim2FaultProcessor implements RestFaultProcessor {

  private static final Scim2FaultProcessor instance = new Scim2FaultProcessor();

  public static Scim2FaultProcessor getInstance() {
    return instance;
  }

  @Override
  public void process(HttpResponse httpResponse, GsonBuilder gsonBuilder) {
    String rawResponse;
    try {
      rawResponse = EntityUtils.toString(httpResponse.getEntity(), Charsets.UTF_8);
      Logger.info(this, String.format("Raw Fault response %s", rawResponse));

      Header responseType = httpResponse.getFirstHeader("Content-Type");
      String responseTypeValue = responseType.getValue();
      if (!StringUtils.contains(responseTypeValue, ContentType.APPLICATION_JSON.getMimeType())) {
        // received non-JSON error response from Zoom unable to process
        String errorMessage = "Unable to parse response, not valid JSON: ";
        Logger.info(this, String.format("%s %s", errorMessage, rawResponse));
        throw new ConnectorException(errorMessage + rawResponse);
      }

      handleFaultResponse(rawResponse, gsonBuilder);

    } catch (IOException e) {
      throw new ConnectorException(
          "Unable to read fault response. "
              + "Status: "
              + httpResponse.getStatusLine().getStatusCode()
              + ", "
              + httpResponse.getStatusLine().getReasonPhrase(),
          e);
    }
  }

  private void handleFaultResponse(String rawResponse, GsonBuilder gsonBuilder) {
    Scim2ErrorResponse faultData = gsonBuilder.create().fromJson(rawResponse, Scim2ErrorResponse.class);
    if (faultData != null) {
      if (faultData.getStatus() != null) {
        if (checkRecognizedFaultCodes(faultData)) {
          // Report the  fault condition
          throw new ConnectorException(
              "SCIM2 Error.  Code: "
                  + faultData.getStatus()
                  + "; Message: "
                  + faultData.getDetail());
        } else {
          Logger.info(this, faultData.getDetail());
          return;
        }
      }
    }
    throw new ConnectorException("Error received from Scim Backend." + rawResponse);
  }

  /**
   * Check the fault status code
   * @param error Scim2 Error
   * @return false when the error can be ignored
   */
  private Boolean checkRecognizedFaultCodes(Scim2ErrorResponse error) {
    switch (error.getStatus()) {
      case HTTP_CONFLICT:
        throw new AlreadyExistsException("User or Group already exists.");
      case HTTP_BAD_REQUEST:
        throw new ConnectorException("SCIM2 Bad Request. "
                + "status: "
                + error.getStatus()
                + "; scimType: "
                + error.getScimType()
                + "; detail: "
                + error.getDetail());
      case HTTP_NOT_FOUND:
        // ignore fault and return to invocator
        return false;
    }
    return true;
  }
}
