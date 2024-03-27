package com.exclamationlabs.connid.base.scim2.model;

import com.exclamationlabs.connid.base.connector.model.IdentityModel;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@AllArgsConstructor
public class Scim2Group implements IdentityModel {

  @Getter private String id;
  @Getter private String name;

  @SerializedName("total_members")
  private Integer totalMembers;

  @Override
  public String getIdentityIdValue() {
    // return getId();
    return "";
  }

  @Override
  public String getIdentityNameValue() {
    //   return getName();
    return "";
  }
}
