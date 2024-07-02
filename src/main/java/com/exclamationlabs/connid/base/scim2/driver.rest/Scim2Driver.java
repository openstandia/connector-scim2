package com.exclamationlabs.connid.base.scim2.driver.rest;

import com.exclamationlabs.connid.base.connector.authenticator.Authenticator;
import com.exclamationlabs.connid.base.connector.driver.rest.BaseRestDriver;
import com.exclamationlabs.connid.base.connector.driver.rest.RestFaultProcessor;
import com.exclamationlabs.connid.base.connector.driver.rest.RestRequest;
import com.exclamationlabs.connid.base.connector.logging.Logger;
import com.exclamationlabs.connid.base.connector.model.IdentityModel;
import com.exclamationlabs.connid.base.scim2.configuration.Scim2Configuration;
import com.exclamationlabs.connid.base.scim2.driver.rest.slack.Scim2SlackGroupInvocator;
import com.exclamationlabs.connid.base.scim2.driver.rest.slack.Scim2SlackUsersInvocator;
import com.exclamationlabs.connid.base.scim2.model.Scim2Group;
import com.exclamationlabs.connid.base.scim2.model.Scim2User;
import com.exclamationlabs.connid.base.scim2.model.slack.Scim2SlackUser;
import org.identityconnectors.framework.common.exceptions.ConnectorException;

public class Scim2Driver extends BaseRestDriver<Scim2Configuration> {

  public Scim2Driver() {
    super();
    if(getConfiguration().getEnableSlackSchema()){
      addInvocator(Scim2SlackUser.class, new Scim2SlackUsersInvocator());
      addInvocator(Scim2Group.class, new Scim2SlackGroupInvocator());
    }else{
      addInvocator(Scim2User.class, new Scim2UsersInvocator());
      addInvocator(Scim2Group.class, new Scim2GroupsInvocator());
    }

  }

  @Override
  protected boolean usesBearerAuthorization() {
    return true;
  }

  @Override
  protected RestFaultProcessor getFaultProcessor() {
    return Scim2FaultProcessor.getInstance();
  }

  @Override
  protected String getBaseServiceUrl() {
    return getConfiguration().getServiceUrl();
  }

  @Override
  public IdentityModel getOneByName(
      Class<? extends IdentityModel> identityModelClass, String nameValue)
      throws ConnectorException {
    return this.getInvocator(identityModelClass).getOneByName(this, nameValue);
  }

  @Override
  public void test() throws ConnectorException {
    try {
      Logger.info(this, "Performing Scim2 Connector Test Procedure");
      Scim2User adminUser =
          executeRequest(
                  new RestRequest.Builder<>(Scim2User.class)
                      .withGet()
                      .withRequestUri("/users/me")
                      .build())
              .getResponseObject();
      // if (adminUser == null || adminUser.getId() == null) {
      //  throw new ConnectorException("Invalid admin user response");
      // }
    } catch (Exception e) {
      throw new ConnectorException("Test for Zoom connection user failed.", e);
    }
  }

  @Override
  public void initialize(
      Scim2Configuration configuration, Authenticator<Scim2Configuration> authenticator)
      throws ConnectorException {
    System.out.println("Scim2 Configuration Called 1--->  " + configuration);
  }

  @Override
  public void close() {}
}
