package com.exclamationlabs.connid.base.scim2.driver.rest;

import com.exclamationlabs.connid.base.connector.driver.DriverInvocator;
import com.exclamationlabs.connid.base.connector.driver.rest.RestRequest;
import com.exclamationlabs.connid.base.connector.driver.rest.RestResponseData;
import com.exclamationlabs.connid.base.connector.results.ResultsFilter;
import com.exclamationlabs.connid.base.connector.results.ResultsPaginator;
import com.exclamationlabs.connid.base.scim2.configuration.Scim2Configuration;
import com.exclamationlabs.connid.base.scim2.driver.rest.slack.Scim2SlackUsersInvocator;
import com.exclamationlabs.connid.base.scim2.model.Scim2User;
import com.exclamationlabs.connid.base.scim2.model.response.ListSlackUsersResponse;
import com.exclamationlabs.connid.base.scim2.model.response.ListUsersResponse;
import com.exclamationlabs.connid.base.scim2.model.slack.Scim2SlackUser;
import java.util.*;
import org.identityconnectors.framework.common.exceptions.ConnectorException;

public class Scim2UsersInvocator implements DriverInvocator<Scim2Driver, Scim2User> {

  @Override
  public String create(Scim2Driver driver, Scim2User user) throws ConnectorException {
    if (driver.getConfiguration().getEnableSlackSchema()) {
      new Scim2SlackUsersInvocator().create(driver, (Scim2SlackUser) user);
    } else {
      // aws
    }
    return "";
  }

  @Override
  public void update(Scim2Driver driver, String userId, Scim2User userModel)
      throws ConnectorException {
    Scim2Configuration config = driver.getConfiguration();
    if ( config.getEnableSlackSchema() ) {
      new Scim2SlackUsersInvocator().update(driver, userId, (Scim2SlackUser) userModel);
    }
    else if (config.getEnableStandardSchema() || config.getEnableAWSSchema())
    {
      ;
    }
    else if (config.getEnableDynamicSchema())
    {
      ;
    }
  }

  @Override
  public void delete(Scim2Driver driver, String userId) throws ConnectorException {
    RestRequest req = null;
    Scim2User scim2User;
    // Scim2User user = getOne(driver, userId, null);

    if (driver.getConfiguration().getEnableSlackSchema()) {
      //  scim2User =  new Scim2SlackUsersInvocator().getOne(driver,userId,null);
      new Scim2SlackUsersInvocator().delete(driver, userId);

    } else if (driver.getConfiguration().getEnableAWSSchema()) {
      // AWS Invocator
    }
  }

  @Override
  public Scim2User getOne(Scim2Driver driver, String objectId, Map<String, Object> prefetchDataMap)
      throws ConnectorException {
    Scim2User user = null;
    if (driver.getConfiguration().getEnableSlackSchema()) {
      user = new Scim2SlackUsersInvocator().getOne(driver, objectId, prefetchDataMap);
    } else if (driver.getConfiguration().getEnableAWSSchema()) {
      // AWS Invocator
    }
    return user;
  }

  @Override
  public Scim2User getOneByName(Scim2Driver driver, String name) throws ConnectorException
  {
    Scim2User user = null;
    Scim2Configuration config = driver.getConfiguration();
    if (config.getEnableSlackSchema()) {
      user = new Scim2SlackUsersInvocator().getOneByName(driver, name);
    }
    else if (config.getEnableAWSSchema() || config.getEnableStandardSchema())
    {
      String queryString ="?filter=userName%20eq%20%22"+name+"%22";
      RestRequest req = new RestRequest.Builder<>(ListUsersResponse.class)
              .withGet()
              .withRequestUri(config.getUsersEndpointUrl() + queryString)
              .build();
      RestResponseData<ListUsersResponse> response = driver.executeRequest(req);
      if (response.getResponseStatusCode() == 200) {
        List<Scim2User> list = response.getResponseObject().getResources();
        if ( list != null && list.size() > 0 ) {
          user = list.get(0);
        }
      }
    }
    else if (config.getEnableDynamicSchema())
    {
      // Do the dynamic lookup here. The difference is the type
      ;
    }
    return user;
  }
  @Override
  public Scim2User getOneByName(Scim2Driver driver, String name, Map<String, Object> prefetchDataMap){
    return getOneByName(driver, name);
  }
  @Override
  // public <T extends Scim2User> Set<T> getAll(
  public Set<Scim2User> getAll(
      Scim2Driver driver,
      ResultsFilter filter,
      ResultsPaginator paginator,
      Integer forceNumber)
      throws ConnectorException {
    String status = null;
    Set<? extends Scim2User> allUsers = null;
    Scim2Configuration config = driver.getConfiguration();

    if (config.getEnableSlackSchema()) {
      allUsers = new Scim2SlackUsersInvocator().getAll(driver, filter, paginator, forceNumber);
    } else if (config.getEnableAWSSchema() || config.getEnableStandardSchema()) {

    }
    else if (config.getEnableDynamicSchema())
    {
      // Dynamic Schema user LinkedTreeList
      ;
    }

    //   return new HashSet<>(allUsers);
    return (Set<Scim2User>) allUsers;
  }

  @Override
  public Map<String, Object> getPrefetch(Scim2Driver driver) {
    return new LinkedHashMap<>();
  }
}
