package com.exclamationlabs.connid.base.scim2.driver.rest.slack;

import com.exclamationlabs.connid.base.connector.driver.DriverInvocator;
import com.exclamationlabs.connid.base.connector.driver.rest.RestRequest;
import com.exclamationlabs.connid.base.connector.driver.rest.RestResponseData;
import com.exclamationlabs.connid.base.scim2.driver.rest.Scim2Driver;
import com.exclamationlabs.connid.base.scim2.model.Scim2User;
import com.exclamationlabs.connid.base.scim2.model.slack.Scim2SlackUser;
import java.util.Map;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.exceptions.ConnectorException;

public class Scim2SlackUsersInvocator implements DriverInvocator<Scim2Driver, Scim2SlackUser> {

  private static final Log LOG = Log.getLog(Scim2SlackUsersInvocator.class);

  @Override
  public String create(Scim2Driver driver, Scim2SlackUser model) throws ConnectorException {
    return null;
  }

  @Override
  public void update(Scim2Driver driver, String userId, Scim2SlackUser userModel)
      throws ConnectorException {}

  @Override
  public void delete(Scim2Driver driver, String userId) throws ConnectorException {}

  @Override
  public Scim2SlackUser getOne(
      Scim2Driver driver, String objectId, Map<String, Object> prefetchDataMap)
      throws ConnectorException {
    Scim2SlackUser user = null;
    driver.getConfiguration().setCurrentToken(driver.getConfiguration().getToken());
    RestRequest req =
            new RestRequest.Builder<>(Scim2SlackUser.class)
                    .withGet()
                    .withRequestUri("/Users/" + objectId)
                    .build();
    RestResponseData<Scim2SlackUser> response = driver.executeRequest(req);
    if (response.getResponseStatusCode() == 200) {
      System.out.println(response.getResponseObject());
      user = response.getResponseObject();
    //  getPhoneInfo(driver, user);
    }

    return user;
  }
}
