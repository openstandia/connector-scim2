package com.exclamationlabs.connid.base.scim2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Scim2Name {
    private String name;
    private String formatted;
    @JsonProperty("familyName")
    private String familyname;
    @JsonProperty("givenName")
    private String givenname;
    @JsonProperty("middleName")
    private String middlename;
    @JsonProperty("honorificPrefix")
    private String honorificprefix;
    @JsonProperty("honorificSuffix")
    private String honorificsuffix;
}
