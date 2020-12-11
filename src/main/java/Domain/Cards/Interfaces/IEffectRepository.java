package Domain.Cards.Interfaces;

import Domain.IRepository;
import Model.Cards.Effects_Races.Effects.IEffect;

public interface IEffectRepository extends IRepository<IEffect, Long> {

    IEffect getIEffectWithName(String Name);
}
