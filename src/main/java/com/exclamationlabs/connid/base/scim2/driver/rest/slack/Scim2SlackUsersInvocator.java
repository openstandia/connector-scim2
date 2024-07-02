package com.exclamationlabs.connid.base.scim2.driver.rest.slack;

import com.exclamationlabs.connid.base.connector.driver.DriverInvocator;
import com.exclamationlabs.connid.base.scim2.driver.rest.Scim2Driver;
import com.exclamationlabs.connid.base.scim2.model.slack.Scim2SlackUser;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.exceptions.ConnectorException;

import java.util.Map;

public class Scim2SlackUsersInvocator implements DriverInvocator<Scim2Driver,Scim2SlackUser> {

    private static final Log LOG = Log.getLog(Scim2SlackUsersInvocator.class);
    @Override
    public String create(Scim2Driver driver, Scim2SlackUser model) throws ConnectorException {
        return null;
    }

    @Override
    public void update(Scim2Driver driver, String userId, Scim2SlackUser userModel) throws ConnectorException {

    }

    @Override
    public void delete(Scim2Driver driver, String userId) throws ConnectorException {

    }

    @Override
    public Scim2SlackUser getOne(Scim2Driver driver, String objectId, Map<String, Object> prefetchDataMap) throws ConnectorException {
        return null;
    }
}
