package com.exclamationlabs.connid.base.scim2.driver.rest.slack;

import com.exclamationlabs.connid.base.connector.driver.DriverInvocator;
import com.exclamationlabs.connid.base.scim2.driver.rest.Scim2Driver;
import com.exclamationlabs.connid.base.scim2.model.slack.Scim2SlackGroup;
import java.util.Map;
import org.identityconnectors.framework.common.exceptions.ConnectorException;

public class Scim2SlackGroupInvocator implements DriverInvocator<Scim2Driver, Scim2SlackGroup> {

  @Override
  public String create(Scim2Driver driver, Scim2SlackGroup model) throws ConnectorException {
    return null;
  }

  @Override
  public void update(Scim2Driver driver, String userId, Scim2SlackGroup userModel)
      throws ConnectorException {}

  @Override
  public void delete(Scim2Driver driver, String userId) throws ConnectorException {}

  @Override
  public Scim2SlackGroup getOne(
      Scim2Driver driver, String objectId, Map<String, Object> prefetchDataMap)
      throws ConnectorException {
    return null;
  }
}
