package com.exclamationlabs.connid.base.scim2.attribute;

//urn:ietf:params:scim:schemas:core:2.0:User

//create another class  Scim2EnterpiseUserAttribute and Scim2GuestUser
public enum Scim2UserAttribute {

  username,
  nickname,
  name_familyName("name.familyName"),
  name_givenName("name.familyName"),


  NAME,
  Scim2UserName,
  DISPLAY_NAME,
  NICK_NAME,
  PROFILE_URL,
  TITLE,
  USER_TYPE,
  PREFERRED_LANGUAGE,
  LOCALE,
  TIMEZONE,
  ACTIVE,
  PASSWORD,
  EMAILS,
  SCIM2_CUSTOM_TYPE, // covers for
  // emails/phonenumbers/ims/photos/entitlements/roles/x509Certificates
  SCIM2_ADDRESS,
  SCIM2_CUSTOM_GROUP;

  Scim2UserAttribute(String s) {
  }
  Scim2UserAttribute() {
  }
}
