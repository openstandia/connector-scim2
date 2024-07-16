package com.exclamationlabs.connid.base.scim2.model.response;

import com.exclamationlabs.connid.base.scim2.model.Meta;
import com.exclamationlabs.connid.base.scim2.model.Scim2User;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Set;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ListUsersResponse {
  @SerializedName("next_page_token")
  private String nextPageToken;

  @SerializedName("startIndex")
  private Integer startIndex;

  @SerializedName("page_number")
  private Integer pageNumber;

  @SerializedName("itemsPerPage")
  private Integer itemsPerPage;

  @SerializedName("totalResults")
  private Integer totalResults;

  @JsonProperty("Resources")
  private Set<Scim2User> Resources;

  //public Set<Scim2Resource> Resources;

  /*public static class Scim2Resource {

    List<String> schemas;
    String id;
    String externalId;

    private Meta meta;
    public static class Meta {
      String created;
      String location;
    }
    String userName;
    String nickName;

    Name name;
    public static class Name {
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

    String displayName;
    String profileUrl;
    String title;
    String timezone;
    boolean active;

    List<Scim2Emails> emails;
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

    List<Scim2Photos> photos;
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
  }*/


}
