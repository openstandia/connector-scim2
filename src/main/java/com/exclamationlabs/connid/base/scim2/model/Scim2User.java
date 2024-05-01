package com.exclamationlabs.connid.base.scim2.model;

import com.exclamationlabs.connid.base.connector.model.IdentityModel;
import lombok.Data;

@Data
public class Scim2User implements IdentityModel {

  private String userName;
  //private String name;

  private Scim2Name scim2Name;
  private Scim2Addresses scim2Addresses;

  private String displayName;
  private String nickName;
  private String profileUrl;
  private String title;
  private String userType;
  private String preferredLanguage;
  private String locale;
  private String timezone;
  private boolean active;
  private String password;
  //private List<Scim2CustomType> scim2CustomType; //covers for emails/phonenumbers/ims/photos/entitlements/roles/x509Certificates

  private Scim2Emails scim2Emails;
  private Scim2PhoneNumbers scim2PhoneNumbers;
  private Scim2Ims scim2Ims;
  private Scim2Photos scim2Photos;
  private Scim2Entitlements scim2Entitlements;
  private Scim2Roles scim2Roles;
  private Scim2X509Certificates scim2X509Certificates;

  private String id;
  private String email;
  @Override
  public String getIdentityIdValue() {
    return id;
  }

  @Override
  public String getIdentityNameValue() {
    return email;
  }


}


