package Model.Cards.Effects_Races;

import Model.Cards.ACard;
import Model.Cards.MonsterCard;

public class GoblinRace extends ARace {
    public GoblinRace() {
        super.name = "GoblinRace";
    }

    @Override
    public int affect(ACard thisCard, ACard opponentCard) {

        if(opponentCard.getClass()== MonsterCard.class)
            if(((MonsterCard) opponentCard).getRace().getClass()==OrkRace.class)
                return 0;

        return thisCard.getDamage();
    }
}