package customer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.LambdaDsl;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Map;
import java.util.UUID;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "address_provider", pactVersion = PactSpecVersion.V3)
public class GetAddressTest {

    @Pact(provider = "address_provider", consumer = "customer_consumer")
    public RequestResponsePact pactForGetExistingAddressId(PactDslWithProvider builder) {

        /**
         * TODO: Add the following expectations for the provider response to the existing ones:
         *   - The response should contain a field 'zipCode' with an integer value
         *   - The response should contain a field 'state' with a string value
         *   - EXTRA: The response should contain a field 'country' with a value that can only be 'United States' or 'Canada'
         *  (for the last one, have a look at https://docs.pact.io/implementation_guides/jvm/consumer#dsl-matching-methods for a hint)
         */
        DslPart body = LambdaDsl.newJsonBody((o) -> o
                .uuid("id", UUID.fromString(AddressId.EXISTING_ADDRESS_ID))
                .stringType("addressType", "billing")
                .stringType("street", "Main Street")
                .integerType("number", 123)
                .stringType("city", "Nothingville")
        ).build();

        Map<String, Object> providerStateParams = Map.of("addressId", AddressId.EXISTING_ADDRESS_ID);

        return builder
                .given("Address exists", providerStateParams)
                .uponReceiving("Retrieving an existing address ID")
                .path(String.format("/address/%s", AddressId.EXISTING_ADDRESS_ID))
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(body)
                .toPact();
    }

    /**
     * TODO: uncomment the pactForGetNonExistentAddressId() method and complete its implementation by:
     *   - removing the 'return null;' statement from the code
     *   - specifying that a GET to /address/00000000-0000-0000-0000-000000000000 is to be performed
     *   - specifying that this request should return an HTTP 404
     *   - generating a pact segment from these expectations and returning that
     *   You should use a provider state with the exact name 'Address does not exist'
     *     using a parameterized provider state just like we saw in the videos, and just like in the interaction defined above.
     *   The implementation is very similar to the one above, but does not need the body() part as we don't expect
     *   the provider to return a response body in this situation.
     */
//    @Pact(provider = "address_provider", consumer = "customer_consumer")
//    public RequestResponsePact pactForGetNonExistentAddressId(PactDslWithProvider builder) {
//
//        return null;
//    }

    /**
     * TODO: create a third method that returns a RequestResponsePact object containing the expectations
     *   around provider behaviour for the situation where the address ID provided is not a valid UUID:
     *   - An example value could be 'strawberry' or 'invalid_uuid'
     *   - In this situation, the provider should return an HTTP 400 (Bad Request)
     *   You should use a provider state with the exact name 'No specific state required'
     *     using a parameterized provider state just like we saw in the videos, and just like in the interaction defined above.
     */


    @Test
    @PactTestFor(pactMethod = "pactForGetExistingAddressId")
    public void testFor_GET_existingAddressId_shouldYieldExpectedAddressData(MockServer mockServer) {

        AddressServiceClient client = new AddressServiceClient(mockServer.getUrl());

        Address address = client.getAddress(AddressId.EXISTING_ADDRESS_ID);

        Assertions.assertEquals(AddressId.EXISTING_ADDRESS_ID, address.getId());
    }

    /**
     * TODO: uncomment the test method after completion of the pactForGetNonExistentAddressId()
     *   method to add this interaction to the contract for the customer_consumer
     */
//    @Test
//    @PactTestFor(pactMethod = "pactForGetNonExistentAddressId")
//    public void testFor_GET_nonExistentAddressId_shouldYieldHttp404(MockServer mockServer) {
//
//        AddressServiceClient client = new AddressServiceClient(mockServer.getUrl());
//
//        Assertions.assertThrows(NotFoundException.class, () -> client.getAddress(AddressId.NON_EXISTING_ADDRESS_ID));
//    }

    /**
     * TODO: after you have created the method returning a RequestResponsePact for the HTTP 400 situation,
     *   create a third test method that calls getAddress() on the AddressClient and verify that it throws a
     *   BadRequestException, very similar to the test for the HTTP 404 situation defined just above.
     */

    /**
     * TODO: After you have completed all the exercises, run the tests for the Customer consumer service using 'mvn clean test'.
     *   Verify that a consumer contract is generated in the target/pacts folder and that this contract
     *   contains all three interactions we have defined in this class, as well as a fourth interaction for the HTTP DELETE interaction.
     */
}
