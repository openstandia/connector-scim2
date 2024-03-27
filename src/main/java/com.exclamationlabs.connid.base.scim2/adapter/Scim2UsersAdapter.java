package com.exclamationlabs.connid.base.scim2.adapter;

import com.exclamationlabs.connid.base.connector.adapter.BaseAdapter;
import com.exclamationlabs.connid.base.connector.attribute.ConnectorAttribute;
import com.exclamationlabs.connid.base.scim2.configuration.Scim2Configuration;
import com.exclamationlabs.connid.base.scim2.model.Scim2User;
import java.util.Set;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;

public class Scim2UsersAdapter extends BaseAdapter<Scim2User, Scim2Configuration> {
  @Override
  public ObjectClass getType() {
    return null;
  }

  @Override
  public Class<Scim2User> getIdentityModelClass() {
    return null;
  }

  @Override
  public Set<ConnectorAttribute> getConnectorAttributes() {
    return null;
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
    return null;
  }
}
