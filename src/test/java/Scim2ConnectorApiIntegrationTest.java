import static org.junit.jupiter.api.Assertions.*;

import com.exclamationlabs.connid.base.connector.configuration.ConfigurationReader;
import com.exclamationlabs.connid.base.connector.test.ApiIntegrationTest;
import com.exclamationlabs.connid.base.scim2.Scim2Connector;
import com.exclamationlabs.connid.base.scim2.configuration.Scim2Configuration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

import org.apache.commons.lang3.StringUtils;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.common.objects.filter.EqualsFilter;
import org.identityconnectors.framework.common.objects.filter.Filter;
import org.identityconnectors.framework.common.objects.filter.FilterVisitor;
import org.identityconnectors.test.common.ToListResultsHandler;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Scim2ConnectorApiIntegrationTest
    extends ApiIntegrationTest<Scim2Configuration, Scim2Connector> {

  private static final String generatedGroupName = "redacted";
  private static String generatedUserId;
  private static String generatedGroupId;
  private static final String existingEntUser  = "U06SRNW2AS0";
  private static final String existingStdUser  = "U07GJHF2BGD";
  private static final String existingUserName = "services-dev";

  protected String composeComplexType(String value, String type, String display, Boolean primary){
    StringJoiner joiner = new StringJoiner(",", "{", "}");
    if ( value != null && value.length() > 0 ) {
      joiner.add(String.format("\"value\":\"%s\"", value));
    }
    if ( type != null && type.length() > 0 ) {
      joiner.add(String.format("\"type\":\"%s\"", type));
    }
    if ( display != null && display.length() > 0 ) {
      joiner.add(String.format("\"display\":\"%s\"", display));
    }
    if ( primary != null ) {
      joiner.add(String.format("\"primary\": %s", primary));
    }
    return joiner.toString();
  }
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
  @Order(100)
  public void test100UserCreate() {
    // Creates a user that will be deleted at the end
    ObjectClass objClazz = new ObjectClass("Scim2User");
    Set<Attribute> attributes = new HashSet<>();
    attributes.add(new AttributeBuilder().setName("userName").addValue("gwashington").build());
    attributes.add(new AttributeBuilder().setName("externalId").addValue("gwashington").build());
    attributes.add(new AttributeBuilder().setName("name_familyName").addValue("Washington").build());
    attributes.add(new AttributeBuilder().setName("name_givenName").addValue("George").build());
    attributes.add(new AttributeBuilder().setName("name_middleName").addValue("Founder").build());
    attributes.add(new AttributeBuilder().setName("name_honorificPrefix").addValue("General").build());
    attributes.add(new AttributeBuilder().setName("name_honorificSuffix").addValue("Senior").build());
    attributes.add(new AttributeBuilder().setName("displayName").addValue("George Washington").build());
    attributes.add(new AttributeBuilder().setName("locale").addValue("en_US.UTF-8").build());
    // attributes.add(new AttributeBuilder().setName("nickName").addValue("Tommy").build());
    attributes.add(new AttributeBuilder().setName("preferredLanguage").addValue("en").build());
    attributes.add(new AttributeBuilder().setName("profileUrl").addValue("http://www.exclamationlabs.com/").build());
    attributes.add(new AttributeBuilder().setName("title").addValue("President").build());
    attributes.add(new AttributeBuilder().setName("timezone").addValue("America/New_York").build());
    attributes.add(new AttributeBuilder().setName("userType").addValue("Employee").build());

    Set<String> phones = new HashSet<>();
    phones.add(composeComplexType("954-555-1776", "work", null, true));
    phones.add(composeComplexType("954-555-1800", "mobile", null, false));
    attributes.add(new AttributeBuilder().setName("phones").addValue(phones).build());

    Set<String> emails = new HashSet<>();
    emails.add(composeComplexType("services-dev+gwwork@provisioniam.com", "work", null, true));
    emails.add(composeComplexType("services-dev+gwhome@provisioniam.com", "home", null, null));
    attributes.add(new AttributeBuilder().setName("emails").addValue(emails).build());

    Set<String> photos = new HashSet<>();
    photos.add(composeComplexType("https://gravatar.com/avatar/abdf31385275b3157dbb610b80041a44?size=256", "photo", null, true));
    attributes.add(new AttributeBuilder().setName("photos").addValue(photos).build());


    Uid newId = getConnectorFacade().create(
            objClazz, attributes, new OperationOptionsBuilder().build());
    assertNotNull(newId);
    assertNotNull(newId.getUidValue());
    generatedUserId = newId.getUidValue();
  }

  @Test
  @Order(110)
  public void test110UpdateUser()
  {
    ToListResultsHandler listHandler = new ToListResultsHandler();
    ObjectClass objClazz = new ObjectClass("Scim2User");
    OperationOptions options = new OperationOptionsBuilder().build();
    Uid uid = new Uid("U07GJHF2BGD");
    Set<AttributeDelta> delta = new HashSet<>();
    delta.add(new AttributeDeltaBuilder().setName("userName").addValueToReplace("tjefferson").build());
    Set<String> emails = new HashSet<>();
    emails.add(composeComplexType("services-dev+tjwork@provisioniam.com", "work", null, true));
    emails.add(composeComplexType("services-dev+tjhome@provisioniam.com", "home", null, null));
    delta.add(new AttributeDeltaBuilder().setName("emails").addValueToReplace(emails).build());
    delta.add(new AttributeDeltaBuilder().setName(OperationalAttributes.ENABLE_NAME).addValueToReplace(true).build());
    Set<AttributeDelta> output = getConnectorFacade().updateDelta(objClazz, uid, delta, options);
    assertNotNull(output);
  }

  @Test
  @Order(120)
  public void test120UserGet() {
    ToListResultsHandler listHandler = new ToListResultsHandler();
    ObjectClass objClazz = new ObjectClass("Scim2User");
    Attribute id =
        new AttributeBuilder().setName(Uid.NAME).addValue(existingStdUser).build();

    getConnectorFacade()
        .search(
            objClazz,
            new EqualsFilter(id),
            listHandler,
            new OperationOptionsBuilder().build());
    List<ConnectorObject> users = listHandler.getObjects();
    assertEquals(1, users.size());
    String userId = users.get(0).getAttributeByName(Uid.NAME).getValue().get(0).toString();
    assertTrue(StringUtils.isNotBlank(userId));
    assertEquals(existingStdUser, userId);
  }
  @Test
  @Order(125)
  public void test125UserGetEnterprise() {
    ToListResultsHandler listHandler = new ToListResultsHandler();
    ObjectClass objClazz = new ObjectClass("Scim2User");
    Attribute id =
            new AttributeBuilder().setName(Uid.NAME).addValue(existingEntUser).build();

    getConnectorFacade().search(
                    objClazz,
                    new EqualsFilter(id),
                    listHandler,
                    new OperationOptionsBuilder().build());
    List<ConnectorObject> users = listHandler.getObjects();
    assertEquals(1, users.size());
    String userId = users.get(0).getAttributeByName(Uid.NAME).getValue().get(0).toString();
    assertEquals(existingEntUser, userId);
  }

  @Test
  //@Disabled
  public void test150GetAllUsers() {
    ToListResultsHandler listHandler = new ToListResultsHandler();
    ObjectClass objClazz = new ObjectClass("Scim2User");
    getConnectorFacade()
        .search(
            objClazz,
            new Filter() {
              @Override
              public boolean accept(ConnectorObject obj) {
                return false;
              }

              @Override
              public <R, P> R accept(FilterVisitor<R, P> v, P p) {
                return null;
              }
            },
            listHandler,
            new OperationOptionsBuilder().build());
    // new Scim2Connector().executeQuery(ObjectClass.ACCOUNT, "", listHandler, new
    // OperationOptionsBuilder().build());
    List<ConnectorObject> users = listHandler.getObjects();
    assertNotNull(users);
    assertTrue(users.size() > 0);
  }
  @Test
  @Disabled
  @Order(210)
  public void test210GroupCreate() {
    Set<Attribute> attributes = new HashSet<>();
    /*
    attributes.add(
            new AttributeBuilder().setName(GROUP_NAME.name()).addValue(generatedGroupName).build());
            */
    generatedGroupId =
            getConnectorFacade()
                    .create(ObjectClass.GROUP, attributes, new OperationOptionsBuilder().build())
                    .getUidValue();
  }

  @Test
  @Order(310)
  public void test390UserDelete() {
    ObjectClass objClazz = new ObjectClass("Scim2User");
    getConnectorFacade().delete(objClazz, new Uid("U07GJHF2BGD"), new OperationOptionsBuilder().build());
  }
}
