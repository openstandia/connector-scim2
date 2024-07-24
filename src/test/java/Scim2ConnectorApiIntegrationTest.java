import static com.exclamationlabs.connid.base.scim2.attribute.Scim2GroupAttribute.GROUP_NAME;
import static org.junit.jupiter.api.Assertions.*;

import com.exclamationlabs.connid.base.connector.configuration.ConfigurationReader;
import com.exclamationlabs.connid.base.connector.test.ApiIntegrationTest;
import com.exclamationlabs.connid.base.scim2.Scim2Connector;
import com.exclamationlabs.connid.base.scim2.attribute.Scim2UserAttribute;
import com.exclamationlabs.connid.base.scim2.attribute.slack.Scim2SlackUserAttribute;
import com.exclamationlabs.connid.base.scim2.configuration.Scim2Configuration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.exclamationlabs.connid.base.scim2.model.UserType;
import com.exclamationlabs.connid.base.scim2.model.slack.Scim2SlackUser;
import org.apache.commons.lang3.StringUtils;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.common.objects.filter.EqualsFilter;
import org.identityconnectors.framework.common.objects.filter.Filter;
import org.identityconnectors.framework.common.objects.filter.FilterVisitor;
import org.identityconnectors.test.common.ToListResultsHandler;
import org.junit.jupiter.api.*;
import com.exclamationlabs.connid.base.scim2.attribute.slack.Scim2SlackUserAttribute.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Scim2ConnectorApiIntegrationTest
    extends ApiIntegrationTest<Scim2Configuration, Scim2Connector> {

  private static final String generatedGroupName = "redacted";
  private static String generatedUserId;
  private static String generatedGroupId;

  @Override
  protected Scim2Configuration getConfiguration() {
    return new Scim2Configuration("slack_scim2");
  }

  @Override
  protected Class<Scim2Connector> getConnectorClass() {
    return Scim2Connector.class;
  }

  @Override
  protected void readConfiguration(Scim2Configuration scim2Configuration) {
    ConfigurationReader.setupTestConfiguration(scim2Configuration);
  }

  @BeforeEach
  public void setup() {
    super.setup();
  }

  @Test
  @Order(50)
  // @Disabled
  public void test050Test() {
    getConnectorFacade().test();
  }

  @Test
  @Order(60)
  public void test060Schema() {
    Schema ss = getConnectorFacade().schema();
    assertNotNull(ss);
  }

  @Test
  //@Disabled
  @Order(100)
  public void test100UserCreate() {
    // Creates a 'pending' user that will be deleted at the end
    Set<Attribute> attributes = new HashSet<>();
    attributes.add(new AttributeBuilder().setName("userName").addValue("local_test").build());
    attributes.add(new AttributeBuilder().setName("name").addValue("local.test").build());
    attributes.add(new AttributeBuilder().setName("emails").addValue("testiam@internet2.edu").build());
  //  attributes.add(new AttributeBuilder().setName(TYPE.name()).addValue(UserType.BASIC).build());
 //   attributes.add(new AttributeBuilder().setName(EMAIL.name()).addValue(userEmail).build());

    Uid newId =
        getConnectorFacade()
            .create(ObjectClass.ACCOUNT, attributes, new OperationOptionsBuilder().build());
    assertNotNull(newId);
    assertNotNull(newId.getUidValue());
     generatedUserId = newId.getUidValue();
  }

  @Test
  @Disabled
  @Order(210)
  public void test210GroupCreate() {
    Set<Attribute> attributes = new HashSet<>();
    attributes.add(
        new AttributeBuilder().setName(GROUP_NAME.name()).addValue(generatedGroupName).build());
    generatedGroupId =
        getConnectorFacade()
            .create(ObjectClass.GROUP, attributes, new OperationOptionsBuilder().build())
            .getUidValue();
  }

  @Test
  //@Disabled
  @Order(140)
  public void test115UserGet() {
    Attribute idAttribute = new AttributeBuilder().setName(Uid.NAME).addValue("U07CRPWQY5Q").build();

    results = new ArrayList<>();
    getConnectorFacade()
        .search(
            ObjectClass.ACCOUNT,
            new EqualsFilter(idAttribute),
            handler,
            new OperationOptionsBuilder().build());
    assertEquals(1, results.size());
    assertTrue(StringUtils.isNotBlank(results.get(0).getUid().getValue().get(0).toString()));
  }

  @Test
  public void test150GetAllUsers()
  {
    ToListResultsHandler listHandler = new ToListResultsHandler();
    getConnectorFacade().search(ObjectClass.ACCOUNT, new Filter() {
      @Override
      public boolean accept(ConnectorObject obj) {
        return false;
      }

      @Override
      public <R, P> R accept(FilterVisitor<R, P> v, P p) {
        return null;
      }
    }, listHandler, new OperationOptionsBuilder().build());
    //new Scim2Connector().executeQuery(ObjectClass.ACCOUNT, "", listHandler, new OperationOptionsBuilder().build());
    List<ConnectorObject> users = listHandler.getObjects();
    assertNotNull(users);
    assertTrue(users.size() > 0);
  }
}
