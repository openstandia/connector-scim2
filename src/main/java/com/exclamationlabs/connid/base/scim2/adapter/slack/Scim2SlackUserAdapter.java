package com.exclamationlabs.connid.base.scim2.adapter.slack;

import com.exclamationlabs.connid.base.connector.adapter.AdapterValueTypeConverter;
import com.exclamationlabs.connid.base.connector.adapter.BaseAdapter;
import com.exclamationlabs.connid.base.connector.attribute.ConnectorAttribute;
import com.exclamationlabs.connid.base.connector.attribute.ConnectorAttributeDataType;
import com.exclamationlabs.connid.base.scim2.adapter.Scim2UserAdapter;
import com.exclamationlabs.connid.base.scim2.configuration.Scim2Configuration;
import com.exclamationlabs.connid.base.scim2.model.Scim2Name;
import com.exclamationlabs.connid.base.scim2.model.Scim2Schema;
import com.exclamationlabs.connid.base.scim2.model.Scim2User;
import com.exclamationlabs.connid.base.scim2.model.slack.Scim2SlackUser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import org.identityconnectors.framework.common.objects.*;

import java.io.IOException;
import java.util.*;

import static com.exclamationlabs.connid.base.scim2.attribute.slack.Scim2SlackUserAttribute.*;

//public class Scim2SlackUserAdapter extends BaseAdapter<Scim2SlackUser, Scim2Configuration> {
public class Scim2SlackUserAdapter  extends BaseAdapter<Scim2SlackUser, Scim2Configuration> {
  @Override
  public ObjectClass getType() {
    return ObjectClass.ACCOUNT;
  }

  @Override
  public Class<Scim2SlackUser> getIdentityModelClass() {
    return Scim2SlackUser.class;
  }


  private Scim2Schema schema;
  private static final String SCIM_USER_ENDPOINT = "https://api.slack.com/scim/v2/Users";
  private static final String SCIM_USER_SCHEMA_ENDPOINT = "https://api.slack.com/scim/v2/Schemas/User";
  public Scim2SlackUserAdapter() {
      //    this.schema = fetchSchema();
     // System.out.println(this.schema);
  }
  private Scim2Schema fetchSchema() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
    /*try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      HttpGet request = new HttpGet(SCIM_USER_SCHEMA_ENDPOINT);
      request.setHeader("Content-type", "application/json");
      try (CloseableHttpResponse response = httpClient.execute(request)) {
        int statusCode = response.getStatusLine().getStatusCode();
        HttpEntity entity = response.getEntity();
        String responseContent = entity != null ? EntityUtils.toString(entity) : null;


        if (response.getStatusLine().getStatusCode() != 200) {
          throw new RuntimeException("Failed to fetch schema: " + response.getStatusLine());
        }

        objectMapper.readValue(responseContent, Scim2Schema.class);

      }
    }*/

    String rawJson = getConfiguration().getSchemaRawJson();
    return objectMapper.readValue(rawJson, Scim2Schema.class);
  }

    private void addAttributeToMap(Map<String, Object> map, Attribute attribute, List<Scim2Schema.Attribute> schemaAttributes) {
      for (Scim2Schema.Attribute schemaAttr : schemaAttributes) {
        if (schemaAttr.name.equals(attribute.getName())) {
          if (schemaAttr.multiValued) {
            List<Object> values = new ArrayList<>();
            for (Object value : attribute.getValue()) {
              if (schemaAttr.subAttributes != null && !schemaAttr.subAttributes.isEmpty()) {
                Map<String, Object> subMap = new HashMap<>();
                for (Attribute subAttr : (List<Attribute>) value) {
                  addAttributeToMap(subMap, subAttr, schemaAttr.subAttributes);
                }
                values.add(subMap);
              } else {
                values.add(value);
              }
            }
            map.put(schemaAttr.name, values);
          } else {
            if (schemaAttr.subAttributes != null && !schemaAttr.subAttributes.isEmpty()) {
              Map<String, Object> subMap = new HashMap<>();
              for (Attribute subAttr : (List<Attribute>) attribute.getValue().get(0)) {
                addAttributeToMap(subMap, subAttr, schemaAttr.subAttributes);
              }
              map.put(schemaAttr.name, subMap);
            } else {
              map.put(schemaAttr.name, attribute.getValue().get(0));
            }
          }
          break;
        }
      }
    }
  private void addAttributesToInfoSet(Set<ConnectorAttribute> attributeInfos, List<Scim2Schema.Attribute> schemaAttributes, String parentPath) {
    for (Scim2Schema.Attribute schemaAttr : schemaAttributes) {
      String fullPath = parentPath.isEmpty() ? schemaAttr.name : parentPath + "." + schemaAttr.name;
     // AttributeInfoBuilder builder = new AttributeInfoBuilder(fullPath);

      ConnectorAttribute builder1 = null;

     // builder.setMultiValued(schemaAttr.multiValued);
     // builder.setRequired(schemaAttr.required);

      if (schemaAttr.type.equalsIgnoreCase("string") || schemaAttr.type.equalsIgnoreCase("complex")) {
        //builder.setType(String.class);
        builder1 = new ConnectorAttribute(fullPath,ConnectorAttributeDataType.valueOf("STRING"),buildFlags(schemaAttr));
      } else if (schemaAttr.type.equalsIgnoreCase("boolean")) {
        //builder.setType(Boolean.class);
        builder1 = new ConnectorAttribute(fullPath,ConnectorAttributeDataType.valueOf("BOOLEAN"),buildFlags(schemaAttr));
      } else if (schemaAttr.type.equalsIgnoreCase("decimal")) {
        //builder.setType(Double.class);
        builder1 = new ConnectorAttribute(fullPath,ConnectorAttributeDataType.valueOf("BIG_DECIMAL"),buildFlags(schemaAttr));
      } else if (schemaAttr.type.equalsIgnoreCase("integer")) {
        //builder.setType(Integer.class);
        builder1 = new ConnectorAttribute(fullPath,ConnectorAttributeDataType.valueOf("INTEGER"),buildFlags(schemaAttr));
      } else if (schemaAttr.type.equalsIgnoreCase("datetime")) {
        //builder.setType(Long.class); // Typically UNIX timestamp
        builder1 = new ConnectorAttribute(fullPath,ConnectorAttributeDataType.valueOf("ZONED_DATE_TIME"),buildFlags(schemaAttr));
      }

      if (schemaAttr.subAttributes != null && !schemaAttr.subAttributes.isEmpty()) {
        addAttributesToInfoSet(attributeInfos, schemaAttr.subAttributes, fullPath);
      }

      //attributeInfos.add(builder.build());
      attributeInfos.add(builder1);
    }
  }
  @Override
  public Set<ConnectorAttribute> getConnectorAttributes() {
    // if configuration standard user/enterprise user, case stmt, Build ConnectorAttribute
    // accordingly
    // Remember Enterprise user is a super set of User
    // If user Dynamic, Build ConnectorAttribute accordingly
    String rawJson = getConfiguration().getSchemaRawJson();
    Scim2SlackUserAdapter scim2SlackUserAdapter = new Scim2SlackUserAdapter();

    System.out.println("RAW JSON ---> " + rawJson);
    ObjectMapper objectMapper = new ObjectMapper();
    List<Scim2Schema> schemaPojo = null;
    Map<String, Object> userMap = new HashMap<>();
    Set<ConnectorAttribute> attributeInfos = new HashSet<>();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    try {
      schemaPojo = objectMapper.readValue(rawJson, new TypeReference<List<Scim2Schema>>() {
      });
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    Set<ConnectorAttribute> result = new HashSet<>();
    schemaPojo.forEach(
            obj -> {
              if (obj.getId().equalsIgnoreCase("urn:ietf:params:scim:schemas:core:2.0:User")) {
               // scim2SlackUserAdapter.setStandardUserSchema(obj);
                List<Scim2Schema.Attribute> userAttributes =
                        obj.getAttributes();
                addAttributesToInfoSet(attributeInfos, userAttributes, "");
              }
            });
    attributeInfos.removeIf(Objects::isNull);
    return attributeInfos;
  }


  /*@Override
  protected Set<Attribute> constructAttributes(Scim2SlackUser scimSlack2User) {
    return null;
  }*/

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
          Scim2Schema.Attribute attribute) {
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

  /*Set<AttributeInfo.Flags> buildFlags(
      SubAttribute attribute) {
    return getFlags(
        attribute.getMultiValued(),
        attribute.getRequired(),
        attribute.getCaseExact(),
        attribute.getMutability(),
        attribute.getReturned(),
        attribute.getUniqueness());
  }*/

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
  protected Set<Attribute> constructAttributes(Scim2SlackUser user) {
    Set<Attribute> attributes = new HashSet<>();

    attributes.add(AttributeBuilder.build(USERNAME.name(), user.getUserName()));
    attributes.add(AttributeBuilder.build(DISPLAY_NAME.name(), user.getDisplayName()));
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
    attributes.add(AttributeBuilder.build(EMAILS.name(), user.getScim2Emails()));
    attributes.add(AttributeBuilder.build(SCIM2_ADDRESS.name(), user.getScim2Addresses()));

    return attributes;
  }

  @Override
  protected Scim2SlackUser constructModel(
      Set<Attribute> attributes,
      Set<Attribute> addedMultiValueAttributes,
      Set<Attribute> removedMultiValueAttributes,
      boolean isCreate) {
    Scim2SlackUser user = new Scim2SlackUser();


    user.setId(AdapterValueTypeConverter.getIdentityIdAttributeValue(attributes));
    user.setUserName(AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, USERNAME));
    user.setDisplayName(AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, DISPLAY_NAME));
    Scim2Name scim2Name= new Scim2Name();
    scim2Name.setName(AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, NAME));
    scim2Name.setFormatted(AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, Scim2UserName));
    //Scim2Name



    /*private String name;
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
    private String honorificsuffix;*/





    user.setDisplayName(AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, DISPLAY_NAME));
    user.setDisplayName(AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, DISPLAY_NAME));
    user.setDisplayName(AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, DISPLAY_NAME));






    user.setId(AdapterValueTypeConverter.getIdentityIdAttributeValue(attributes));
    //user.se
    // user.setUserName(AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes,
    // "userName"));
    Scim2Name userName = new Scim2Name();
    // userName.setName();

  //  user.setScim2Name(userName);
    return user;
  }
}




