package com.exclamationlabs.connid.base.scim2.attribute.slack;

public enum Scim2SlackUserAttribute {
  userName,
  nickName,
  // Name Fields
  name,
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
  SCIM2_CUSTOM_GROUP
}
