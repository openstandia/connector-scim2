package com.exclamationlabs.connid.base.scim2.adapter.slack;

import static com.exclamationlabs.connid.base.connector.attribute.ConnectorAttributeDataType.STRING;
import static com.exclamationlabs.connid.base.scim2.attribute.slack.Scim2SlackUserAttribute.*;
import com.exclamationlabs.connid.base.connector.attribute.ConnectorAttribute;
import com.exclamationlabs.connid.base.scim2.adapter.Scim2UserAdapter;
import com.exclamationlabs.connid.base.scim2.model.*;
import com.exclamationlabs.connid.base.scim2.model.slack.Scim2SlackUser;
import java.util.*;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeInfo;

public class Scim2SlackUserAdapter extends Scim2UserAdapter {

  public Set<ConnectorAttribute> getSlackConnectorAttributes(){
    Set<ConnectorAttribute> attributes = new HashSet<>();
    attributes.add(new ConnectorAttribute(guest_type.name(), STRING));
    attributes.add(new ConnectorAttribute(guest_expiration.name(), STRING));
    attributes.add(new ConnectorAttribute(guest_channels.name(), STRING, AttributeInfo.Flags.MULTIVALUED));
    attributes.add(new ConnectorAttribute(profile_startDate.name(), STRING));
    return attributes;
  }

  @Override
  public Set<ConnectorAttribute> getConnectorAttributes() {
    Set<ConnectorAttribute> result = null;
    // call Standard Attributes method
    result = getCoreConnectorAttributes();
    // call enterprise attributes method
    result.addAll(getEnterpriseConnectorAttributes());
    // call slack guest attributes method
    // call slack profile attributes method
    result.addAll(getSlackConnectorAttributes());
    return result;
  }

  @Override
  public Set<Attribute> constructAttributes(Scim2User user) {
    Set<Attribute> attributes = new HashSet<>();
    attributes.addAll(populateCoreAttributes(user));
    attributes.addAll(populateEnterpriseAttributes(user));
    // ToDO Add Guest
    // Todo Add Profile
    return attributes;
  }

  @Override
  public Scim2SlackUser constructModel(
      Set<Attribute> attributes,
      Set<Attribute> addedMultiValueAttributes,
      Set<Attribute> removedMultiValueAttributes,
      boolean isCreate) {
    Scim2SlackUser user = new Scim2SlackUser();
    populateCoreUser(user, attributes, addedMultiValueAttributes, removedMultiValueAttributes, isCreate);
    populateEnterpriseUser(user, attributes, addedMultiValueAttributes, removedMultiValueAttributes, isCreate);
    return user;
  }
}
