package provider;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class AddressRepository {

    private final Map<UUID, Address> addresses = new HashMap<>();

    public void insert(Address address) {

        addresses.put(address.getId(), address);
    }

    public Address getById(UUID id) {

        return addresses.get(id);
    }

    public void deleteById(UUID id) {

        addresses.remove(id);
    }
}
