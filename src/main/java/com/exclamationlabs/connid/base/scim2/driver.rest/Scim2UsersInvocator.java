package com.exclamationlabs.connid.base.scim2.driver.rest;

import com.exclamationlabs.connid.base.connector.driver.DriverInvocator;
import com.exclamationlabs.connid.base.scim2.driver.rest.slack.Scim2SlackGroupInvocator;
import com.exclamationlabs.connid.base.scim2.driver.rest.slack.Scim2SlackUsersInvocator;
import com.exclamationlabs.connid.base.scim2.model.Scim2User;
import java.util.Map;

import com.exclamationlabs.connid.base.scim2.model.slack.Scim2SlackGroup;
import com.exclamationlabs.connid.base.scim2.model.slack.Scim2SlackUser;
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
    Scim2User user = null;
    if(driver.getConfiguration().getEnableSlackSchema()){
      user =  new Scim2SlackUsersInvocator().getOne(driver,objectId,prefetchDataMap);
    }else if(driver.getConfiguration().getEnableAWSSchema()) {
       //AWS Invocator
    }
    return user;
  }
}
