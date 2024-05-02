package com.exclamationlabs.connid.base.scim2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Scim2Addresses {
  private String name;
  private String formatted;

  @JsonProperty("streetAddress")
  private String streetaddress;

  private String locality;
  private String region;

  @JsonProperty("postalCode")
  private String postalcode;

  private String country;
  private String type; // should enum
}
