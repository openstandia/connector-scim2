package com.exclamationlabs.connid.base.scim2.model.response.fault;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ErrorData {
  private String field;
  private String message;
}
