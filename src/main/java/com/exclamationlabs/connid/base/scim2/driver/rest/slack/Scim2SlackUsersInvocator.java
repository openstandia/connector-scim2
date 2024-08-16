package com.exclamationlabs.connid.base.scim2.driver.rest.slack;

import com.exclamationlabs.connid.base.connector.driver.DriverInvocator;
import com.exclamationlabs.connid.base.connector.driver.rest.RestRequest;
import com.exclamationlabs.connid.base.connector.driver.rest.RestResponseData;
import com.exclamationlabs.connid.base.connector.results.ResultsFilter;
import com.exclamationlabs.connid.base.connector.results.ResultsPaginator;
import com.exclamationlabs.connid.base.scim2.configuration.Scim2Configuration;
import com.exclamationlabs.connid.base.scim2.driver.rest.Scim2Driver;
import com.exclamationlabs.connid.base.scim2.driver.rest.Scim2UsersInvocator;
import com.exclamationlabs.connid.base.scim2.model.response.ListSlackUsersResponse;
import com.exclamationlabs.connid.base.scim2.model.slack.Scim2SlackUser;
import java.util.*;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.exceptions.ConnectorException;

public class Scim2SlackUsersInvocator implements DriverInvocator<Scim2Driver, Scim2SlackUser> {

  private static final Log LOG = Log.getLog(Scim2SlackUsersInvocator.class);

  @Override
  public String create(Scim2Driver driver, Scim2SlackUser scim2SlackUser)
      throws ConnectorException {

    String id = null;

    RestRequest request =
        new RestRequest.Builder<>(Scim2SlackUser.class)
            .withPost()
            .withRequestUri(driver.getConfiguration().getUsersEndpointUrl())
            .withRequestBody(scim2SlackUser)
            .build();

    RestResponseData<Scim2SlackUser> data = driver.executeRequest(request);
    Scim2SlackUser user = data.getResponseObject();
    if (user != null) {
      id = user.getId();
    }
    return id;
  }

  @Override
  public void update(Scim2Driver driver, String userId, Scim2SlackUser userModel)
      throws ConnectorException {

    RestRequest req = new RestRequest.Builder<>(Scim2SlackUser.class)
            .withPut()
            .withRequestUri(driver.getConfiguration().getUsersEndpointUrl() + "/" + userId)
            .withRequestBody(userModel)
            .build();
    RestResponseData<Scim2SlackUser> response = driver.executeRequest(req);
    Scim2SlackUser updated = response.getResponseObject();
  }

  @Override
  public void delete(Scim2Driver driver, String userId) throws ConnectorException {
    RestRequest req = null;

    req =
        new RestRequest.Builder<>(Void.class)
            .withDelete()
            .withRequestUri(driver.getConfiguration().getUsersEndpointUrl() + "/" +userId)
            .build();
    RestResponseData<Void> data = driver.executeRequest(req);
    return;
  }

  @Override
  public Scim2SlackUser getOne(
      Scim2Driver driver, String objectId, Map<String, Object> prefetchDataMap)
      throws ConnectorException {
    Scim2SlackUser user = null;

    RestRequest req =
        new RestRequest.Builder<>(Scim2SlackUser.class)
            .withGet()
            .withRequestUri(driver.getConfiguration().getUsersEndpointUrl() + "/" + objectId)
            .build();
    RestResponseData<Scim2SlackUser> response = driver.executeRequest(req);
    if (response.getResponseStatusCode() == 200) {
      user = response.getResponseObject();
    }

    return user;
  }

  public Scim2SlackUser getOneByName(Scim2Driver driver, String name) {
    Scim2SlackUser user = null;
    String queryString ="?filter=userName%20eq%20%22"+name+"%22";
    RestRequest req = new RestRequest.Builder<>(ListSlackUsersResponse.class)
                    .withGet()
                    .withRequestUri(driver.getConfiguration().getUsersEndpointUrl() + queryString)
                    .build();
    RestResponseData<ListSlackUsersResponse> response = driver.executeRequest(req);
    if (response.getResponseStatusCode() == 200) {
      List<Scim2SlackUser> list = response.getResponseObject().getResources();
      if ( list != null && list.size() > 0 ) {
        user = list.get(0);
      }
    }
    return user;
  }

  public Scim2SlackUser getOneByName(Scim2Driver driver, String userName, Map<String, Object> prefetchDataMap){
    return getOneByName(driver, userName);
  }

  public Set<Scim2SlackUser> getUsersList( Scim2Driver driver, ResultsFilter filter, ResultsPaginator paginator) {
    Scim2Configuration config = driver.getConfiguration();
    List<Scim2SlackUser> userList = new ArrayList<>();
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
        new RestRequest.Builder<>(ListSlackUsersResponse.class)
            .withGet()
            .withRequestUri(config.getUsersEndpointUrl() + query )
            .build();
    RestResponseData<ListSlackUsersResponse> data = driver.executeRequest(request);
    ListSlackUsersResponse response = data.getResponseObject();

    if ( response != null && data.getResponseStatusCode() == 200 ) {
      userList = response.getResources();
      if (userList != null && userList.size() > 0) {
        paginator.setTotalResults(response.getTotalResults());
        Integer pages = response.getTotalResults() / response.getItemsPerPage();
        if (response.getTotalResults() % response.getItemsPerPage() > 0 )
        {
          pages++;
        }
        paginator.setNumberOfTotalPages( pages);
        // paginator.setPageSize(response.getItemsPerPage());
        if (paginator.getNumberOfProcessedResults() == null) {
          paginator.setNumberOfProcessedResults(0);
        }
        paginator.setNumberOfProcessedResults(paginator.getNumberOfProcessedResults() + userList.size());
        if ( paginator.getNumberOfProcessedPages() == null )
        {
          paginator.setNumberOfProcessedPages(0);
        }
        paginator.setNumberOfProcessedPages(paginator.getNumberOfProcessedPages()+1);
        if ( paginator.getTotalResults() == paginator.getNumberOfProcessedResults() ) {
          paginator.setNoMoreResults(true);
        }
      }
      else {
        paginator.setNoMoreResults(true);
      }
    }
    // Convert the list to a Set
    Set<Scim2SlackUser> subSet = new HashSet<>(userList);

    return subSet;
  }

  @Override
  public Map<String, Object> getPrefetch(Scim2Driver driver) {
    return new LinkedHashMap<>();
  }

  @Override
  public Set<Scim2SlackUser> getAll(Scim2Driver driver, ResultsFilter filter, ResultsPaginator paginator,Integer forceNumber)
      throws ConnectorException {
    Set<Scim2SlackUser> allUsers = null;
    allUsers = getUsersList(driver, filter, paginator);
    return allUsers;
  }
}
