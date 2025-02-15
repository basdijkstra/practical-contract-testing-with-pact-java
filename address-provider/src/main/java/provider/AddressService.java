package provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public Address getAddress(UUID addressId) {

        return addressRepository.getById(addressId);
    }

    public void deleteAddress(UUID addressId) {

        addressRepository.deleteById(addressId);
    }
}
