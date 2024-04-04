import com.exclamationlabs.connid.base.connector.test.util.ConnectorMockRestTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class Scim2ConnectorTest extends ConnectorMockRestTest {

  /*private Scim2Connector connector;
  @BeforeEach
  public void setup() {
      connector = new Scim2Connector() {
          @Override
          public void init(Configuration configuration) {
              setAuthenticator(null);
              setDriver(
                      new Scim2Driver() {
                          @Override
                          protected HttpClient createClient() {
                              return stubClient;
                          }
                      });
              super.init(configuration);
          }
      };
      Scim2Configuration configuration = getScim2Configuration();

      connector.init(configuration);
  }

  private static Scim2Configuration getScim2Configuration() {
      Scim2Configuration configuration = new Scim2Configuration();
      configuration.setServiceUrl("test");
      configuration.setTokenUrl("test");
      configuration.setClientId("test1");
      configuration.setConfigGroup("group");
      configuration.setScim2UserUrl("url");
      configuration.setScim2GroupUrl("groupurl");
      configuration.setConfigUser("user");
     // configuration.set
      //configuration.setAccountId("1234");
      configuration.setClientSecret(new GuardedString("test2".toCharArray()));
      return configuration;
  }

  @Test
  public void test100Test() {
      final String responseData =
              "{\"id\":\"ZpRAY4X9SEipRS9kS--Img\",\"configGroup\":\"5555\",\"first_name\":\"Alfred\",\"last_name\":\"Neuman\",\"email\":\"alfred@mad.com\",\"scim2UserUrl\":\"test.com\",\"configUser\":\"SDAKA\",\"timezone\":\"America/Chicago\",\"verified\":0,\"created_at\":\"2020-05-06T19:22:24Z\",\"last_login_time\":\"2020-05-10T19:37:29Z\",\"scim2GroupUrl\":\"https://lh6.googleusercontent.com/-mboZtlAHsM4/AAAAAAAAAAI/AAAAAAAAAAA/AMZuuclRl5BboLrsXCiJ9dRBBD1yEIG2ww/photo.jpg\",\"language\":\"en-US\",\"phone_number\":\"\",\"status\":\"active\"}";
      prepareMockResponse(responseData);
      connector.test();
  }*/

}
