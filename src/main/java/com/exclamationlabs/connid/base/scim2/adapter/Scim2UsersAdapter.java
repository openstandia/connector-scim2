package com.exclamationlabs.connid.base.scim2.adapter;

import com.exclamationlabs.connid.base.connector.adapter.BaseAdapter;
import com.exclamationlabs.connid.base.connector.attribute.ConnectorAttribute;
import com.exclamationlabs.connid.base.connector.attribute.ConnectorAttributeDataType;
import com.exclamationlabs.connid.base.scim2.configuration.Scim2Configuration;
import com.exclamationlabs.connid.base.scim2.model.Scim2Schema;
import com.exclamationlabs.connid.base.scim2.model.Scim2User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.identityconnectors.framework.common.objects.*;

public class Scim2UsersAdapter extends BaseAdapter<Scim2User, Scim2Configuration> {
  @Override
  public ObjectClass getType() {
    return null;
  }

  @Override
  public Class<Scim2User> getIdentityModelClass() {
    return null;
  }

  @Override
  public Set<ConnectorAttribute> getConnectorAttributes() {
    // if configuration standard user/enterprise user, case stmt, Build ConnectorAttribute
    // accordingly
    // Remember Enterprise user is a super set of User
    // If user Dynamic, Build ConnectorAttribute accordingly
    String rawJson = getConfiguration().getSchemaRawJson();
    System.out.println("RAW JSON ---> " + rawJson);

    // String jsonFilePath =
    // "/Users/sdaka/Desktop/DSR/work/iamscim2/src/test/resources/Schema_raw_JSON.json";
    ObjectMapper objectMapper = new ObjectMapper();
    List<Scim2Schema> schemaPojo = null;
    try {
      schemaPojo =
          objectMapper.readValue(new File(rawJson), new TypeReference<List<Scim2Schema>>() {});
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    System.out.println("DSR 1: " + schemaPojo);
    System.out.println("DSR 2: " + schemaPojo.size());

    Set<ConnectorAttribute> result = new HashSet<>();

    schemaPojo.forEach(
        obj -> {
          boolean user = obj.getName().equalsIgnoreCase("User");
          if (user) {
            List<com.exclamationlabs.connid.base.scim2.model.Attribute> userAttributes =
                obj.getAttributes();
            for (com.exclamationlabs.connid.base.scim2.model.Attribute userAttribute :
                userAttributes) {
              String req = userAttribute.getRequired() ? "REQUIRED" : "";
              result.add(
                  new ConnectorAttribute(
                      userAttribute.getName(),
                      ConnectorAttributeDataType.valueOf(userAttribute.getType()),
                      AttributeInfo.Flags.valueOf(req)));
            }
          }
        });
    System.out.println(schemaPojo);

    return result;
  }

  @Override
  protected Set<Attribute> constructAttributes(Scim2User model) {
    return null;
  }

  @Override
  protected Scim2User constructModel(
      Set<Attribute> attributes,
      Set<Attribute> addedMultiValueAttributes,
      Set<Attribute> removedMultiValueAttributes,
      boolean isCreate) {
    return null;
  }
}
