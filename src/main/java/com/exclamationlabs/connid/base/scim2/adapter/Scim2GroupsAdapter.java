package com.exclamationlabs.connid.base.scim2.adapter;

import static com.exclamationlabs.connid.base.scim2.attribute.Scim2GroupAttribute.GROUP_NAME;
import static com.exclamationlabs.connid.base.scim2.attribute.Scim2GroupAttribute.TOTAL_MEMBERS;

import com.exclamationlabs.connid.base.connector.adapter.AdapterValueTypeConverter;
import com.exclamationlabs.connid.base.connector.adapter.BaseAdapter;
import com.exclamationlabs.connid.base.connector.attribute.ConnectorAttribute;
import com.exclamationlabs.connid.base.scim2.adapter.aws.Scim2AwsUserAdapter;
import com.exclamationlabs.connid.base.scim2.adapter.slack.Scim2SlackGroupsAdapter;
import com.exclamationlabs.connid.base.scim2.configuration.Scim2Configuration;
import com.exclamationlabs.connid.base.scim2.model.Scim2Group;
import java.util.HashSet;
import java.util.Set;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.AttributeInfo;
import org.identityconnectors.framework.common.objects.ObjectClass;

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

    if (isSlack) {
      Scim2SlackGroupsAdapter scim2SlackGroupsAdapter = new Scim2SlackGroupsAdapter();
      scim2SlackGroupsAdapter.setConfig(getConfiguration().getSchemaRawJson());
      result = scim2SlackGroupsAdapter.getConnectorAttributes();
    } else if (isAWS) {
      Scim2AwsUserAdapter scim2AwsUserAdapter = new Scim2AwsUserAdapter();
      result = scim2AwsUserAdapter.getConnectorAttributes();
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
