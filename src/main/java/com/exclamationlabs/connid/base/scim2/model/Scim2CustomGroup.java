package com.exclamationlabs.connid.base.scim2.model;

import lombok.Data;

@Data
public class Scim2CustomGroup {
    private String name; //complex
    private String value;
    private String $ref;
    private String display;
    private String type; //should enum
}
