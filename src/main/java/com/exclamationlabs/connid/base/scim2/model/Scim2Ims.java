package com.exclamationlabs.connid.base.scim2.model;

import lombok.Data;

@Data
public class Scim2Ims {
  private String name;
  private String value;
  private String display;
  private String type; // should enum
  private boolean primary;
}
