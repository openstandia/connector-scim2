package com.exclamationlabs.connid.base.scim2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Scim2Name {
  public String name;
  public String formatted;

  @JsonProperty("familyName")
  public String familyname;

  @JsonProperty("givenName")
  public String givenname;

  @JsonProperty("middleName")
  public String middlename;

  @JsonProperty("honorificPrefix")
  public String honorificprefix;

  @JsonProperty("honorificSuffix")
  public String honorificsuffix;
}
