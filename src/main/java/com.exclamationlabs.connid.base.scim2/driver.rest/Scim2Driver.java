package com.exclamationlabs.connid.base.scim2.driver.rest;

import com.exclamationlabs.connid.base.connector.authenticator.Authenticator;
import com.exclamationlabs.connid.base.connector.driver.BaseDriver;
import com.exclamationlabs.connid.base.scim2.configuration.Scim2Configuration;
import com.exclamationlabs.connid.base.scim2.model.Scim2Group;
import com.exclamationlabs.connid.base.scim2.model.Scim2User;
import org.identityconnectors.framework.common.exceptions.ConnectorException;

public class Scim2Driver extends BaseDriver<Scim2Configuration> {

  public Scim2Driver() {
    super();
    addInvocator(Scim2User.class, new Scim2UsersInvocator());
    addInvocator(Scim2Group.class, new Scim2GroupsInvocator());
  }

  @Override
  public void initialize(
      Scim2Configuration configuration, Authenticator<Scim2Configuration> authenticator)
      throws ConnectorException {}

  @Override
  public void test() throws ConnectorException {}

  @Override
  public void close() {}
}
