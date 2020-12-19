package Domain.User.InMemory;

import Domain.User.Interfaces.ITradeRepository;
import Model.Cards.ACard;
import Model.User.Trade.ITrade;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryITradeRepository implements ITradeRepository {

    ConcurrentMap<String, ITrade> trades = new ConcurrentHashMap<>();
    Long currentID = 1L;


    @Override
    public synchronized String persistEntity(ITrade entity) {
        while (trades.containsKey(currentID.toString())) {
            currentID++;
        }
        entity.setId(currentID.toString());
        trades.put(currentID.toString(), entity);
        return currentID.toString();
    }


    @Override
    public synchronized String persistEntityGenNoId(ITrade entity) {
        if (!trades.containsKey(entity.getId())) {
            trades.put(entity.getId(), entity);
            return entity.getId();
        }
        return null;
    }

    @Override
    public synchronized boolean updateEntity(ITrade entity) {
        if (entity.getId() != null) {
            if (trades.containsKey(entity.getId())) {
                trades.put(entity.getId(), entity);
                return true;
            }
        }
        return false;
    }

    @Override
    public ITrade findEntity(String id) {
        return trades.get(id);
    }

    @Override
    public synchronized boolean deleteEntity(String id) {
        if (trades.containsKey(id)) {
            trades.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public List<ITrade> getAllEntities() {
        return new ArrayList<>(trades.values());
    }

}
