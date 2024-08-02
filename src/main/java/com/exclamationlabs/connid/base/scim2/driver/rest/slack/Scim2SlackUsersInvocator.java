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

    Scim2User user = null;
    String id = null;

    UserCreationRequest requestData = new UserCreationRequest(scim2SlackUser);

    RestRequest request =
        new RestRequest.Builder<>(Scim2UserCreateResponse.class)
            .withPost()
            .withRequestUri("/Users")
            .withRequestBody(scim2SlackUser)
            .build();

    System.out.println("Payload ---> " + request);

    RestResponseData<Scim2UserCreateResponse> data = driver.executeRequest(request);
    Scim2UserCreateResponse response = data.getResponseObject();

    return "";
  }

  @Override
  public void update(Scim2Driver driver, String userId, Scim2SlackUser userModel)
      throws ConnectorException {}

  @Override
  public void delete(Scim2Driver driver, String userId) throws ConnectorException {
    RestRequest req = null;

    req =
        new RestRequest.Builder<>(Void.class)
            .withDelete()
            .withRequestUri("/Users/" + userId)
            .build();
    driver.executeRequest(req);
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
            .withRequestUri("/Users/" + objectId)
            .build();
    RestResponseData<Scim2SlackUser> response = driver.executeRequest(req);
    if (response.getResponseStatusCode() == 200) {
      System.out.println(response.getResponseObject());
      user = response.getResponseObject();
      //  getPhoneInfo(driver, user);
    }

    return user;
  }

  public Set<Scim2SlackUser> getUsersByStatus(
      Scim2Driver scim2Driver, String status, ResultsPaginator paginator) {

    Set<Scim2SlackUser> slackUsers = new HashSet<>();
    String additionalQueryString = "";
    RestRequest request =
        new RestRequest.Builder<>(ListUsersResponse.class)
            .withGet()
            .withRequestUri("/Users")
            .build();
    RestResponseData<ListUsersResponse> data = scim2Driver.executeRequest(request);
    ListUsersResponse response = data.getResponseObject();

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
        RestResponseData<ListUsersResponse> data1 = scim2Driver.executeRequest(request);
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
  public Set<Scim2SlackUser> getAll(
      Scim2Driver scim2Driver,
      ResultsFilter filter,
      ResultsPaginator paginator,
      Integer forceNumber)
      throws ConnectorException {
    String status = null;
    Set<Scim2SlackUser> allUsers = null;
    Set<Scim2SlackUser> inactiveUsers = null;
    Set<Scim2SlackUser> activeUsers = null;

    if (scim2Driver.getConfiguration().getEnableSlackSchema()) {
      allUsers = getUsersByStatus(scim2Driver, filter.getValue(), paginator);
    } else if (scim2Driver.getConfiguration().getEnableAWSSchema()) {
      // AWS Invocator
    }

    return allUsers;
  }
}
