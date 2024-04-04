package com.exclamationlabs.connid.base.scim2.model;

public enum UserCreationType {
  CREATE("create"),
  AUTO_CREATE("autoCreate"),
  CUSTOM_CREATE("custCreate"),
  SSO_CREATE("ssoCreate");

  private String scim2Name;

  UserCreationType(String name) {
    scim2Name = name;
  }

  public String getScim2Name() {
    return scim2Name;
  }
}
