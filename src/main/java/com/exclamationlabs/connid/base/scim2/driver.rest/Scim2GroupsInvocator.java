package com.exclamationlabs.connid.base.scim2.driver.rest;

import com.exclamationlabs.connid.base.connector.driver.DriverInvocator;
import com.exclamationlabs.connid.base.connector.driver.rest.RestRequest;
import com.exclamationlabs.connid.base.connector.driver.rest.RestResponseData;
import com.exclamationlabs.connid.base.connector.logging.Logger;
import com.exclamationlabs.connid.base.connector.results.ResultsFilter;
import com.exclamationlabs.connid.base.connector.results.ResultsPaginator;
import com.exclamationlabs.connid.base.scim2.configuration.Scim2Configuration;
import com.exclamationlabs.connid.base.scim2.model.Scim2Group;

import java.net.URLEncoder;
import java.util.*;

import com.exclamationlabs.connid.base.scim2.model.Scim2Operation;
import com.exclamationlabs.connid.base.scim2.model.Scim2PatchOp;
import com.exclamationlabs.connid.base.scim2.model.Scim2User;
import com.exclamationlabs.connid.base.scim2.model.response.ListGroupResponse;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.exceptions.ConnectorException;

public class Scim2GroupsInvocator implements DriverInvocator<Scim2Driver, Scim2Group> {
  private static final Log LOG = Log.getLog(Scim2GroupsInvocator.class);

  @Override
  public String create(Scim2Driver driver, Scim2Group model) throws ConnectorException {
    String id = null;
    Scim2Configuration config = driver.getConfiguration();
    if ( config.getEnableSlackSchema()
            || config.getEnableStandardSchema()
            || config.getEnableAWSSchema() )
    {
      RestRequest request =
              new RestRequest.Builder<>(Scim2Group.class)
                      .withPost()
                      .withRequestUri(driver.getConfiguration().getGroupsEndpointUrl())
                      .withRequestBody(model)
                      .build();

      RestResponseData<Scim2Group> data = driver.executeRequest(request);
      Scim2Group group = data.getResponseObject();
      if ( group!= null) {
        id = group.getId();
      }
    }
    else if (config.getEnableDynamicSchema())
    {
      ;
    }
    return id;
  }

  @Override
  public void delete(Scim2Driver driver, String groupId) throws ConnectorException {
    // Delete is usually delete
    Scim2Configuration config = driver.getConfiguration();
    RestRequest req = new RestRequest.Builder<>(Void.class)
            .withDelete()
            .withRequestUri(config.getGroupsEndpointUrl() + "/" +groupId)
            .build();
    RestResponseData<Void> data = driver.executeRequest(req);
  }

  @Override
  public Set<Scim2Group> getAll(Scim2Driver driver, ResultsFilter filter, ResultsPaginator paginator, Integer resultCap) throws ConnectorException
  {
    List<Scim2Group> groupList = new ArrayList<>();
    Scim2Configuration config = driver.getConfiguration();
    String filterParameter = Scim2UsersInvocator.getFilterParameter(filter);
    String pagingParameter = Scim2UsersInvocator.getPagingParameter(paginator);
    String query = "";
    if ( pagingParameter != null && filterParameter != null )
    {
      query = "?" + pagingParameter + "&" +  filterParameter ;
    }
    else if (pagingParameter != null )
    {
      query = "?" + pagingParameter;
    }
    else if (filterParameter != null )
    {
      query = "?" + filterParameter;
    }
    RestRequest request =
            new RestRequest.Builder<>(ListGroupResponse.class)
                    .withGet()
                    .withRequestUri(config.getGroupsEndpointUrl() + query )
                    .build();
    RestResponseData<ListGroupResponse> data = driver.executeRequest(request);
    ListGroupResponse response = data.getResponseObject();
    if ( response != null && data.getResponseStatusCode() == 200 )
    {
      groupList = response.getResources();
      if ( groupList != null && groupList.size() > 0 )
      {
        Scim2UsersInvocator.updatePaginator(paginator, groupList.size(), response.getTotalResults(), response.getItemsPerPage());
      }
      else {
        paginator.setNoMoreResults(true);
      }
    }
    else {
      paginator.setNoMoreResults(true);
    }
    Set<Scim2Group> groupSet = new HashSet<>(groupList);
    return groupSet;
  }

  @Override
  public Set<Scim2Group> getAll(Scim2Driver driver, ResultsFilter filter, ResultsPaginator paginator, Integer resultCap, Map<String, Object> prefetchDataMap) throws ConnectorException
  {
    return getAll(driver, filter, paginator, resultCap);
  }

  @Override
  public Scim2Group getOne(Scim2Driver driver, String objectId, Map<String, Object> prefetchDataMap)
      throws ConnectorException {
    Scim2Group group = null;
    Scim2Configuration config = driver.getConfiguration();
    if (config.getEnableStandardSchema()
            || config.getEnableAWSSchema()
            || config.getEnableSlackSchema())
    {
      RestRequest req =
              new RestRequest.Builder<>(Scim2Group.class)
                      .withGet()
                      .withRequestUri(config.getGroupsEndpointUrl() + "/" + objectId)
                      .build();
      RestResponseData<Scim2Group> response = driver.executeRequest(req);
      if (response.getResponseStatusCode() == 200) {
        group = response.getResponseObject();
      }
    }
    else if (config.getEnableDynamicSchema())
    {
      // Assume dynamic is the same for now since a Group member is a map
      RestRequest req =
              new RestRequest.Builder<>(Scim2Group.class)
                      .withGet()
                      .withRequestUri(config.getGroupsEndpointUrl() + "/" + objectId)
                      .build();
      RestResponseData<Scim2Group> response = driver.executeRequest(req);
      if (response.getResponseStatusCode() == 200) {
        group = response.getResponseObject();
      }
    }
    return group;
  }

  @Override
  public Scim2Group getOneByName(Scim2Driver driver, String objectName) throws ConnectorException
  {
    Scim2Group group = null;
    Scim2Configuration config = driver.getConfiguration();
    String displayName = URLEncoder.encode(objectName);
    String query = "?filter=displayName+eq+%22" + displayName + "%22";
    RestRequest request =
            new RestRequest.Builder<>(ListGroupResponse.class)
                    .withGet()
                    .withRequestUri(config.getGroupsEndpointUrl() + query )
                    .build();
    RestResponseData<ListGroupResponse> data = driver.executeRequest(request);

    if (data.getResponseStatusCode() == 200 && data.getResponseObject() != null )
    {
      ListGroupResponse response = data.getResponseObject();
      List<Scim2Group> list = response.getResources();
      if (list != null && list.size() > 0)
      {
        group = list.get(0);
      }
    }
    return group;
  }

  @Override
  public Scim2Group getOneByName(Scim2Driver driver, String objectName, Map<String, Object> prefetchDataMap) throws ConnectorException
  {
    return getOneByName(driver, objectName);
  }

  @Override
  public void update(Scim2Driver driver, String groupId, Scim2Group model)
      throws ConnectorException {
    Scim2Configuration config = driver.getConfiguration();
    if ( model.getDisplayName() != null && model.getDisplayName().length() > 0 ) {
      RestRequest req = new RestRequest.Builder<>(Scim2User.class)
              .withPut()
              .withRequestUri(config.getGroupsEndpointUrl() + "/" + groupId)
              .withRequestBody(model)
              .build();
      RestResponseData<Scim2User> response = driver.executeRequest(req);
      Scim2User updated = response.getResponseObject();
    }
    updateMembers(driver, groupId, model.getAddMembers(), model.getRemoveMembers());
  }

  public void updateMembers(Scim2Driver driver, String groupId,
                            Set<Map<String, String>> add,
                            Set<Map<String, String>> remove)
  {
    boolean hasWork = false;
    String url = driver.getConfiguration().getGroupsEndpointUrl() + "/" + groupId;
    Scim2PatchOp patchOp = new Scim2PatchOp();
    patchOp.setOperations(new ArrayList<>());

    if (remove != null && !remove.isEmpty())
    {
      for (Map<String, String>item: new ArrayList<>(remove))
      {
        Scim2Operation op = new Scim2Operation();
        op.setOperation("remove");
        String path = String.format("members[value eq \"%s\"]", item.get("value"));
        op.setPath(path);
        patchOp.getOperations().add(op);
      }
      hasWork = true;
    }

    if ( add != null && !add.isEmpty() ) {
      Scim2Operation addOperation = new Scim2Operation();
      addOperation.setOperation("add");
      addOperation.setPath("members");
      addOperation.setValue(new ArrayList<>(add));
      patchOp.getOperations().add(addOperation);
      hasWork = true;
    }

    if ( hasWork )
    {
      RestRequest request =
              new RestRequest.Builder<>(Scim2Group.class)
                      .withPatch()
                      .withRequestUri(url)
                      .withRequestBody(patchOp)
                      .build();
      RestResponseData<Scim2Group> data = driver.executeRequest(request);
      if (data.getResponseStatusCode() != 200 && data.getResponseStatusCode() != 204)
      {
        LOG.warn(String.format("SCIM2 Update Group Members returned HTTP status %s", Integer.toString(data.getResponseStatusCode())));
      }
    }
  }
}
