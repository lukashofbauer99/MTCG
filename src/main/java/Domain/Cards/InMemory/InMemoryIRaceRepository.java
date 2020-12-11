package Domain.Cards.InMemory;

import Domain.Cards.Interfaces.IRaceRepository;
import Model.Cards.Effects_Races.Effects.BaseEffect;
import Model.Cards.Effects_Races.Effects.FireEffect;
import Model.Cards.Effects_Races.Effects.IEffect;
import Model.Cards.Effects_Races.Effects.WaterEffect;
import Model.Cards.Effects_Races.Races.BaseRace;
import Model.Cards.Effects_Races.Races.DragonRace;
import Model.Cards.Effects_Races.Races.GoblinRace;
import Model.Cards.Effects_Races.Races.IRace;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryIRaceRepository implements IRaceRepository {

    ConcurrentMap<Long, IRace> races = new ConcurrentHashMap<>();
    Long currentID=1l;

    public InMemoryIRaceRepository() {
        IRace base=new BaseRace();
        persistEntity(base);
        persistEntity(new GoblinRace(base));
        persistEntity(new DragonRace(base));
        //TODO:add new Races here test them
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
        return new ArrayList<IRace>(races.values());
    }

}
