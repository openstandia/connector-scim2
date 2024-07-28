package com.exclamationlabs.connid.base.scim2.adapter;

import com.exclamationlabs.connid.base.connector.adapter.AdapterValueTypeConverter;
import com.exclamationlabs.connid.base.connector.adapter.BaseAdapter;
import com.exclamationlabs.connid.base.connector.attribute.ConnectorAttribute;
import com.exclamationlabs.connid.base.scim2.adapter.aws.Scim2AwsUserAdapter;
import com.exclamationlabs.connid.base.scim2.adapter.slack.Scim2SlackUserAdapter;
import com.exclamationlabs.connid.base.scim2.configuration.Scim2Configuration;
import com.exclamationlabs.connid.base.scim2.model.*;
import com.exclamationlabs.connid.base.scim2.model.slack.Scim2SlackUser;
import java.util.*;
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
    // follow is the order of preference  if user selects more than one true in the mid point UI
    Boolean isSlack = getConfiguration().getEnableSlackSchema();
    Boolean isAWS = getConfiguration().getEnableAWSSchema();
    Boolean isStandard = getConfiguration().getEnableStandardSchema();
    Boolean isDynamic = getConfiguration().getEnableDynamicSchema();

    Set<ConnectorAttribute> result = null;

    if (isSlack) {
      Scim2SlackUserAdapter scim2SlackUserAdapter = new Scim2SlackUserAdapter();
      scim2SlackUserAdapter.setConfig(getConfiguration().getSchemaRawJson());
      result = scim2SlackUserAdapter.getConnectorAttributes();
    } else if (isAWS) {
      Scim2AwsUserAdapter scim2AwsUserAdapter = new Scim2AwsUserAdapter();
      result = scim2AwsUserAdapter.getConnectorAttributes();
    }
    return result;
  }

  @Override
  protected Set<Attribute> constructAttributes(Scim2User user) {
    Set<Attribute> attributes = null;
    if (getConfiguration().getEnableSlackSchema()) {
      attributes = new Scim2SlackUserAdapter().constructAttributes((Scim2SlackUser) user);
    } else if (getConfiguration().getEnableAWSSchema()) {
      // Handle AWS etc..
    }
    return attributes;
  }

  @Override
  protected Scim2User constructModel(
      Set<Attribute> attributes,
      Set<Attribute> addedMultiValueAttributes,
      Set<Attribute> removedMultiValueAttributes,
      boolean isCreate) {
    Set<ConnectorAttribute> ss = getConnectorAttributes();
    Scim2User user = new Scim2User();
    user.setId(AdapterValueTypeConverter.getIdentityIdAttributeValue(attributes));

    if (getConfiguration().getEnableSlackSchema()) {
      user =
          new Scim2SlackUserAdapter()
              .constructModel(
                  attributes, addedMultiValueAttributes, removedMultiValueAttributes, isCreate);
    } else if (getConfiguration().getEnableAWSSchema()) {
      // Handle AWS etc..
    }
    return user;
  }
}
