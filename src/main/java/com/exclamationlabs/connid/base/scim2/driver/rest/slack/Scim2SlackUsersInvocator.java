package com.exclamationlabs.connid.base.scim2.driver.rest.slack;

import com.exclamationlabs.connid.base.connector.driver.DriverInvocator;
import com.exclamationlabs.connid.base.connector.driver.rest.RestRequest;
import com.exclamationlabs.connid.base.connector.driver.rest.RestResponseData;
import com.exclamationlabs.connid.base.connector.results.ResultsFilter;
import com.exclamationlabs.connid.base.connector.results.ResultsPaginator;
import com.exclamationlabs.connid.base.scim2.driver.rest.Scim2Driver;
import com.exclamationlabs.connid.base.scim2.model.Scim2User;
import com.exclamationlabs.connid.base.scim2.model.request.UserCreationRequest;
import com.exclamationlabs.connid.base.scim2.model.response.ListSlackUsersResponse;
import com.exclamationlabs.connid.base.scim2.model.response.ListUsersResponse;
import com.exclamationlabs.connid.base.scim2.model.response.Scim2UserCreateResponse;
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
    driver.getConfiguration().setCurrentToken(driver.getConfiguration().getToken());
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

  public Set<Scim2SlackUser> getUsersByStatus(
      Scim2Driver scim2Driver, String status, ResultsPaginator paginator) {

    List<Scim2SlackUser> slackUsers = new ArrayList<>();
    String additionalQueryString = "";
    RestRequest request =
        new RestRequest.Builder<>(ListSlackUsersResponse.class)
            .withGet()
            .withRequestUri("/Users")
            .build();
    RestResponseData<ListSlackUsersResponse> data = scim2Driver.executeRequest(request);
    ListSlackUsersResponse response = data.getResponseObject();

    if (response != null) {
      // slackUsers = data.getResponseObject().getResources();
      // LOG.error("Slack Response - DSR " + response);
      slackUsers = response.getResources();
      paginator.setTotalResults(response.getTotalResults().intValue());
      //  paginator.setNumberOfProcessedPages(response.getStartIndex().intValue());
      paginator.setNumberOfTotalPages(
          response.getTotalResults().intValue() / response.getItemsPerPage().intValue());
      paginator.setPageSize(response.getItemsPerPage().intValue());
      if (data.getResponseObject().getResources() != null
          && data.getResponseObject().getResources().size() > 0) {
        if (paginator.getNumberOfProcessedResults() == null) {
          paginator.setNumberOfProcessedResults(0);
        }
        paginator.setNumberOfProcessedResults(
            paginator.getNumberOfProcessedResults()
                + data.getResponseObject().getResources().size());
      }

      while (true) {
        assert response != null;
        if (!(response.getStartIndex() + response.getItemsPerPage() < response.getTotalResults()))
          break;
        // Integer pageNumber = response.getPageNumber() + 1;
        int startIndex = response.getStartIndex() + response.getItemsPerPage();

        additionalQueryString =
            "?startIndex=" + startIndex + "&itemsPerPage=" + response.getItemsPerPage();
        request =
            new RestRequest.Builder<>(ListSlackUsersResponse.class)
                .withGet()
                .withRequestUri("/Users" + additionalQueryString)
                .build();
        RestResponseData<ListSlackUsersResponse> data1 = scim2Driver.executeRequest(request);
        response = data1.getResponseObject();
        if (response != null) {
          paginator.setTotalResults(response.getTotalResults().intValue());
          // paginator.setNumberOfProcessedPages(response.getPageNumber());
          paginator.setNumberOfTotalPages(response.getTotalResults().intValue());
          paginator.setPageSize(response.getItemsPerPage());
          if (data1.getResponseObject().getResources() != null
              && !data1.getResponseObject().getResources().isEmpty()) {
            paginator.setNumberOfProcessedResults(
                paginator.getNumberOfProcessedResults()
                    + data1.getResponseObject().getResources().size());
            // slackUsers = response.getResources();

            slackUsers.addAll(data1.getResponseObject().getResources());
          }
        }
      }
    }

    List<Scim2SlackUser> dataList = new ArrayList<>(slackUsers);

    // Get the first 0-9 elements (total 10 elements)
    List<Scim2SlackUser> subList = dataList.subList(10, Math.min(14, dataList.size()));

    // Convert the sublist back to a Set
    Set<Scim2SlackUser> subSet = new LinkedHashSet<>(subList);

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
    allUsers = getUsersByStatus(driver, filter.getValue(), paginator);
    return allUsers;
  }
}
