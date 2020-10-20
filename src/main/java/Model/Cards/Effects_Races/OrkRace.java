package Model.Cards.Effects_Races;

import Model.Cards.ACard;

public class OrkRace extends ARace {
    public OrkRace() {
        super.name = "OrkRace";
    }

    @Override
    public int affect(ACard thisCard, ACard opponentCard) {
            return thisCard.getDamage();
    }
}