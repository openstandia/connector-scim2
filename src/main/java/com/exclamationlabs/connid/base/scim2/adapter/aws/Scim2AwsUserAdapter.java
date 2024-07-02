package com.exclamationlabs.connid.base.scim2.adapter.aws;

import com.exclamationlabs.connid.base.connector.adapter.BaseAdapter;
import com.exclamationlabs.connid.base.connector.attribute.ConnectorAttribute;
import com.exclamationlabs.connid.base.scim2.configuration.Scim2Configuration;
import com.exclamationlabs.connid.base.scim2.model.slack.Scim2SlackUser;
import java.util.Set;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;

public class Scim2AwsUserAdapter extends BaseAdapter<Scim2SlackUser, Scim2Configuration> {

  @Override
  public ObjectClass getType() {
    return null;
  }

  @Override
  public Class<Scim2SlackUser> getIdentityModelClass() {
    return null;
  }

  @Override
  public Set<ConnectorAttribute> getConnectorAttributes() {

    return null;
  }

  @Override
  protected Set<Attribute> constructAttributes(Scim2SlackUser model) {
    return null;
  }

  @Override
  protected Scim2SlackUser constructModel(
      Set<Attribute> attributes,
      Set<Attribute> addedMultiValueAttributes,
      Set<Attribute> removedMultiValueAttributes,
      boolean isCreate) {
    return null;
  }
}
