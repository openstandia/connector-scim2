package com.exclamationlabs.connid.base.scim2.model;

import com.exclamationlabs.connid.base.connector.model.IdentityModel;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Scim2Group implements IdentityModel {

  private String id;

  private String name;

  @SerializedName("total_members")
  private Integer totalMembers;

  @Override
  public String getIdentityIdValue() {
    return getId();
  }

  @Override
  public String getIdentityNameValue() {
    return getName();
  }
}
