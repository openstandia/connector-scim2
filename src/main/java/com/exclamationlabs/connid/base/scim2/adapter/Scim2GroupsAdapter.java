package com.exclamationlabs.connid.base.scim2.adapter;

import com.exclamationlabs.connid.base.connector.adapter.BaseAdapter;
import com.exclamationlabs.connid.base.connector.attribute.ConnectorAttribute;
import com.exclamationlabs.connid.base.scim2.configuration.Scim2Configuration;
import com.exclamationlabs.connid.base.scim2.model.Scim2Group;
import java.util.Set;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;

public class Scim2GroupsAdapter extends BaseAdapter<Scim2Group, Scim2Configuration> {

  @Override
  public ObjectClass getType() {
    return null;
  }

  @Override
  public Class<Scim2Group> getIdentityModelClass() {
    return null;
  }

  @Override
  public Set<ConnectorAttribute> getConnectorAttributes() {
    return null;
  }

  @Override
  protected Set<Attribute> constructAttributes(Scim2Group model) {
    return null;
  }

  @Override
  protected Scim2Group constructModel(
      Set<Attribute> attributes,
      Set<Attribute> addedMultiValueAttributes,
      Set<Attribute> removedMultiValueAttributes,
      boolean isCreate) {
    return null;
  }
}
