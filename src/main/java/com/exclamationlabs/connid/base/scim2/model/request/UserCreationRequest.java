package com.exclamationlabs.connid.base.scim2.model.request;

import com.exclamationlabs.connid.base.scim2.model.Scim2User;

public class UserCreationRequest {
  // private final String action;

  // @SerializedName("user_info")
  private final Scim2User user;

  public UserCreationRequest(Scim2User userInput) {
    // action = actionInput;
    user = userInput;
  }

  public Scim2User getUser() {
    return user;
  }
}
