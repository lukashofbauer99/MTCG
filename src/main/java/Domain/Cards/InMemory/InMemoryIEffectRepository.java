package Domain.Cards.InMemory;

import Domain.Cards.Interfaces.IEffectRepository;
import Model.Cards.Effects_Races.Effects.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryIEffectRepository implements IEffectRepository {

    ConcurrentMap<Long,IEffect> effects = new ConcurrentHashMap<>();
    Long currentID= 1L;

    public InMemoryIEffectRepository() {
        IEffect base=new BaseEffect();
        persistEntity(base);
        persistEntity(new FireEffect(base));
        persistEntity(new WaterEffect(base));
        persistEntity(new NormalEffect(base));
    }

    @Override
    public synchronized Long persistEntity(IEffect entity) {
        while (effects.containsKey(currentID)) {
            currentID++;
        }
        entity.setId(currentID);
        effects.put(currentID, entity);
        return currentID;
    }

    @Override
    public synchronized boolean updateEntity(IEffect entity) {
        if(entity.getId()!=null) {
            if (effects.containsKey(entity.getId())) {
                effects.put(entity.getId(), entity);
                return true;
            }
        }
        return false;
    }

    @Override
    public IEffect findEntity(Long id) {
        return effects.get(id);
    }

    @Override
    public synchronized boolean deleteEntity(Long id) {
        if(effects.containsKey(id)) {
            effects.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public List<IEffect> getAllEntities() {
        return new ArrayList<>(effects.values());
    }

    @Override
    public IEffect getIEffectWithName(String Name) {
        return effects.values().stream().filter(x->x.getName().equals(Name.toLowerCase())).findFirst().orElse(null);
    }
}
