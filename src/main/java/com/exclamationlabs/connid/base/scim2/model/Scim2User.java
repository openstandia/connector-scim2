package com.exclamationlabs.connid.base.scim2.model;

import com.exclamationlabs.connid.base.connector.model.IdentityModel;
import com.google.gson.annotations.SerializedName;
import java.util.Set;

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
  private String id;
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
    return id;
  }

  @Override
  public String getIdentityNameValue() {
    return email;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public Set<String> getGroupIds() {
    return groupIds;
  }

  public void setGroupIds(Set<String> groupIds) {
    this.groupIds = groupIds;
  }

  public Set<String> getGroupsToAdd() {
    return groupsToAdd;
  }

  public void setGroupsToAdd(Set<String> groupsToAdd) {
    this.groupsToAdd = groupsToAdd;
  }

  public Set<String> getGroupsToRemove() {
    return groupsToRemove;
  }

  public void setGroupsToRemove(Set<String> groupsToRemove) {
    this.groupsToRemove = groupsToRemove;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getLastLoginTime() {
    return lastLoginTime;
  }

  public void setLastLoginTime(String lastLoginTime) {
    this.lastLoginTime = lastLoginTime;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Long getPersonalMeetingId() {
    return personalMeetingId;
  }

  public void setPersonalMeetingId(Long personalMeetingId) {
    this.personalMeetingId = personalMeetingId;
  }

  public String getPhoneCountry() {
    return phoneCountry;
  }

  public void setPhoneCountry(String phoneCountry) {
    this.phoneCountry = phoneCountry;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getTimezone() {
    return timezone;
  }

  public void setTimezone(String timezone) {
    this.timezone = timezone;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public String getVerified() {
    return verified;
  }

  public void setVerified(String verified) {
    this.verified = verified;
  }
}
