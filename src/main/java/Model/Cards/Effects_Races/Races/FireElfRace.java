package Model.Cards.Effects_Races.Races;

import Model.Battle.State;
import Model.Cards.ACard;
import Model.Cards.MonsterCard;

public class FireElfRace extends ABaseRace {
    public FireElfRace(IRace race) {
        super.base=race;
        super.name = "fireelf";
    }

    @Override
    public double affect(ACard thisCard, ACard opponentCard, State state) {
        return thisCard.getDamage();
    }
}