package com.exclamationlabs.connid.base.scim2.model.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserStatusChangeRequest {
  private String action;
}
