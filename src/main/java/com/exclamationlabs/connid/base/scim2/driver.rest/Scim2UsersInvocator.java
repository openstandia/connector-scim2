package com.exclamationlabs.connid.base.scim2.driver.rest;

import com.exclamationlabs.connid.base.connector.driver.DriverInvocator;
import com.exclamationlabs.connid.base.scim2.model.Scim2User;
import java.util.Map;
import org.identityconnectors.framework.common.exceptions.ConnectorException;

public class Scim2UsersInvocator implements DriverInvocator<Scim2Driver, Scim2User> {

  @Override
  public String create(Scim2Driver driver, Scim2User model) throws ConnectorException {
    return null;
  }

  @Override
  public void update(Scim2Driver driver, String userId, Scim2User userModel)
      throws ConnectorException {}

  @Override
  public void delete(Scim2Driver driver, String userId) throws ConnectorException {}

  @Override
  public Scim2User getOne(Scim2Driver driver, String objectId, Map<String, Object> prefetchDataMap)
      throws ConnectorException {
    return null;
  }
}
