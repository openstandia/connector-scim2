package com.exclamationlabs.connid.base.scim2.driver.rest;

import com.exclamationlabs.connid.base.connector.driver.DriverInvocator;
import com.exclamationlabs.connid.base.connector.driver.rest.RestRequest;
import com.exclamationlabs.connid.base.connector.driver.rest.RestResponseData;
import com.exclamationlabs.connid.base.connector.filter.FilterType;
import com.exclamationlabs.connid.base.connector.results.ResultsFilter;
import com.exclamationlabs.connid.base.connector.results.ResultsPaginator;
import com.exclamationlabs.connid.base.scim2.configuration.Scim2Configuration;
import com.exclamationlabs.connid.base.scim2.driver.rest.slack.Scim2SlackUsersInvocator;
import com.exclamationlabs.connid.base.scim2.model.Scim2User;
import com.exclamationlabs.connid.base.scim2.model.response.ListUsersResponse;
import com.exclamationlabs.connid.base.scim2.model.slack.Scim2SlackUser;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.exceptions.ConnectorException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Scim2UsersInvocator implements DriverInvocator<Scim2Driver, Scim2User>
{
    private static final Log LOG = Log.getLog(Scim2UsersInvocator.class);
    @Override
    public String create(Scim2Driver driver, Scim2User user) throws ConnectorException
    {
        String id = null;
        Scim2Configuration config = driver.getConfiguration();
        if (config.getEnableSlackSchema())
        {
            id = new Scim2SlackUsersInvocator().create(driver, (Scim2SlackUser) user);
        }
        else if ( config.getEnableStandardSchema() || config.getEnableAWSSchema() )
        {
            RestRequest request =
                    new RestRequest.Builder<>(Scim2User.class)
                            .withPost()
                            .withRequestUri(driver.getConfiguration().getUsersEndpointUrl())
                            .withRequestBody(user)
                            .build();

            RestResponseData<Scim2User> data = driver.executeRequest(request);
            Scim2User theUser = data.getResponseObject();
            if ( theUser != null) {
                id = theUser.getId();
            }
        }
        else if (config.getEnableDynamicSchema())
        {
            ;
        }
        return id;
    }

    @Override
    public void delete(Scim2Driver driver, String userId) throws ConnectorException
    {
        Scim2User scim2User;
        Scim2Configuration config = driver.getConfiguration();

        if (config.getEnableSlackSchema())
        {
            new Scim2SlackUsersInvocator().delete(driver, userId);
        }
        else if (config.getEnableAWSSchema()
                || config.getEnableStandardSchema()
                || config.getEnableDynamicSchema())
        {
            // Delete is usually delete 
            RestRequest req = new RestRequest.Builder<>(Void.class)
                            .withDelete()
                            .withRequestUri(config.getUsersEndpointUrl() + "/" +userId)
                            .build();
            RestResponseData<Void> data = driver.executeRequest(req);
        }
    }

    @Override
    // public <T extends Scim2User> Set<T> getAll(
    public Set<Scim2User> getAll(Scim2Driver driver, ResultsFilter filter, ResultsPaginator paginator, Integer forceNumber)
            throws ConnectorException
    {
        Set<? extends Scim2User> allUsers = null;
        Scim2Configuration config = driver.getConfiguration();

        if (config.getEnableSlackSchema())
        {
            allUsers = new Scim2SlackUsersInvocator().getAll(driver, filter, paginator, forceNumber);
        }
        else if (config.getEnableAWSSchema() || config.getEnableStandardSchema())
        {

        }
        else if (config.getEnableDynamicSchema())
        {
            // Dynamic Schema user LinkedTreeList
            ;
        }
        return (Set<Scim2User>) allUsers;
    }

    /**
     * @param filter
     * @return a SCIM2 filter or null when filter is not specified
     */
    public static String getFilterParameter(ResultsFilter filter )
    {
        String parameter = null;
        if ( filter != null && filter.hasFilter())
        {
            String attribute = filter.getAttribute();
            String value = filter.getValue();
            if ( filter.getFilterType().equals(FilterType.EqualsFilter))
            {
                parameter = "filter=" + attribute +"%20eq%20%22"+value+"%22";
            }
        }
        return parameter;
    }

    /**
     * @param paginator
     * @return SCIM2 Pagination parameters or null when pagination is not specified
     */
    public static String getPagingParameter(ResultsPaginator paginator)
    {
        String parameter = null;
        if ( paginator != null )
        {
            if ( paginator.hasPagination() )
            {
                Integer count = paginator.getPageSize();
                parameter = "count=" + count;
            }
            if ( paginator.getCurrentOffset() != null ) {
                Integer startIndex = paginator.getCurrentOffset();
                String start = "startIndex=" + startIndex;
                parameter = (parameter == null) ? start : parameter + "&" + start  ;
            }
            else if ( paginator.getCurrentPageNumber() != null )
            {
                Integer startIndex = ((paginator.getCurrentPageNumber()-1) * paginator.getPageSize()) + 1;
                String start = "startIndex=" + startIndex;
                parameter = (parameter == null) ? start : parameter + "&" + start  ;
            }
        }
        else {
            parameter = "";
        }
        return parameter;
    }
    public static void updatePaginator(ResultsPaginator paginator, int totalReturned, int totalResults, int pageSize){
        paginator.setTotalResults(totalResults);
        if (pageSize < paginator.getPageSize())
        {
            // override the number of items returned in the page
            pageSize = paginator.getPageSize();
        }
        Integer pages = totalResults / pageSize;
        if ( (totalResults % pageSize) > 0 )
        {
            pages++;
        }
        paginator.setNumberOfTotalPages( pages);
        if (paginator.getNumberOfProcessedResults() == null) {
            paginator.setNumberOfProcessedResults(0);
        }
        paginator.setNumberOfProcessedResults(paginator.getNumberOfProcessedResults() + totalReturned);
        if ( paginator.getNumberOfProcessedPages() == null )
        {
            paginator.setNumberOfProcessedPages(0);
        }
        paginator.setNumberOfProcessedPages(paginator.getNumberOfProcessedPages()+1);
        if ( paginator.getTotalResults() == paginator.getNumberOfProcessedResults() ) {
            paginator.setNoMoreResults(true);
        }
        if ( totalReturned == 0 )
        {
            paginator.setNoMoreResults(true);
        }
    }

    @Override
    public Scim2User getOne(Scim2Driver driver, String objectId, Map<String, Object> prefetchDataMap)
            throws ConnectorException
    {
        Scim2User user = null;
        Scim2Configuration config = driver.getConfiguration();
        if (config.getEnableSlackSchema())
        {
            user = new Scim2SlackUsersInvocator().getOne(driver, objectId, prefetchDataMap);
        }
        else if (config.getEnableStandardSchema() || config.getEnableAWSSchema())
        {
            RestRequest req =
                    new RestRequest.Builder<>(Scim2User.class)
                            .withGet()
                            .withRequestUri(driver.getConfiguration().getUsersEndpointUrl() + "/" + objectId)
                            .build();
            RestResponseData<Scim2SlackUser> response = driver.executeRequest(req);
            if (response.getResponseStatusCode() == 200) {
                user = response.getResponseObject();
            }
        }
        else if (config.getEnableDynamicSchema())
        {
            ;
        }
        return user;
    }

    @Override
    public Scim2User getOneByName(Scim2Driver driver, String name, Map<String, Object> prefetchDataMap)
    {
        return getOneByName(driver, name);
    }

    @Override
    public Scim2User getOneByName(Scim2Driver driver, String name) throws ConnectorException
    {
        Scim2User user = null;
        Scim2Configuration config = driver.getConfiguration();
        if (config.getEnableSlackSchema())
        {
            user = new Scim2SlackUsersInvocator().getOneByName(driver, name);
        }
        else if (config.getEnableAWSSchema() || config.getEnableStandardSchema())
        {
            String queryString = "?filter=userName%20eq%20%22" + name + "%22";
            RestRequest req = new RestRequest.Builder<>(ListUsersResponse.class)
                    .withGet()
                    .withRequestUri(config.getUsersEndpointUrl() + queryString)
                    .build();
            RestResponseData<ListUsersResponse> response = driver.executeRequest(req);
            if (response.getResponseStatusCode() == 200)
            {
                List<Scim2User> list = response.getResponseObject().getResources();
                if (list != null && list.size() > 0)
                {
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
    public Map<String, Object> getPrefetch(Scim2Driver driver)
    {
        return new LinkedHashMap<>();
    }

    @Override
    public void update(Scim2Driver driver, String userId, Scim2User user)
            throws ConnectorException
    {
        Scim2Configuration config = driver.getConfiguration();
        if (config.getEnableSlackSchema())
        {
            new Scim2SlackUsersInvocator().update(driver, userId, (Scim2SlackUser) user);
        }
        else if (config.getEnableStandardSchema() || config.getEnableAWSSchema())
        {
            RestRequest req = new RestRequest.Builder<>(Scim2User.class)
                    .withPut()
                    .withRequestUri(config.getUsersEndpointUrl() + "/" + userId)
                    .withRequestBody(user)
                    .build();
            RestResponseData<Scim2User> response = driver.executeRequest(req);
            Scim2User updated = response.getResponseObject();
        }
        else if (config.getEnableDynamicSchema())
        {
            ;
        }
    }
}
