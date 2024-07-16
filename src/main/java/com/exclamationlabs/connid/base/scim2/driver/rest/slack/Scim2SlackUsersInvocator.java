package com.exclamationlabs.connid.base.scim2.driver.rest.slack;

import com.exclamationlabs.connid.base.connector.driver.DriverInvocator;
import com.exclamationlabs.connid.base.connector.driver.rest.RestRequest;
import com.exclamationlabs.connid.base.connector.driver.rest.RestResponseData;
import com.exclamationlabs.connid.base.connector.results.ResultsFilter;
import com.exclamationlabs.connid.base.connector.results.ResultsPaginator;
import com.exclamationlabs.connid.base.scim2.driver.rest.Scim2Driver;
import com.exclamationlabs.connid.base.scim2.model.Scim2User;
import com.exclamationlabs.connid.base.scim2.model.UserCreationType;
import com.exclamationlabs.connid.base.scim2.model.request.UserCreationRequest;
import com.exclamationlabs.connid.base.scim2.model.response.ListUsersResponse;
import com.exclamationlabs.connid.base.scim2.model.response.Scim2UserCreateResponse;
import com.exclamationlabs.connid.base.scim2.model.slack.Scim2SlackUser;

import java.util.*;

import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.exceptions.ConnectorException;

public class Scim2SlackUsersInvocator implements DriverInvocator<Scim2Driver, Scim2SlackUser> {

  private static final Log LOG = Log.getLog(Scim2SlackUsersInvocator.class);


  @Override
  public String create(Scim2Driver driver, Scim2SlackUser scim2SlackUser) throws ConnectorException {

    Scim2User user = null;
    String id = null;


    UserCreationRequest requestData =
            new UserCreationRequest(scim2SlackUser);

    RestRequest request =
            new RestRequest.Builder<>(Scim2UserCreateResponse.class)
                    .withPost()
                    .withRequestUri("/Users")
                    .withRequestBody(scim2SlackUser)
                    .build();

    System.out.println("Payload ---> "+request);

    RestResponseData<Scim2UserCreateResponse> data = driver.executeRequest(request);
    Scim2UserCreateResponse response = data.getResponseObject();

    return "";
  }

  @Override
  public void update(Scim2Driver driver, String userId, Scim2SlackUser userModel)
      throws ConnectorException {}

  @Override
  public void delete(Scim2Driver driver, String userId) throws ConnectorException {}

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

    Set<Scim2User> users = null;
    Set<Scim2SlackUser> slackUsers = new HashSet<>();
    boolean getAll = false;
    String additionalQueryString = "?status=" + status;
    if (paginator != null && paginator.hasPagination()) {
      additionalQueryString = additionalQueryString + "&page_size=" + paginator.getPageSize();
      if (paginator.getCurrentPageNumber() == null || paginator.getCurrentPageNumber() <= 0) {
        paginator.setCurrentPageNumber(1);
      }
      additionalQueryString =
              additionalQueryString + "&page_number=" + paginator.getCurrentPageNumber();
    } else {
      getAll = true;
    }
    RestRequest request =
            new RestRequest.Builder<>(ListUsersResponse.class)
                    .withGet()
                    .withRequestUri("/Users" + additionalQueryString)
                    .build();
    RestResponseData<ListUsersResponse> data = scim2Driver.executeRequest(request);
    ListUsersResponse response = data.getResponseObject();

    if (response != null) {
      users = response.getResources();
      paginator.setTotalResults(response.getTotalResults());
      paginator.setNumberOfProcessedPages(response.getPageNumber());
      paginator.setNumberOfTotalPages(response.getStartIndex());
      paginator.setPageSize(response.getItemsPerPage());
      if (response.getResources() != null && response.getResources().size() > 0) {
        if (paginator.getNumberOfProcessedResults() == null) {
          paginator.setNumberOfProcessedResults(0);
        }
        paginator.setNumberOfProcessedResults(
                paginator.getNumberOfProcessedResults() + response.getResources().size());
      }

      while (getAll && response.getPageNumber() < response.getTotalResults()) {
        Integer pageNumber = response.getPageNumber() + 1;
        additionalQueryString =
                "?status="
                        + status
                        + "&page_size="
                        + response.getPageNumber()
                        + "&page_number="
                        + pageNumber;
        request =
                new RestRequest.Builder<>(ListUsersResponse.class)
                        .withGet()
                        .withRequestUri("/Users" + additionalQueryString)
                        .build();
        data = scim2Driver.executeRequest(request);
        response = data.getResponseObject();
        if (response != null) {
          paginator.setTotalResults(response.getTotalResults());
          paginator.setNumberOfProcessedPages(response.getPageNumber());
          paginator.setNumberOfTotalPages(response.getPageNumber());
          paginator.setPageSize(response.getItemsPerPage());
          if (response.getResources() != null && !response.getResources().isEmpty()) {
            paginator.setNumberOfProcessedResults(
                    paginator.getNumberOfProcessedResults() + response.getResources().size());
            users = response.getResources();

           // slackUsers.addAll((Collection<? extends Scim2SlackUser>) response.getResources());
          }
        }
      }
    }
    return slackUsers;
  }

  @Override
  public Map<String, Object> getPrefetch(Scim2Driver driver) {
    return new LinkedHashMap<>();
  }
  @Override
  public Set<Scim2SlackUser> getAll(
          Scim2Driver scim2Driver, ResultsFilter filter, ResultsPaginator paginator, Integer forceNumber)
          throws ConnectorException {
    String status = null;
    Set<Scim2SlackUser> allUsers = null;
    Set<Scim2SlackUser> inactiveUsers = null;
    Set<Scim2SlackUser> activeUsers = null;

    if(scim2Driver.getConfiguration().getEnableSlackSchema()){
      allUsers = getUsersByStatus(scim2Driver,filter.getValue(),paginator);
    }else if(scim2Driver.getConfiguration().getEnableAWSSchema()) {
      //AWS Invocator
    }

    return allUsers;

  }
}
