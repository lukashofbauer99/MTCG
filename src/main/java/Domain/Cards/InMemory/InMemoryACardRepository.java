package Domain.Cards.InMemory;

import Domain.Cards.Interfaces.IACardRepository;
import Model.Cards.ACard;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryACardRepository implements IACardRepository {

    ConcurrentMap<String,ACard> cards = new ConcurrentHashMap<>();
    Long currentID= 1L;


    @Override
    public synchronized String persistEntity(ACard entity) {
        while (cards.containsKey(currentID.toString())) {
            currentID++;
        }
        entity.setId(currentID.toString());
        cards.put(currentID.toString(), entity);
        return currentID.toString();
    }


    @Override
    public synchronized String persistEntityGenNoId(ACard entity) {
        if (!cards.containsKey(entity.getId())) {
            cards.put(entity.getId(), entity);
            return entity.getId();
        }
        return null;
    }

    @Override
    public synchronized boolean updateEntity(ACard entity) {
        if(entity.getId()!=null) {
            if (cards.containsKey(entity.getId())) {
                cards.put(entity.getId(), entity);
                return true;
            }
        }
        return false;
    }

    @Override
    public ACard findEntity(String id) {
        return cards.get(id);
    }

    @Override
    public synchronized boolean deleteEntity(String id) {
        if(cards.containsKey(id)) {
            cards.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public List<ACard> getAllEntities() {
        return new ArrayList<>(cards.values());
    }

}
