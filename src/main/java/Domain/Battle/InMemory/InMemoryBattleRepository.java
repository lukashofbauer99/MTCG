package Domain.Battle.InMemory;

import Domain.Battle.Interfaces.IBattleRepository;
import Model.Battle.Battle;
import Model.Cards.Effects_Races.Effects.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryBattleRepository implements IBattleRepository {

    ConcurrentMap<Long, Battle> battles = new ConcurrentHashMap<>();
    Long currentID = 1L;

    public InMemoryBattleRepository() {

    }

    @Override
    public synchronized Long persistEntity(Battle entity) {
        while (battles.containsKey(currentID)) {
            currentID++;
        }
        entity.setId(currentID);
        battles.put(currentID, entity);
        return currentID;
    }

    @Override
    public synchronized boolean updateEntity(Battle entity) {
        if (entity.getId() != null) {
            if (battles.containsKey(entity.getId())) {
                battles.put(entity.getId(), entity);
                return true;
            }
        }
        return false;
    }

    @Override
    public Battle findEntity(Long id) {
        return battles.get(id);
    }

    @Override
    public synchronized boolean deleteEntity(Long id) {
        if (battles.containsKey(id)) {
            battles.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Battle> getAllEntities() {
        return new ArrayList<>(battles.values());
    }

}
