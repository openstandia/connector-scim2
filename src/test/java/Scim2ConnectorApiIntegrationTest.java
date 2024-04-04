import static com.exclamationlabs.connid.base.scim2.attribute.Scim2GroupAttribute.GROUP_NAME;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.exclamationlabs.connid.base.connector.configuration.ConfigurationNameBuilder;
import com.exclamationlabs.connid.base.connector.configuration.ConfigurationReader;
import com.exclamationlabs.connid.base.connector.test.ApiIntegrationTest;
import com.exclamationlabs.connid.base.scim2.Scim2Connector;
import com.exclamationlabs.connid.base.scim2.configuration.Scim2Configuration;
import java.util.HashSet;
import java.util.Set;
import org.identityconnectors.framework.common.objects.*;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Scim2ConnectorApiIntegrationTest
    extends ApiIntegrationTest<Scim2Configuration, Scim2Connector> {

  private static final String generatedGroupName = "redacted";
  private static String generatedGroupId;

  @Override
  protected Scim2Configuration getConfiguration() {
    return new Scim2Configuration(
        new ConfigurationNameBuilder().withConnector(() -> "SCIM2").build());
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
    /* System.out.println("This is getting called");
    String jsonFilePath = "/Users/sdaka/Desktop/DSR/work/iamscim2/src/test/resources/Schema_raw_JSON.json";
    ObjectMapper objectMapper = new ObjectMapper();
    List<Scim2Schema> schemaPojo = null;
    try {
        schemaPojo = objectMapper.readValue(new File(jsonFilePath), new TypeReference<List<Scim2Schema>>() {});
    } catch (IOException e) {
        throw new RuntimeException(e);
    }

    // Now you can use the pojo object, which contains the data from the JSON file
    System.out.println("Data from JSON file:");
    System.out.println(schemaPojo);

    Set<ConnectorAttribute> result = new HashSet<>();

    schemaPojo.forEach(obj->{

        boolean user = obj.getName().equalsIgnoreCase("User");


        if(user){
            List<com.exclamationlabs.connid.base.scim2.model.Attribute> userAttributes = obj.getAttributes();
            for (com.exclamationlabs.connid.base.scim2.model.Attribute userAttribute : userAttributes) {
                AttributeInfo.Flags req = userAttribute.getRequired() ? REQUIRED : NOT_READABLE;
                String STRING_TYPE = userAttribute.getType().equalsIgnoreCase("string") ? "STRING" : "";

                if(userAttribute.getSubAttributes()!=null && !userAttribute.getSubAttributes().isEmpty()){
                    for (com.exclamationlabs.connid.base.scim2.model.SubAttribute subAttribute : userAttribute.getSubAttributes()) {
                       // String STRING_TYPE_INNER = userAttribute.getType().equalsIgnoreCase("string") ? "STRING" : "";
                        result.add(new ConnectorAttribute(subAttribute.getName(), ConnectorAttributeDataType.valueOf("STRING")));
                    }

                }

                result.add(new ConnectorAttribute(userAttribute.getName(), ConnectorAttributeDataType.valueOf("STRING"), req));
            }
        }
    });

    System.out.println("result is  "+result);*/

    super.setup();
  }

  @Test
  @Order(50)
  public void test050Test() {
    getConnectorFacade().test();
  }

  @Test
  @Order(60)
  public void test060Schema() {
    assertNotNull(getConnectorFacade().schema());
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
}
