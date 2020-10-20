package Model.Cards.Effects_Races.Races;

import Model.Cards.ACard;

public class DragonRace extends ARace {
    public DragonRace(ARace race) {

        super.base = race;
        super.name = "OrkRace";

    }

    @Override
    public int affect(ACard thisCard, ACard opponentCard) {
            return thisCard.getDamage();
    }
}