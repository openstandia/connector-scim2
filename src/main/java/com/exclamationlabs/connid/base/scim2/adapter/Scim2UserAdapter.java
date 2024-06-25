package com.exclamationlabs.connid.base.scim2.adapter;

import com.exclamationlabs.connid.base.connector.adapter.AdapterValueTypeConverter;
import com.exclamationlabs.connid.base.connector.adapter.BaseAdapter;
import com.exclamationlabs.connid.base.connector.attribute.ConnectorAttribute;
import com.exclamationlabs.connid.base.scim2.adapter.standard.Scim2StandardUserAdapter;
import com.exclamationlabs.connid.base.scim2.configuration.Scim2Configuration;
import com.exclamationlabs.connid.base.scim2.model.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import java.io.IOException;
import java.util.*;
import java.util.HashMap;
import java.util.Map;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.common.objects.Attribute;

public class Scim2UserAdapter extends BaseAdapter<Scim2User, Scim2Configuration> {
  @Override
  public ObjectClass getType() {
    return ObjectClass.ACCOUNT;
  }

  @Override
  public Class<Scim2User> getIdentityModelClass() {
    return Scim2User.class;
  }

  @Override
  public Set<ConnectorAttribute> getConnectorAttributes() {
    // if configuration standard user/enterprise user, case stmt, Build ConnectorAttribute
    // accordingly
    // Remember Enterprise user is a super set of User
    // If user Dynamic, Build ConnectorAttribute accordingly
    //follow is the order of preference  if user selects more than one true in the mid point UI
    Boolean isSlack = getConfiguration().getEnableSlackSchema();
    Boolean isAWS = getConfiguration().getEnableAWSSchema();
    Boolean isStandard = getConfiguration().getEnableStandardSchema();
    Boolean isDynamic = getConfiguration().getEnableDynamicSchema();


    String rawJson = getConfiguration().getSchemaRawJson();
    Scim2StandardUserAdapter scim2StandardUserAdapter = new Scim2StandardUserAdapter();

    Gson gson = new Gson();
    JsonElement jsonElement = JsonParser.parseString(rawJson);

    if (jsonElement.isJsonArray()) {
      JsonArray jsonArray = jsonElement.getAsJsonArray();
      for (JsonElement element : jsonArray) {
        Map<String, Object> schemaMap = parseJsonElement(element);
        printSchema(schemaMap, 0);
      }
    }

    System.out.println("RAW JSON ---> " + rawJson);
    ObjectMapper objectMapper = new ObjectMapper();
    List<Scim2Schema> schemaPojo = null;

    try {
      schemaPojo = objectMapper.readValue(rawJson, new TypeReference<List<Scim2Schema>>() {});
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    Set<ConnectorAttribute> result = new HashSet<>();
    schemaPojo.forEach(
        obj -> {/*
          if (obj.getId().equalsIgnoreCase("urn:ietf:params:scim:schemas:core:2.0:User")) {
            scim2StandardUserAdapter.setStandardUserSchema(obj);

            List<Scim2Schema.Attribute> userAttributes =
                obj.getAttributes();

            for (Scim2Schema.Attribute userAttribute :
                userAttributes) {

              if (userAttribute.getType().equalsIgnoreCase("complex")) {

                if (!userAttribute.getType().equalsIgnoreCase("reference"))
                  for (SubAttribute subAttribute : userAttribute.getSubAttributes()) {
                    if (!(subAttribute.getType().equalsIgnoreCase("reference")
                        || subAttribute.getType().equalsIgnoreCase("binary")))
                      result.add(
                          new ConnectorAttribute(
                              subAttribute.getName(),
                              ConnectorAttributeDataType.valueOf(
                                  subAttribute.getType().toUpperCase()),
                              buildFlags(subAttribute)));
                  }

              } else {
                if (!userAttribute.getType().equalsIgnoreCase("reference")) {
                  result.add(
                      new ConnectorAttribute(
                          userAttribute.getName(),
                          ConnectorAttributeDataType.valueOf(userAttribute.getType().toUpperCase()),
                          buildFlags(userAttribute)));
                }
                if (userAttribute.getSubAttributes() != null) {
                  for (SubAttribute subAttribute : userAttribute.getSubAttributes()) {
                    if (!(subAttribute.getType().equalsIgnoreCase("reference")
                        || subAttribute.getType().equalsIgnoreCase("binary")))
                      result.add(
                          new ConnectorAttribute(
                              subAttribute.getName(),
                              ConnectorAttributeDataType.valueOf(
                                  subAttribute.getType().toUpperCase()),
                              buildFlags(subAttribute)));
                  }
                }
              }
            }
          }*/
        });
    return result;
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

  Set<AttributeInfo.Flags> buildFlags(
      com.exclamationlabs.connid.base.scim2.model.Attribute attribute) {
    return getFlags(
        attribute.getMultiValued(),
        attribute.getRequired(),
        attribute.getCaseExact(),
        attribute.getMutability(),
        attribute.getReturned(),
        attribute.getUniqueness());
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

  Set<AttributeInfo.Flags> buildFlags(
      com.exclamationlabs.connid.base.scim2.model.SubAttribute attribute) {
    return getFlags(
        attribute.getMultiValued(),
        attribute.getRequired(),
        attribute.getCaseExact(),
        attribute.getMutability(),
        attribute.getReturned(),
        attribute.getUniqueness());
  }

  public static Map<String, Object> parseJsonElement(JsonElement jsonElement) {
    Map<String, Object> map = new HashMap<>();
    Gson gson = new Gson();

    if (jsonElement.isJsonObject()) {
      JsonObject jsonObject = jsonElement.getAsJsonObject();
      for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
        JsonElement value = entry.getValue();
        if (value.isJsonObject()) {
          map.put(entry.getKey(), parseJsonElement(value));
        } else {
          map.put(entry.getKey(), gson.fromJson(value, Object.class));
        }
      }
    }

    return map;
  }

  public static void printSchema(Map<String, Object> schemaMap, int depth) {
    for (Map.Entry<String, Object> entry : schemaMap.entrySet()) {
      String indentation = "  ".repeat(depth);
      System.out.println(indentation + "Property: " + entry.getKey());
      Object value = entry.getValue();
      if (value instanceof Map) {
        printSchema((Map<String, Object>) value, depth + 1);
      } else {
        System.out.println(indentation + "  " + "Type: " + value);
      }
    }
  }

  @Override
  protected Set<Attribute> constructAttributes(Scim2User model) {
    return null;
  }

  @Override
  protected Scim2User constructModel(
      Set<Attribute> attributes,
      Set<Attribute> addedMultiValueAttributes,
      Set<Attribute> removedMultiValueAttributes,
      boolean isCreate) {
    Scim2User user = new Scim2User();

    user.setId(AdapterValueTypeConverter.getIdentityIdAttributeValue(attributes));
    // user.setUserName(AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes,
    // "userName"));
    Scim2Name userName = new Scim2Name();
    // userName.setName();

    user.setScim2Name(userName);
    return user;
  }
}
