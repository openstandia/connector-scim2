package com.exclamationlabs.connid.base.scim2.model.response.fault;

import java.util.List;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
  private Integer code;
  private String message;
  private List<ErrorData> errors;

  public void setCode(Integer code) {
    this.code = code;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public void setErrors(List<ErrorData> errors) {
    this.errors = errors;
  }

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

  public Integer getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public List<ErrorData> getErrors() {
    return errors;
  }
}
