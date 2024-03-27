package com.exclamationlabs.connid.base.scim2.model;

import com.exclamationlabs.connid.base.connector.model.IdentityModel;
import com.google.gson.annotations.SerializedName;
import java.util.Set;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class Scim2User implements IdentityModel {

  @SerializedName("created_at")
  private String createdAt;

  private String email;
  // private ZoomFeature feature;

  @SerializedName("first_name")
  private String firstName;

  @SerializedName("group_ids")
  private Set<String> groupIds;

  private transient Set<String> groupsToAdd;
  private transient Set<String> groupsToRemove;
  @Getter private String id;
  // private transient ZoomPhoneUserProfile outboundAdd;
  // private transient ZoomPhoneUserProfile outboundRemove;
  private String language;

  @SerializedName("last_login_time")
  private String lastLoginTime;

  @SerializedName("last_name")
  private String lastName;

  private String password;

  @SerializedName("pmi")
  private Long personalMeetingId;

  @SerializedName("phone_country")
  private String phoneCountry;

  @SerializedName("phone_number")
  private String phoneNumber;

  // private transient ZoomPhoneUserProfile phoneProfile;
  // private transient ZoomPhoneSite site;
  private String status;
  private String timezone;
  private Integer type;
  private String verified;

  @Override
  public String getIdentityIdValue() {
    // return getId();
    return "";
  }

  @Override
  public String getIdentityNameValue() {
    // return getEmail();
    return "";
  }
}
