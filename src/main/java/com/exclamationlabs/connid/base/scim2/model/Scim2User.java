package com.exclamationlabs.connid.base.scim2.model;

import com.exclamationlabs.connid.base.connector.model.IdentityModel;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Scim2User implements IdentityModel {
  private Boolean active;
  @SerializedName("addresses")
  private List<Scim2Address> addresses;
  private String displayName;
  private transient String email;
  private List<Scim2ComplexType> emails;
  @SerializedName("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User")
  private Scim2EnterpriseUser enterpriseInfo;
  private List<Scim2ComplexType> entitlements;
  private String externalId;
  private List<Scim2GroupType> groups;
  private String id;
  private List<Scim2ComplexType> ims;
  private String locale;
  private Scim2Meta meta;
  private Scim2Name name;
  private String nickName;
  private String password;
  private List<Scim2ComplexType> phoneNumbers;
  private List<Scim2ComplexType> photos;
  private String preferredLanguage;
  private String profileUrl;
  private List<Scim2ComplexType> roles;
  private List<String> schemas;
  private String timezone;
  private String title;
  private String userName;
  private String userType;
  private List<Scim2ComplexType> x509Certificates;

  public Boolean getActive()
  {
    return active;
  }

  public List<Scim2Address> getAddresses()
  {
    return addresses;
  }

  public String getDisplayName()
  {
    return displayName;
  }

  public String getEmail()
  {
    return email;
  }

  public List<Scim2ComplexType> getEmails()
  {
    return emails;
  }

  public Scim2EnterpriseUser getEnterpriseInfo()
  {
    return enterpriseInfo;
  }

  public List<Scim2ComplexType> getEntitlements()
  {
    return entitlements;
  }

  public String getExternalId()
  {
    return externalId;
  }

  public List<Scim2GroupType> getGroups()
  {
    return groups;
  }

  public String getId()
  {
    return id;
  }

  @Override
  public String getIdentityIdValue() {
    return id;
  }

  @Override
  public String getIdentityNameValue() {
    return userName;
  }

  public List<Scim2ComplexType> getIms()
  {
    return ims;
  }

  public String getLocale()
  {
    return locale;
  }

  public Scim2Meta getMeta()
  {
    return meta;
  }

  public Scim2Name getName()
  {
    return name;
  }

  public String getNickName()
  {
    return nickName;
  }

  public String getPassword()
  {
    return password;
  }

  public List<Scim2ComplexType> getPhoneNumbers()
  {
    return phoneNumbers;
  }

  public List<Scim2ComplexType> getPhotos()
  {
    return photos;
  }

  public String getPreferredLanguage()
  {
    return preferredLanguage;
  }

  public String getProfileUrl()
  {
    return profileUrl;
  }

  public List<Scim2ComplexType> getRoles()
  {
    return roles;
  }

  public List<String> getSchemas()
  {
    return schemas;
  }

  public String getTimezone()
  {
    return timezone;
  }

  public String getTitle()
  {
    return title;
  }

  public String getUserName()
  {
    return userName;
  }

  public String getUserType()
  {
    return userType;
  }

  public List<Scim2ComplexType> getX509Certificates()
  {
    return x509Certificates;
  }

  public void setActive(Boolean active)
  {
    this.active = active;
  }

  public void setAddresses(List<Scim2Address> addresses)
  {
    this.addresses = addresses;
  }

  public void setDisplayName(String displayName)
  {
    this.displayName = displayName;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public void setEmails(List<Scim2ComplexType> emails)
  {
    this.emails = emails;
  }

  public void setEnterpriseInfo(Scim2EnterpriseUser enterpriseInfo)
  {
    this.enterpriseInfo = enterpriseInfo;
  }

  public void setEntitlements(List<Scim2ComplexType> entitlements)
  {
    this.entitlements = entitlements;
  }

  public void setExternalId(String externalId)
  {
    this.externalId = externalId;
  }

  public void setGroups(List<Scim2GroupType> groups)
  {
    this.groups = groups;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public void setIms(List<Scim2ComplexType> ims)
  {
    this.ims = ims;
  }

  public void setLocale(String locale)
  {
    this.locale = locale;
  }

  public void setMeta(Scim2Meta meta)
  {
    this.meta = meta;
  }

  public void setName(Scim2Name name)
  {
    this.name = name;
  }

  public void setNickName(String nickName)
  {
    this.nickName = nickName;
  }

  public void setPassword(String password)
  {
    this.password = password;
  }

  public void setPhoneNumbers(List<Scim2ComplexType> phoneNumbers)
  {
    this.phoneNumbers = phoneNumbers;
  }

  public void setPhotos(List<Scim2ComplexType> photos)
  {
    this.photos = photos;
  }

  public void setPreferredLanguage(String preferredLanguage)
  {
    this.preferredLanguage = preferredLanguage;
  }

  public void setProfileUrl(String profileUrl)
  {
    this.profileUrl = profileUrl;
  }

  public void setRoles(List<Scim2ComplexType> roles)
  {
    this.roles = roles;
  }

  public void setSchemas(List<String> schemas)
  {
    this.schemas = schemas;
  }

  public void setTimezone(String timezone)
  {
    this.timezone = timezone;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public void setUserName(String userName)
  {
    this.userName = userName;
  }

  public void setUserType(String userType)
  {
    this.userType = userType;
  }

  public void setX509Certificates(List<Scim2ComplexType> x509Certificates)
  {
    this.x509Certificates = x509Certificates;
  }
}
