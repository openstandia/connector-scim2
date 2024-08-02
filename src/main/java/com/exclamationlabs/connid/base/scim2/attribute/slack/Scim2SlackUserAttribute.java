package com.exclamationlabs.connid.base.scim2.attribute.slack;

import com.exclamationlabs.connid.base.scim2.attribute.Scim2UserAttribute;

public enum Scim2SlackUserAttribute {
  userName("userName"),
  nickName("nickName"),
  // Name Fields
  name,
  //name_familyName("name.familyName"),
  //name_givenName("name.givenName"),

  familyName,
  givenName,
  middleName,
  honorificPrefix,
  honorificSuffix,

  // Address Fields

  streetAddress,
  locality,
  region,
  postalCode,
  country,

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
  emails,
  SCIM2_CUSTOM_TYPE, // covers for
  // emails/phonenumbers/ims/photos/entitlements/roles/x509Certificates
  SCIM2_ADDRESS,
  SCIM2_CUSTOM_GROUP;

  Scim2SlackUserAttribute(String s) {}

  Scim2SlackUserAttribute() {}
}
