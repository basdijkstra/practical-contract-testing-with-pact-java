package provider;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping(value = "/address/{addressId}", produces = "application/json")
    public Address getAddress(@PathVariable UUID addressId) {

        Address address = addressService.getAddress(addressId);

        if (address == null) {
            throw new NotFoundException();
        }

        return address;
    }

    @DeleteMapping("/address/{addressId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteAddress(@PathVariable UUID addressId) {

        addressService.deleteAddress(addressId);
    }
}
