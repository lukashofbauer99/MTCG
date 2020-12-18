package Domain.Cards.InMemory;

import Domain.Cards.Interfaces.ICardPackRepository;
import Model.Cards.CardPacks.ICardPack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryCardPackRepository implements ICardPackRepository {

    ConcurrentMap<Long, ICardPack> cardPacks = new ConcurrentHashMap<>();
    Long currentID = 1L;


    @Override
    public synchronized Long persistEntity(ICardPack entity) {
        while (cardPacks.containsKey(currentID)) {
            currentID++;
        }
        entity.setId(currentID);
        cardPacks.put(currentID, entity);
        return currentID;
    }

    @Override
    public synchronized boolean updateEntity(ICardPack entity) {
        if (entity.getId() != null) {
            if (cardPacks.containsKey(entity.getId())) {
                cardPacks.put(entity.getId(), entity);
                return true;
            }
        }
        return false;
    }

    @Override
    public ICardPack findEntity(Long id) {
        return cardPacks.get(id);
    }

    @Override
    public synchronized boolean deleteEntity(Long id) {
        if (cardPacks.containsKey(id)) {
            cardPacks.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public List<ICardPack> getAllEntities() {
        return new ArrayList<>(cardPacks.values());
    }

}
