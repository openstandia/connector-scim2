package com.exclamationlabs.connid.base.scim2.adapter.slack;

import static com.exclamationlabs.connid.base.scim2.attribute.slack.Scim2SlackUserAttribute.*;

import com.exclamationlabs.connid.base.connector.adapter.AdapterValueTypeConverter;
import com.exclamationlabs.connid.base.connector.adapter.BaseAdapter;
import com.exclamationlabs.connid.base.connector.attribute.ConnectorAttribute;
import com.exclamationlabs.connid.base.connector.attribute.ConnectorAttributeDataType;
import com.exclamationlabs.connid.base.scim2.configuration.Scim2Configuration;
import com.exclamationlabs.connid.base.scim2.model.*;
import com.exclamationlabs.connid.base.scim2.model.slack.Scim2SlackUser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import java.io.IOException;
import java.util.*;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.common.objects.Attribute;

public class Scim2SlackUserAdapter extends BaseAdapter<Scim2SlackUser, Scim2Configuration> {
  @Override
  public ObjectClass getType() {
    return ObjectClass.ACCOUNT;
  }

  public String getConfig() {
    return config;
  }

  public void setConfig(String config) {
    this.config = config;
  }

  String config;

  @Override
  public Class<Scim2SlackUser> getIdentityModelClass() {
    return Scim2SlackUser.class;
  }

  private void addAttributesToInfoSet(
      Set<ConnectorAttribute> attributeInfos,
      List<Scim2Schema.Attribute> schemaAttributes,
      String parentPath) {
    for (Scim2Schema.Attribute schemaAttr : schemaAttributes) {
      String fullPath = parentPath.isEmpty() ? schemaAttr.name : parentPath + "." + schemaAttr.name;
      // AttributeInfoBuilder builder = new AttributeInfoBuilder(fullPath);

      ConnectorAttribute builder1 = null;

      // builder.setMultiValued(schemaAttr.multiValued);
      // builder.setRequired(schemaAttr.required);

      if (schemaAttr.type.equalsIgnoreCase("string")
          || schemaAttr.type.equalsIgnoreCase("complex")) {
        // builder.setType(String.class);
        builder1 =
            new ConnectorAttribute(
                fullPath, ConnectorAttributeDataType.valueOf("STRING"), buildFlags(schemaAttr));
      } else if (schemaAttr.type.equalsIgnoreCase("boolean")) {
        // builder.setType(Boolean.class);
        builder1 =
            new ConnectorAttribute(
                fullPath, ConnectorAttributeDataType.valueOf("BOOLEAN"), buildFlags(schemaAttr));
      } else if (schemaAttr.type.equalsIgnoreCase("decimal")) {
        // builder.setType(Double.class);
        builder1 =
            new ConnectorAttribute(
                fullPath,
                ConnectorAttributeDataType.valueOf("BIG_DECIMAL"),
                buildFlags(schemaAttr));
      } else if (schemaAttr.type.equalsIgnoreCase("integer")) {
        // builder.setType(Integer.class);
        builder1 =
            new ConnectorAttribute(
                fullPath, ConnectorAttributeDataType.valueOf("INTEGER"), buildFlags(schemaAttr));
      } else if (schemaAttr.type.equalsIgnoreCase("datetime")) {
        // builder.setType(Long.class); // Typically UNIX timestamp
        builder1 =
            new ConnectorAttribute(
                fullPath,
                ConnectorAttributeDataType.valueOf("ZONED_DATE_TIME"),
                buildFlags(schemaAttr));
      }

      if (schemaAttr.subAttributes != null && !schemaAttr.subAttributes.isEmpty()) {
        addAttributesToInfoSet(attributeInfos, schemaAttr.subAttributes, fullPath);
      }

      // attributeInfos.add(builder.build());
      attributeInfos.add(builder1);
    }
  }

  @Override
  public Set<ConnectorAttribute> getConnectorAttributes() {
    String rawJson = getConfig();
    System.out.println("RAW JSON ---> " + rawJson);
    ObjectMapper objectMapper = new ObjectMapper();
    List<Scim2Schema> schemaPojo = null;
    Map<String, Object> userMap = new HashMap<>();
    Set<ConnectorAttribute> attributeInfos = new HashSet<>();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    try {
      schemaPojo = objectMapper.readValue(rawJson, new TypeReference<List<Scim2Schema>>() {});
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    Set<ConnectorAttribute> result = new HashSet<>();
    schemaPojo.forEach(
        obj -> {
          if (obj.getId().equalsIgnoreCase("urn:ietf:params:scim:schemas:core:2.0:User")) {
            // scim2SlackUserAdapter.setStandardUserSchema(obj);
            List<Scim2Schema.Attribute> userAttributes = obj.getAttributes();
            addAttributesToInfoSet(attributeInfos, userAttributes, "");
          }
        });
    attributeInfos.removeIf(Objects::isNull);
    return attributeInfos;
  }

  private void processAttributeFlags(
      Set<AttributeInfo.Flags> flagsSet,
      boolean multiValued,
      boolean required,
      boolean caseExact,
      String mutability,
      String returned,
      String uniqueness) {
    if (multiValued) {
      flagsSet.add(AttributeInfo.Flags.MULTIVALUED);
    }
    if (required) {
      flagsSet.add(AttributeInfo.Flags.REQUIRED);
    }
    // if (caseExact) {
    //     list.add(AttributeInfo.Subtypes.MULTIVALUED);
    // }
    if ("readOnly".equalsIgnoreCase(mutability)) {
      flagsSet.add(AttributeInfo.Flags.NOT_UPDATEABLE);
    }
    if ("writeOnly".equalsIgnoreCase(mutability)) {
      flagsSet.add(AttributeInfo.Flags.NOT_READABLE);
    }
    if ("never".equalsIgnoreCase(returned)) {
      flagsSet.add(AttributeInfo.Flags.NOT_RETURNED_BY_DEFAULT);
    }
    if ("server".equalsIgnoreCase(uniqueness)) {
      flagsSet.add(AttributeInfo.Flags.NOT_CREATABLE);
    }
  }

  Set<AttributeInfo.Flags> buildFlags(Scim2Schema.Attribute attribute) {
    return getFlags(
        attribute.multiValued,
        attribute.required,
        attribute.caseExact,
        attribute.mutability,
        attribute.returned,
        attribute.uniqueness);
  }

  private Set<AttributeInfo.Flags> getFlags(
      Boolean multiValued,
      Boolean required,
      Boolean caseExact,
      String mutability,
      String returned,
      String uniqueness) {
    Set<AttributeInfo.Flags> flagsSet = new HashSet<>();
    processAttributeFlags(
        flagsSet,
        multiValued != null ? multiValued : false,
        required != null ? required : false,
        caseExact != null ? caseExact : false,
        mutability != null ? mutability : "",
        returned != null ? returned : "",
        uniqueness != null ? uniqueness : "");
    return flagsSet;
  }

  @Override
  public Set<Attribute> constructAttributes(Scim2SlackUser user) {
    Set<Attribute> attributes = new HashSet<>();

    attributes.add(AttributeBuilder.build(userName.name(), user.getUserName()));
    attributes.add(AttributeBuilder.build(nickName.name(), user.getNickName()));
    attributes.add(AttributeBuilder.build(name.name(), user.getName()));
    attributes.add(AttributeBuilder.build(familyName.name(), user.getName().getFamilyName()));
    attributes.add(AttributeBuilder.build(givenName.name(), user.getName().getGivenName()));
    attributes.add(AttributeBuilder.build(middleName.name(), user.getName().getGivenName()));
    attributes.add(
        AttributeBuilder.build(honorificPrefix.name(), user.getName().getHonorificprefix()));
    attributes.add(
        AttributeBuilder.build(honorificSuffix.name(), user.getName().getHonorificsuffix()));

    attributes.add(
        AttributeBuilder.build(
            streetAddress.name(), user.getAddresses().get(0).getStreetAddress()));
    attributes.add(
        AttributeBuilder.build(locality.name(), user.getAddresses().get(0).getLocality()));
    attributes.add(AttributeBuilder.build(region.name(), user.getAddresses().get(0).getRegion()));
    attributes.add(AttributeBuilder.build(country.name(), user.getAddresses().get(0).getCountry()));
    attributes.add(
        AttributeBuilder.build(postalCode.name(), user.getAddresses().get(0).getPostalCode()));

    /*attributes.add(AttributeBuilder.build(DISPLAY_NAME.name(), user.getDisplayName()));
    attributes.add(AttributeBuilder.build(NICK_NAME.name(), user.getNickName()));
    attributes.add(AttributeBuilder.build(PROFILE_URL.name(), user.getProfileUrl()));
    attributes.add(AttributeBuilder.build(TITLE.name(), user.getTitle()));
    attributes.add(AttributeBuilder.build(USER_TYPE.name(), user.getUserType()));
    attributes.add(AttributeBuilder.build(TITLE.name(), user.getTitle()));
    attributes.add(AttributeBuilder.build(PREFERRED_LANGUAGE.name(), user.getPreferredLanguage()));
    attributes.add(AttributeBuilder.build(LOCALE.name(), user.getLocale()));
    attributes.add(AttributeBuilder.build(TIMEZONE.name(), user.getTimezone()));
    attributes.add(AttributeBuilder.build(ACTIVE.name(), user.isActive()));
    attributes.add(AttributeBuilder.build(PASSWORD.name(), user.getPassword()));
    //attributes.add(AttributeBuilder.build(EMAILS.name(), user.getEmails()));
    attributes.add(AttributeBuilder.build(SCIM2_ADDRESS.name(), user.getAddresses()));*/

    return attributes;
  }

  @Override
  public Scim2SlackUser constructModel(
      Set<Attribute> attributes,
      Set<Attribute> addedMultiValueAttributes,
      Set<Attribute> removedMultiValueAttributes,
      boolean isCreate) {
    Scim2SlackUser user = new Scim2SlackUser();
    user.setId(AdapterValueTypeConverter.getIdentityIdAttributeValue(attributes));
    user.setUserName(
        AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, userName));
    user.setEmails(
        AdapterValueTypeConverter.getMultipleAttributeValue(List.class, attributes, emails));

    String fn =
        AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, familyName);
    String gn =
        AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, givenName);
    Scim2Name name = new Scim2Name();
    name.setFamilyName(fn);
    name.setGivenName(gn);
    user.setName(name);

    String stAddress =
        AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, streetAddress);

    String local =
        AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, locality);
    String reg =
        AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, region);
    String pc =
        AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, postalCode);
    String ctry =
        AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, country);

    Scim2Addresses addresses = new Scim2Addresses();
    addresses.setCountry(ctry);
    addresses.setStreetAddress(stAddress);
    addresses.setRegion(reg);
    addresses.setPostalCode(pc);
    addresses.setLocality(local);
    // user.setAddresses(addresses);
    user.setAddresses(Arrays.asList(addresses));
    user.setSchemas(Arrays.asList("urn:ietf:params:scim:schemas:core:2.0:User"));

    /*  user.setDisplayName(
        AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, DISPLAY_NAME));
    Scim2Name scim2Name = new Scim2Name();

    scim2Name.setFormatted(
        AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, Scim2UserName));

    user.setDisplayName(
        AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, DISPLAY_NAME));
    user.setDisplayName(
        AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, DISPLAY_NAME));
    user.setDisplayName(
        AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, DISPLAY_NAME));

    user.setId(AdapterValueTypeConverter.getIdentityIdAttributeValue(attributes));
    Scim2Name userName = new Scim2Name();*/
    return user;
  }
}
