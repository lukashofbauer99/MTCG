package Domain.Cards.Interfaces;

import Domain.IRepository;
import Model.Cards.CardPacks.ICardPack;
import Model.Cards.Effects_Races.Effects.IEffect;
import Model.Cards.Effects_Races.Races.IRace;

public interface IRaceRepository extends IRepository<IRace, Long> {
    IRace getIRaceWithName(String Name);
}
