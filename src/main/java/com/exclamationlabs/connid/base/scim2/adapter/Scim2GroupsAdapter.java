package com.exclamationlabs.connid.base.scim2.adapter;

import static com.exclamationlabs.connid.base.connector.attribute.ConnectorAttributeDataType.STRING;
import static com.exclamationlabs.connid.base.scim2.attribute.Scim2GroupAttribute.*;
import static org.identityconnectors.framework.common.objects.AttributeInfo.Flags.*;

import com.exclamationlabs.connid.base.connector.adapter.AdapterValueTypeConverter;
import com.exclamationlabs.connid.base.connector.adapter.BaseAdapter;
import com.exclamationlabs.connid.base.connector.attribute.ConnectorAttribute;
import com.exclamationlabs.connid.base.scim2.adapter.slack.Scim2SlackGroupsAdapter;
import com.exclamationlabs.connid.base.scim2.configuration.Scim2Configuration;
import com.exclamationlabs.connid.base.scim2.model.Scim2Group;
import java.util.HashSet;
import java.util.Set;

import org.identityconnectors.framework.common.objects.*;

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

    Boolean isSlack = getConfiguration().getEnableSlackSchema();
    Boolean isAWS = getConfiguration().getEnableAWSSchema();
    Boolean isStandard = getConfiguration().getEnableStandardSchema();
    Boolean isDynamic = getConfiguration().getEnableDynamicSchema();

    Set<ConnectorAttribute> result = null;

    if ( isSlack || isAWS || isStandard )
    {
      // Slack and Amazon Web Service share the same group attributes as the core scim2 group schema
      result = new HashSet<>();
      result.add(new ConnectorAttribute(Uid.NAME, id.name(), STRING, NOT_UPDATEABLE, REQUIRED));
      // A human-readable name for the Group.
      result.add(new ConnectorAttribute(Name.NAME, displayName.name(), STRING, REQUIRED));
      result.add(new ConnectorAttribute(externalId.name(), STRING));
      /*
       * Identifier for members of the Group.
       * This is a JSON string containing sub attributes
       * value, $ref, and type
       */
      result.add(new ConnectorAttribute(members.name(), STRING, MULTIVALUED));

      // Identifier for the member of this Group.
      result.add(new ConnectorAttribute(members_value.name(), STRING, MULTIVALUED));
      // The URI corresponding to a SCIM resource that is a member of this Group.
      result.add(new ConnectorAttribute(members_$ref.name(), STRING, MULTIVALUED));
      // A label indicating the type of resource. For example, 'User' or 'Group'.
      result.add(new ConnectorAttribute(members_type.name(), STRING, MULTIVALUED));
    }
    else
    {
      /*
       * We are treating this as the dynamic implementation
       */
      Scim2SlackGroupsAdapter scim2SlackGroupsAdapter = new Scim2SlackGroupsAdapter();
      scim2SlackGroupsAdapter.setConfig(getConfiguration().getSchemaRawJson());
      result = scim2SlackGroupsAdapter.getConnectorAttributes();
    }
    return result;
  }

  Set<AttributeInfo.Flags> buildFlags(
      com.exclamationlabs.connid.base.scim2.model.Attribute attribute) {

    Set<AttributeInfo.Flags> flagsSet = new HashSet<>();
    boolean multiValued = attribute.getMultiValued() != null ? attribute.getMultiValued() : false;
    boolean required = attribute.getRequired() != null ? attribute.getRequired() : false;
    boolean caseExact = attribute.getCaseExact() != null ? attribute.getCaseExact() : false;
    String mutability = attribute.getMutability() != null ? attribute.getMutability() : "";
    String returned = attribute.getReturned() != null ? attribute.getReturned() : "";
    String uniqueness = attribute.getUniqueness() != null ? attribute.getUniqueness() : "";
    if (multiValued) flagsSet.add(AttributeInfo.Flags.valueOf("MULTIVALUED"));

    if (required) flagsSet.add(AttributeInfo.Flags.valueOf("REQUIRED"));

    // if(caseExact)
    //   list.add(AttributeInfo.Subtypes.valueOf("MULTIVALUED"));

    if (mutability.equalsIgnoreCase("readOnly"))
      flagsSet.add(AttributeInfo.Flags.valueOf("NOT_UPDATEABLE"));

    if (mutability.equalsIgnoreCase("writeOnly"))
      flagsSet.add(AttributeInfo.Flags.valueOf("NOT_READABLE"));

    if (returned.equalsIgnoreCase("never"))
      flagsSet.add(AttributeInfo.Flags.valueOf("NOT_RETURNED_BY_DEFAULT"));

    if (uniqueness.equalsIgnoreCase("server"))
      flagsSet.add(AttributeInfo.Flags.valueOf("NOT_CREATABLE"));

    return flagsSet;
  }

  Set<AttributeInfo.Flags> buildFlags(
      com.exclamationlabs.connid.base.scim2.model.SubAttribute attribute) {

    Set<AttributeInfo.Flags> flagsSet = new HashSet<>();
    boolean multiValued = attribute.getMultiValued() != null ? attribute.getMultiValued() : false;
    boolean required = attribute.getRequired() != null ? attribute.getRequired() : false;
    boolean caseExact = attribute.getCaseExact() != null ? attribute.getCaseExact() : false;
    String mutability = attribute.getMutability() != null ? attribute.getMutability() : "";
    String returned = attribute.getReturned() != null ? attribute.getReturned() : "";
    String uniqueness = attribute.getUniqueness() != null ? attribute.getUniqueness() : "";
    if (multiValued) flagsSet.add(AttributeInfo.Flags.valueOf("MULTIVALUED"));

    if (required) flagsSet.add(AttributeInfo.Flags.valueOf("REQUIRED"));

    // if(caseExact)
    //   list.add(AttributeInfo.Subtypes.valueOf("MULTIVALUED"));

    if (mutability.equalsIgnoreCase("readOnly"))
      flagsSet.add(AttributeInfo.Flags.valueOf("NOT_UPDATEABLE"));

    if (mutability.equalsIgnoreCase("writeOnly"))
      flagsSet.add(AttributeInfo.Flags.valueOf("NOT_READABLE"));

    if (returned.equalsIgnoreCase("never"))
      flagsSet.add(AttributeInfo.Flags.valueOf("NOT_RETURNED_BY_DEFAULT"));

    if (uniqueness.equalsIgnoreCase("server"))
      flagsSet.add(AttributeInfo.Flags.valueOf("NOT_CREATABLE"));

    return flagsSet;
  }

  @Override
  protected Set<Attribute> constructAttributes(Scim2Group group)
  {
    Set<Attribute> attributes = new HashSet<>();
    attributes.add(AttributeBuilder.build(displayName.name(), group.getDisplayName()));
    attributes.add(AttributeBuilder.build(Name.NAME, group.getIdentityNameValue()));
    attributes.add(AttributeBuilder.build(id.name(), group.getId()));
    attributes.add(AttributeBuilder.build(Uid.NAME, group.getIdentityIdValue()));
    attributes.add(AttributeBuilder.build(externalId.name(), group.getExternalId()));

    if ( group.getMembers() != null && group.getMembers().size() > 0 )
    {
      // TBD create JSON string for each member

    }
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
    group.setDisplayName(
        AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, displayName));
    group.setExternalId(
        AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, externalId));

    return group;
  }
}
