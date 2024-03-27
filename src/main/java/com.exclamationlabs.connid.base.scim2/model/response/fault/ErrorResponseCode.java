package com.exclamationlabs.connid.base.scim2.model.response.fault;

public class ErrorResponseCode {
  int PAID_SUBSCRIPTION_REQUIRED = 200;
  int VALIDATION_FAILED = 300;

  int USER_NOT_FOUND = 1001;
  int USER_ALREADY_EXISTS = 1005;

  int GROUP_NOT_FOUND = 4130;
  int GROUP_NAME_ALREADY_EXISTS = 4132;
  int TOKEN_EXPIRED = 124;
}
