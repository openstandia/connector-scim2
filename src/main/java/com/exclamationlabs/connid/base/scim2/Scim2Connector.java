package com.exclamationlabs.connid.base.scim2;

import com.exclamationlabs.connid.base.connector.BaseFullAccessConnector;
import com.exclamationlabs.connid.base.connector.authenticator.Authenticator;
import com.exclamationlabs.connid.base.connector.authenticator.DirectAccessTokenAuthenticator;
import com.exclamationlabs.connid.base.scim2.adapter.Scim2GroupsAdapter;
import com.exclamationlabs.connid.base.scim2.adapter.Scim2UserAdapter;
import com.exclamationlabs.connid.base.scim2.configuration.Scim2Configuration;
import com.exclamationlabs.connid.base.scim2.driver.rest.Scim2Driver;
import org.identityconnectors.framework.common.objects.SuggestedValues;
import org.identityconnectors.framework.common.objects.SuggestedValuesBuilder;

import org.identityconnectors.framework.spi.ConnectorClass;
import org.identityconnectors.framework.spi.operations.DiscoverConfigurationOp;

import java.util.HashMap;
import java.util.Map;

@ConnectorClass(
    displayNameKey = "scim2.connector.display",
    configurationClass = Scim2Configuration.class)
public class Scim2Connector extends BaseFullAccessConnector<Scim2Configuration> implements DiscoverConfigurationOp{

  public Scim2Connector() {
    super(Scim2Configuration.class);
    setAuthenticator((Authenticator) new DirectAccessTokenAuthenticator());
    setDriver(new Scim2Driver());
    setAdapters(new Scim2UserAdapter(), new Scim2GroupsAdapter());
  }

  @Override
  public Map<String, SuggestedValues> discoverConfiguration()
  {
    Map<String, SuggestedValues> suggestions = new HashMap<>();

    // deepGet Suggestion
    if ( configuration.getDeepImport() != null )
    {
      suggestions.put("deepGet", SuggestedValuesBuilder.buildOpen(configuration.getDeepGet()));
    }
    else
    {
      suggestions.put("deepGet", SuggestedValuesBuilder.buildOpen(false));
    }
    // deepImport Suggestion
    if ( configuration.getDeepImport() != null )
    {
      suggestions.put("deepImport", SuggestedValuesBuilder.buildOpen(configuration.getDeepImport()));
    }
    else
    {
      suggestions.put("deepImport", SuggestedValuesBuilder.buildOpen(false));
    }
    // pagination Suggestion
    if ( configuration.getPagination() != null )
    {
      suggestions.put("pagination", SuggestedValuesBuilder.buildOpen(configuration.getPagination()));
    }
    else
    {
      suggestions.put("pagination", SuggestedValuesBuilder.buildOpen(true));
    }

    if ( configuration.getIoErrorRetries() != null )
    {
      suggestions.put("ioErrorRetries", SuggestedValuesBuilder.buildOpen(configuration.getIoErrorRetries()));
    }
    else
    {
      suggestions.put("ioErrorRetries", SuggestedValuesBuilder.buildOpen(3));
    }

    if ( configuration.getImportBatchSize() != null )
    {
      suggestions.put("importBatchSize", SuggestedValuesBuilder.buildOpen(configuration.getImportBatchSize()));
    }
    else
    {
      suggestions.put("importBatchSize", SuggestedValuesBuilder.buildOpen(20, 50, 100));
    }

    if ( configuration.getDuplicateErrorReturnsId() != null )
    {
      suggestions.put("duplicateErrorReturnsId", SuggestedValuesBuilder.buildOpen(configuration.getDuplicateErrorReturnsId()));
    }
    else
    {
      suggestions.put("duplicateErrorReturnsId", SuggestedValuesBuilder.buildOpen(true));
    }

    if ( !configuration.getEnableStandardSchema() )
    {
      suggestions.put("enableStandardSchema", SuggestedValuesBuilder.buildOpen(true));
    }
    if ( !configuration.getEnableEnterpriseUser())
    {
      suggestions.put("enableEnterpriseUser", SuggestedValuesBuilder.buildOpen(true));
    }

    if ( configuration.getUsersEndpointUrl() != null )
    {
      suggestions.put("usersEndpointUrl", SuggestedValuesBuilder.buildOpen(configuration.getUsersEndpointUrl()));
    }
    else
    {
      suggestions.put("usersEndpointUrl", SuggestedValuesBuilder.buildOpen("/Users"));
    }

    if ( configuration.getGroupsEndpointUrl() != null )
    {
      suggestions.put("groupsEndpointUrl", SuggestedValuesBuilder.buildOpen(configuration.getGroupsEndpointUrl()));
    }
    else
    {
      suggestions.put("groupsEndpointUrl", SuggestedValuesBuilder.buildOpen("/Groups"));
    }
    // User Schema ID List
    {
      String[] userSchemaIds = new String[]{
              "urn:ietf:params:scim:schemas:core:2.0:User",
              "urn:ietf:params:scim:schemas:extension:enterprise:2.0:User"};
      suggestions.put("userSchemaIdList", SuggestedValuesBuilder.buildOpen("urn:ietf:params:scim:schemas:core:2.0:User"));
    }
    // Group Schema ID List
    {
      String[] groupSchemaIds = new String[]{"urn:ietf:params:scim:schemas:core:2.0:Group"};
      suggestions.put("groupSchemaIdList", SuggestedValuesBuilder.buildOpen("urn:ietf:params:scim:schemas:core:2.0:Group"));
    }

    return suggestions;
  }
  public void testPartialConfiguration()
  {
    test();
  }
}
