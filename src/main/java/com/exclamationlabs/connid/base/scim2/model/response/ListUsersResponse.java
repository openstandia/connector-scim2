package com.exclamationlabs.connid.base.scim2.model.response;

import com.exclamationlabs.connid.base.scim2.model.Scim2User;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import java.util.Set;
import lombok.Data;

@Data
public class ListUsersResponse<T extends Scim2User> {
  @SerializedName("startIndex")
  private Integer startIndex;

  @SerializedName("itemsPerPage")
  private Integer itemsPerPage;

  @SerializedName("totalResults")
  private Integer totalResults;

  @SerializedName("Resources")
  private Set<T> resources;

  private Set<String> schemas;
  public Set<T> getResources() {
    return resources;
  }

  public void setResources(Set<T> resources) {
    this.resources = resources;
  }
}
