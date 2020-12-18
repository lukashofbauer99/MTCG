package Model.Cards.Effects_Races.Races;

import Model.Battle.State;
import Model.Cards.ACard;

public class BaseRace extends ABaseRace {
    public BaseRace() {
        super.name = "baseRace";
    }

    @Override
    public double affect(ACard thisCard, ACard opponentCard, State state) {
        return thisCard.getDamage();
    }
}