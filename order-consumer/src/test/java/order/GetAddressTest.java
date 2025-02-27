package order;

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

    @Pact(provider = "address_provider", consumer = "order_consumer")
    public RequestResponsePact pactForGetExistingAddressId(PactDslWithProvider builder) {

        DslPart body = LambdaDsl.newJsonBody((o) -> o
                .uuid("id", UUID.fromString(AddressId.EXISTING_ADDRESS_ID))
                .stringType("addressType", "billing")
                .stringType("street", "Main Street")
                .integerType("number", 123)
                .stringType("city", "Nothingville")
                .integerType("zipCode", 54321)
                .stringType("state", "Tennessee")
                .stringMatcher("country", "United States|Canada", "United States")
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

    @Pact(provider = "address_provider", consumer = "order_consumer")
    public RequestResponsePact pactForGetNonExistentAddressId(PactDslWithProvider builder) {

        Map<String, Object> providerStateParams = Map.of("addressId", AddressId.NON_EXISTING_ADDRESS_ID);

        return builder
                .given("Address does not exist", providerStateParams)
                .uponReceiving("Retrieving an address ID that does not exist")
                .path(String.format("/address/%s", AddressId.NON_EXISTING_ADDRESS_ID))
                .method("GET")
                .willRespondWith()
                .status(404)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "pactForGetExistingAddressId")
    public void testFor_GET_existingAddressId_shouldYieldExpectedAddressData(MockServer mockServer) {

        AddressServiceClient client = new AddressServiceClient(mockServer.getUrl());

        Address address = client.getAddress(AddressId.EXISTING_ADDRESS_ID);

        Assertions.assertEquals(AddressId.EXISTING_ADDRESS_ID, address.getId());
    }

    @Test
    @PactTestFor(pactMethod = "pactForGetNonExistentAddressId")
    public void testFor_GET_nonExistentAddressId_shouldYieldHttp404(MockServer mockServer) {

        AddressServiceClient client = new AddressServiceClient(mockServer.getUrl());

        Assertions.assertThrows(NotFoundException.class, () -> client.getAddress(AddressId.NON_EXISTING_ADDRESS_ID));
    }
}
