package provider;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junitsupport.loader.PactBrokerAuth;
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Provider("address_provider")
@PactBroker(url="${PACT_BROKER_BASE_URL}", authentication = @PactBrokerAuth(token = "${PACT_BROKER_TOKEN}"))
public class ContractVerificationTest {

    @LocalServerPort
    int port;

    @MockBean
    private AddressRepository addressRepository;

    @BeforeEach
    public void setUp(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", port));
    }

    @TestTemplate
    @ExtendWith(PactVerificationSpringProvider.class)
    public void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @State("Address exists")
    public void addressWithIdExists(Map<String, Object> params) {
        String addressId = params.get("addressId").toString();

        Address address = new Address();
        address.setId(UUID.fromString(addressId));
        address.setAddressType("billing");
        address.setStreet("Rodeo Drive");
        address.setNumber(123);
        address.setCity("Beverly Hills");
        address.setZipCode(90210);
        address.setState("CA");
        address.setCountry("United States");

        when(addressRepository.getById(UUID.fromString(addressId))).thenReturn(address);
    }

    /**
     * TODO: Add a provider state handler method for the provider state 'No specific state required'.
     *   The method body can be left empty, as we do not need to perform specific setup
     *   on the provider side for this specific provider state.
     */
    

    /**
     * TODO: Add a provider state handler method for the provider state 'Address does not exist'.
     *   Make sure it takes provider state parameters.
     *   In the body of the method, extract the 'addressId' value from the provider state parameters.
     *   Set up the address repository mock so that it returns null when we call the getById() method
     *   using this address ID as an argument.
     */

}