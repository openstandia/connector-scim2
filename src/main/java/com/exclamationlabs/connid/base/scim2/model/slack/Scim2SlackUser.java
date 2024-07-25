package com.exclamationlabs.connid.base.scim2.model.slack;

import com.exclamationlabs.connid.base.connector.model.IdentityModel;
import com.exclamationlabs.connid.base.scim2.model.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

@Data
public class Scim2SlackUser extends Scim2User {

  @SerializedName("urn:ietf:params:scim:schemas:extension:slack:guest:2.0:User")
  private SlackGuestUser guestInfo;

  //@SerializedName("urn:ietf:params:scim:schemas:extension:slack:directory:2.0:User")
  //private SlackDirectoryUser directoryInfo;

  @SerializedName("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User")
  private Scim2EnterpriseUser enterpriseInfo;

  @SerializedName("urn:ietf:params:scim:schemas:extension:slack:profile:2.0:User")
  private SlackProfileUser profileInfo;

 /*
  private String id;
  private String userName;
  private String nickName;
  private Scim2Name name;
  private Scim2Addresses scim2Addresses;

  private String displayName;

  private String profileUrl;
  private String title;
  private String userType;
  private String preferredLanguage;
  private String locale;
  private String timezone;
  private boolean active;
  private String password;
  // private List<Scim2CustomType> scim2CustomType; //covers for
  // emails/phonenumbers/ims/photos/entitlements/roles/x509Certificates

  private List<Scim2Emails> emails;
  private Scim2PhoneNumbers scim2PhoneNumbers;
  private Scim2Ims scim2Ims;
  private List<Scim2Photos> photos;
  private Scim2Entitlements scim2Entitlements;
  private Scim2Roles scim2Roles;
  private Scim2X509Certificates scim2X509Certificates;

  private String email;

  @Override
  public String getIdentityIdValue() {
    return id;
  }

  @Override
  public String getIdentityNameValue() {
    return email;
  }

  public static class Scim2Name {
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

  public static class Scim2Addresses {

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

  public static class Scim2Emails {
    private String name;
    private String value;
    private String display;
    private String type; // should enum
    private boolean primary;
  }

  public static class Scim2PhoneNumbers {
    private String name;
    private String value;
    private String display;
    private String type; // should enum
    private boolean primary;
  }

  public static class Scim2Ims {
    private String name;
    private String value;
    private String display;
    private String type; // should enum
    private boolean primary;
  }

  public static class Scim2Photos {

    private String name;
    private String value;
    private String display;
    private String type; // should enum
    private boolean primary;
  }

  public static class Scim2Entitlements {

    private String name;
    private String value;
    private String display;
    private String type; // should enum
    private boolean primary;
  }
  ;

  public static class scim2Roles {

    private String name;
    private String value;
    private String display;
    private String type; // should enum
    private boolean primary;
  }
  ;

  public static class scim2X509Certificates {
    private String name;
    private String value;
    private String display;
    private String type; // should enum
    private boolean primary;
  }
  ;
  */

}
