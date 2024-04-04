package com.exclamationlabs.connid.base.scim2.model.response.fault;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
  private Integer code;
  private String message;
  private List<ErrorData> errors;

  public String getErrorDetails() {

    if (getErrors() == null || getErrors().isEmpty()) {
      return null;
    } else {
      StringBuilder response = new StringBuilder();
      for (ErrorData data : getErrors()) {
        response.append("; FIELD:");
        //  response.append(data.getField());
        response.append(", MESSAGE:");
        //  response.append(data.getMessage());
      }
      return response.toString();
    }
  }
}
