package Domain.Cards.InMemory;

import Domain.Cards.Interfaces.IVendorRepository;
import Model.Cards.Vendor.IVendor;
import Model.Cards.Vendor.NormalVendor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryIVendorRepository implements IVendorRepository {

    ConcurrentMap<Long, IVendor> vendors = new ConcurrentHashMap<>();
    Long currentID = 1L;

    public InMemoryIVendorRepository() {
        persistEntity(new NormalVendor());
    }

    @Override
    public synchronized Long persistEntity(IVendor entity) {
        while (vendors.containsKey(currentID)) {
            currentID++;
        }
        entity.setId(currentID);
        vendors.put(currentID, entity);
        return currentID;
    }

    @Override
    public synchronized boolean updateEntity(IVendor entity) {
        if (entity.getId() != null) {
            if (vendors.containsKey(entity.getId())) {
                vendors.put(entity.getId(), entity);
                return true;
            }
        }
        return false;
    }

    @Override
    public IVendor findEntity(Long id) {
        return vendors.get(id);
    }

    @Override
    public synchronized boolean deleteEntity(Long id) {
        if (vendors.containsKey(id)) {
            vendors.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public List<IVendor> getAllEntities() {
        return new ArrayList<>(vendors.values());
    }


}
