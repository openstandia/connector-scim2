package com.exclamationlabs.connid.base.scim2.adapter;

import com.exclamationlabs.connid.base.connector.adapter.AdapterValueTypeConverter;
import com.exclamationlabs.connid.base.connector.adapter.BaseAdapter;
import com.exclamationlabs.connid.base.connector.attribute.ConnectorAttribute;
import com.exclamationlabs.connid.base.connector.attribute.ConnectorAttributeDataType;
import com.exclamationlabs.connid.base.scim2.configuration.Scim2Configuration;
import com.exclamationlabs.connid.base.scim2.model.CustomScimComplexType;
import com.exclamationlabs.connid.base.scim2.model.Scim2Group;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.exclamationlabs.connid.base.scim2.model.Scim2Schema;
import com.exclamationlabs.connid.base.scim2.model.SubAttribute;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.AttributeInfo;
import org.identityconnectors.framework.common.objects.ObjectClass;

import static com.exclamationlabs.connid.base.scim2.attribute.Scim2GroupAttribute.GROUP_NAME;
import static com.exclamationlabs.connid.base.scim2.attribute.Scim2GroupAttribute.TOTAL_MEMBERS;
import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

public class Scim2GroupsAdapter extends BaseAdapter<Scim2Group, Scim2Configuration> {

  @Override
  public ObjectClass getType() {
    return ObjectClass.GROUP;
  }

  @Override
  public Class<Scim2Group> getIdentityModelClass() {
    return Scim2Group.class;
  }

  @Override
  public Set<ConnectorAttribute> getConnectorAttributes() {
    String rawJson = getConfiguration().getSchemaRawJson();
    System.out.println("RAW JSON ---> " + rawJson);
    ObjectMapper objectMapper = new ObjectMapper();
    List<Scim2Schema> schemaPojo = null;

    try {
      schemaPojo =
              objectMapper.readValue(rawJson, new TypeReference<List<Scim2Schema>>() {});
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    Set<ConnectorAttribute> result = new HashSet<>();
    schemaPojo.forEach(
            obj -> {
              if(obj.getId().equalsIgnoreCase("urn:ietf:params:scim:schemas:core:2.0:Group")){
                List<com.exclamationlabs.connid.base.scim2.model.Attribute> userAttributes = obj.getAttributes();

                for (com.exclamationlabs.connid.base.scim2.model.Attribute userAttribute : userAttributes) {

                  if(userAttribute.getType().equalsIgnoreCase("complex")){

                      //CustomScimComplexType customScimComplexType = objectMapper.readValue(userAttribute.getSubAttributes().toString(),CustomScimComplexType.class);


                      for(SubAttribute subAttribute : userAttribute.getSubAttributes()){
                        if(!subAttribute.getName().equalsIgnoreCase("$ref"))
                        result.add(
                                new ConnectorAttribute(
                                        subAttribute.getName(),
                                        ConnectorAttributeDataType.valueOf(subAttribute.getType().toUpperCase()),
                                        buildFlags(subAttribute)));
                      }

                  }else
                  {
                    result.add(
                          new ConnectorAttribute(
                                  userAttribute.getName(),
                                  ConnectorAttributeDataType.valueOf(userAttribute.getType().toUpperCase()),
                                  buildFlags(userAttribute)));

                  if (userAttribute.getSubAttributes() != null) {
                    for (SubAttribute subAttribute : userAttribute.getSubAttributes()) {
                      result.add(
                              new ConnectorAttribute(
                                      subAttribute.getName(),
                                      ConnectorAttributeDataType.valueOf(subAttribute.getType().toUpperCase()),
                                      buildFlags(subAttribute)));

                    }
                  }
                }

                }


              }
            });
    return result;
  }

  Set<AttributeInfo.Flags> buildFlags(com.exclamationlabs.connid.base.scim2.model.Attribute attribute){

    Set<AttributeInfo.Flags> flagsSet = new HashSet<>();
    boolean multiValued = attribute.getMultiValued()!=null ? attribute.getMultiValued(): false;
    boolean required = attribute.getRequired() != null ? attribute.getRequired() : false;
    boolean caseExact = attribute.getCaseExact() != null ? attribute.getCaseExact():false;
    String mutability = attribute.getMutability() != null ? attribute.getMutability():"";
    String returned = attribute.getReturned() != null ? attribute.getReturned():"";
    String uniqueness = attribute.getUniqueness() != null ? attribute.getUniqueness():"";
    if(multiValued)
      flagsSet.add(AttributeInfo.Flags.valueOf("MULTIVALUED"));

    if(required)
      flagsSet.add(AttributeInfo.Flags.valueOf("REQUIRED"));


    // if(caseExact)
    //   list.add(AttributeInfo.Subtypes.valueOf("MULTIVALUED"));


    if(mutability.equalsIgnoreCase("readOnly"))
      flagsSet.add(AttributeInfo.Flags.valueOf("NOT_UPDATEABLE"));

    if(mutability.equalsIgnoreCase("writeOnly"))
      flagsSet.add(AttributeInfo.Flags.valueOf("NOT_READABLE"));


    if(returned.equalsIgnoreCase("never"))
      flagsSet.add(AttributeInfo.Flags.valueOf("NOT_RETURNED_BY_DEFAULT"));

    if(uniqueness.equalsIgnoreCase("server"))
      flagsSet.add(AttributeInfo.Flags.valueOf("NOT_CREATABLE"));

    return flagsSet;

  }

  Set<AttributeInfo.Flags> buildFlags(com.exclamationlabs.connid.base.scim2.model.SubAttribute attribute){

    Set<AttributeInfo.Flags> flagsSet = new HashSet<>();
    boolean multiValued = attribute.getMultiValued()!=null ? attribute.getMultiValued(): false;
    boolean required = attribute.getRequired() != null ? attribute.getRequired() : false;
    boolean caseExact = attribute.getCaseExact() != null ? attribute.getCaseExact():false;
    String mutability = attribute.getMutability() != null ? attribute.getMutability():"";
    String returned = attribute.getReturned() != null ? attribute.getReturned():"";
    String uniqueness = attribute.getUniqueness() != null ? attribute.getUniqueness():"";
    if(multiValued)
      flagsSet.add(AttributeInfo.Flags.valueOf("MULTIVALUED"));

    if(required)
      flagsSet.add(AttributeInfo.Flags.valueOf("REQUIRED"));


    // if(caseExact)
    //   list.add(AttributeInfo.Subtypes.valueOf("MULTIVALUED"));


    if(mutability.equalsIgnoreCase("readOnly"))
      flagsSet.add(AttributeInfo.Flags.valueOf("NOT_UPDATEABLE"));

    if(mutability.equalsIgnoreCase("writeOnly"))
      flagsSet.add(AttributeInfo.Flags.valueOf("NOT_READABLE"));


    if(returned.equalsIgnoreCase("never"))
      flagsSet.add(AttributeInfo.Flags.valueOf("NOT_RETURNED_BY_DEFAULT"));

    if(uniqueness.equalsIgnoreCase("server"))
      flagsSet.add(AttributeInfo.Flags.valueOf("NOT_CREATABLE"));

    return flagsSet;

  }

  @Override
  protected Set<Attribute> constructAttributes(Scim2Group group) {
    Set<Attribute> attributes = new HashSet<>();
    attributes.add(AttributeBuilder.build(TOTAL_MEMBERS.name(), group.getTotalMembers()));

    return attributes;

  }

  @Override
  protected Scim2Group constructModel(
      Set<Attribute> attributes,
      Set<Attribute> addedMultiValueAttributes,
      Set<Attribute> removedMultiValueAttributes,
      boolean isCreate) {

    Scim2Group group = new Scim2Group();
    group.setId(AdapterValueTypeConverter.getIdentityIdAttributeValue(attributes));
    group.setName(
            AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, GROUP_NAME));
    group.setTotalMembers(
            AdapterValueTypeConverter.getSingleAttributeValue(
                    Integer.class, attributes, TOTAL_MEMBERS));
    return group;

  }
}
