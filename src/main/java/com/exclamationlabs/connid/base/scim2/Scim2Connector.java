package com.exclamationlabs.connid.base.scim2;

import com.exclamationlabs.connid.base.connector.BaseFullAccessConnector;
import com.exclamationlabs.connid.base.connector.authenticator.Authenticator;
import com.exclamationlabs.connid.base.connector.authenticator.DirectAccessTokenAuthenticator;
import com.exclamationlabs.connid.base.scim2.adapter.Scim2GroupsAdapter;
import com.exclamationlabs.connid.base.scim2.adapter.Scim2UserAdapter;
import com.exclamationlabs.connid.base.scim2.configuration.Scim2Configuration;
import com.exclamationlabs.connid.base.scim2.driver.rest.Scim2Driver;
import org.identityconnectors.framework.spi.ConnectorClass;

@ConnectorClass(
        displayNameKey = "scim2.connector.display",
        configurationClass = Scim2Configuration.class)
public class Scim2Connector extends BaseFullAccessConnector<Scim2Configuration> {

    public Scim2Connector() {
        super(Scim2Configuration.class);
        setAuthenticator((Authenticator) new DirectAccessTokenAuthenticator());
        setDriver(new Scim2Driver());
        setAdapters(new Scim2UserAdapter(), new Scim2GroupsAdapter());
    }

    public void testPartialConfiguration() {
        test();
    }
}
