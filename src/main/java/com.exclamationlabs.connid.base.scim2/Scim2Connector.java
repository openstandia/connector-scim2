package com.exclamationlabs.connid.base.scim2;

import com.exclamationlabs.connid.base.connector.BaseFullAccessConnector;
import com.exclamationlabs.connid.base.connector.authenticator.Authenticator;
import com.exclamationlabs.connid.base.connector.authenticator.OAuth2TokenClientCredentialsAuthenticator;
import com.exclamationlabs.connid.base.scim2.adapter.Scim2GroupsAdapter;
import com.exclamationlabs.connid.base.scim2.adapter.Scim2UsersAdapter;
import com.exclamationlabs.connid.base.scim2.configuration.Scim2Configuration;
import com.exclamationlabs.connid.base.scim2.driver.rest.Scim2Driver;
import java.util.Collections;
import java.util.Map;
import org.identityconnectors.framework.spi.ConnectorClass;

@ConnectorClass(
    displayNameKey = "scim2.connector.display",
    configurationClass = Scim2Configuration.class)
public class Scim2Connector extends BaseFullAccessConnector<Scim2Configuration> {

  public Scim2Connector() {
    super(Scim2Configuration.class);
    setAuthenticator(
        (Authenticator)
            new OAuth2TokenClientCredentialsAuthenticator() {
              @Override
              public String getGrantType() {
                return "account_credentials";
              }

              @Override
              public Map<String, String> getAdditionalFormFields() {
                return Collections.singletonMap("scim2UserUrl", configuration.getScim2UserUrl());
              }
            });
    setDriver(new Scim2Driver());
    setAdapters(new Scim2UsersAdapter(), new Scim2GroupsAdapter());
  }
}
