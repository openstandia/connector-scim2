package com.exclamationlabs.connid.base.scim2.model.request;

import com.exclamationlabs.connid.base.scim2.model.Scim2User;
import com.google.gson.annotations.SerializedName;

public class UserCreationRequest {
  private final String action;

  @SerializedName("user_info")
  private final Scim2User user;

  public UserCreationRequest(String actionInput, Scim2User userInput) {
    action = actionInput;
    user = userInput;
  }

  public String getAction() {
    return action;
  }

  public Scim2User getUser() {
    return user;
  }
}
