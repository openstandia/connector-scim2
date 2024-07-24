package com.exclamationlabs.connid.base.scim2.driver.rest;

import com.exclamationlabs.connid.base.connector.driver.Driver;
import com.exclamationlabs.connid.base.connector.driver.DriverInvocator;
import com.exclamationlabs.connid.base.connector.driver.rest.RestRequest;
import com.exclamationlabs.connid.base.connector.driver.rest.RestResponseData;
import com.exclamationlabs.connid.base.connector.model.IdentityModel;
import com.exclamationlabs.connid.base.connector.results.ResultsFilter;
import com.exclamationlabs.connid.base.connector.results.ResultsPaginator;
import com.exclamationlabs.connid.base.scim2.driver.rest.slack.Scim2SlackGroupInvocator;
import com.exclamationlabs.connid.base.scim2.driver.rest.slack.Scim2SlackUsersInvocator;
import com.exclamationlabs.connid.base.scim2.model.Scim2Emails;
import com.exclamationlabs.connid.base.scim2.model.Scim2Name;
import com.exclamationlabs.connid.base.scim2.model.Scim2User;

import java.util.*;

import com.exclamationlabs.connid.base.scim2.model.response.ListUsersResponse;
import com.exclamationlabs.connid.base.scim2.model.slack.Scim2SlackGroup;
import com.exclamationlabs.connid.base.scim2.model.slack.Scim2SlackUser;
import org.identityconnectors.framework.common.exceptions.ConnectorException;

public class Scim2UsersInvocator implements DriverInvocator<Scim2Driver, Scim2User> {



  @Override
  public String create(Scim2Driver driver, Scim2User user1) throws ConnectorException {
    Scim2SlackUser user = new Scim2SlackUser();
    user.setUserName("local44.test44");
    Scim2Emails emails = new Scim2Emails();
    emails.setValue("sreeni.iam44@internet2.edu");
    emails.setPrimary(true);
    user.setEmails(Arrays.asList(emails));
    Scim2Name name = new Scim2Name();
    name.setFamilyName("local");
    name.setGivenName("test");
    user.setName(name);
    user.setSchemas(Arrays.asList("urn:ietf:params:scim:schemas:core:2.0:User"));
    //user.setSchemas("["urn:ietf:params:scim:schemas:core:2.0:User"]");
    if(driver.getConfiguration().getEnableSlackSchema()){
      new Scim2SlackUsersInvocator().create(driver, user);
    }else{
      //aws
    }
    return "";
  }

  @Override
  public void update(Scim2Driver driver, String userId, Scim2User userModel)
      throws ConnectorException {}

  @Override
  public void delete(Scim2Driver driver, String userId) throws ConnectorException {
    RestRequest req = null;
    Scim2User scim2User ;
   // Scim2User user = getOne(driver, userId, null);

    if(driver.getConfiguration().getEnableSlackSchema()){
    //  scim2User =  new Scim2SlackUsersInvocator().getOne(driver,userId,null);
      new Scim2SlackUsersInvocator().delete(driver,userId);

    }else if(driver.getConfiguration().getEnableAWSSchema()) {
      //AWS Invocator
    }


  }

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

  @Override
  //public <T extends Scim2User> Set<T> getAll(
  public Set<Scim2User> getAll(
          Scim2Driver scim2Driver, ResultsFilter filter, ResultsPaginator paginator, Integer forceNumber)
          throws ConnectorException {
    String status = null;
    Set<Scim2User> inactiveUsers = null;
    Set<Scim2User> activeUsers = null;
    Set<? extends Scim2User> allUsers;

    if(scim2Driver.getConfiguration().getEnableSlackSchema()){
      allUsers  =  new Scim2SlackUsersInvocator().getAll(scim2Driver,filter,paginator,forceNumber);
    }else if(scim2Driver.getConfiguration().getEnableAWSSchema()) {
      //AWS Invocator
    }

   // return new HashSet<>(allUsers);
    return null;

  }

  @Override
  public Map<String, Object> getPrefetch(Scim2Driver driver) {
    return new LinkedHashMap<>();
  }


}
