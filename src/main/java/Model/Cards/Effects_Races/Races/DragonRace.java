package Model.Cards.Effects_Races.Races;

import Model.Cards.ACard;

public class DragonRace extends ABaseRace {
    public DragonRace(IRace race) {

        super.base = race;
        super.name = "OrkRace";

    }

    @Override
    public int affect(ACard thisCard, ACard opponentCard) {
            return thisCard.getDamage();
    }
}