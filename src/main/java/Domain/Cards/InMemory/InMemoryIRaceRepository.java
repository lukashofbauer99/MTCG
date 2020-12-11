package Domain.Cards.InMemory;

import Domain.Cards.Interfaces.IRaceRepository;
import Model.Cards.Effects_Races.Effects.BaseEffect;
import Model.Cards.Effects_Races.Effects.FireEffect;
import Model.Cards.Effects_Races.Effects.IEffect;
import Model.Cards.Effects_Races.Effects.WaterEffect;
import Model.Cards.Effects_Races.Races.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryIRaceRepository implements IRaceRepository {

    ConcurrentMap<Long, IRace> races = new ConcurrentHashMap<>();
    Long currentID= 1L;

    public InMemoryIRaceRepository() {
        IRace base=new BaseRace();
        persistEntity(base);
        persistEntity(new GoblinRace(base));
        persistEntity(new DragonRace(base));
        persistEntity(new WizardRace(base));
        persistEntity(new OrkRace(base));
        persistEntity(new KrakenRace(base));
        persistEntity(new FireElfRace(base));
        persistEntity(new KnightRace(base));
    }

    @Override
    public synchronized Long persistEntity(IRace entity) {
        while (races.containsKey(currentID)) {
            currentID++;
        }
        entity.setId(currentID);
        races.put(currentID, entity);
        return currentID;
    }

    @Override
    public synchronized boolean updateEntity(IRace entity) {
        if(entity.getId()!=null) {
            if (races.containsKey(entity.getId())) {
                races.put(entity.getId(), entity);
                return true;
            }
        }
        return false;
    }

    @Override
    public IRace findEntity(Long id) {
        return races.get(id);
    }

    @Override
    public synchronized boolean deleteEntity(Long id) {
        if(races.containsKey(id)) {
            races.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public List<IRace> getAllEntities() {
        return new ArrayList<>(races.values());
    }

    @Override
    public IRace getIRaceWithName(String Name) {
        return races.values().stream().filter(x->x.getName().equals(Name.toLowerCase())).findFirst().orElse(null);
    }

}
