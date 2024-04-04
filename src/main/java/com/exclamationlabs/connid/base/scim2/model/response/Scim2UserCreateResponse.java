package com.exclamationlabs.connid.base.scim2.model.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Scim2UserCreateResponse {
  private Integer code;
  private String email;

  @SerializedName("first_name")
  private String firstName;

  private String id;

  @SerializedName("last_name")
  private String lastName;

  private String message;
  private Integer type;
}
