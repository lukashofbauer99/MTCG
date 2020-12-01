package Model.Cards.Effects_Races.Races;

import Model.Cards.ACard;

public class BaseRace extends ABaseRace {
    public BaseRace() {
        super.name = "baseRace";
    }

    @Override
    public int affect(ACard thisCard, ACard opponentCard) {
        return thisCard.getDamage();
    }
}