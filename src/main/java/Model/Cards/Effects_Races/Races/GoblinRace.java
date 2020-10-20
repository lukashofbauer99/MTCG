package Model.Cards.Effects_Races.Races;

import Model.Cards.ACard;
import Model.Cards.MonsterCard;

public class GoblinRace extends ARace {
    public GoblinRace(ARace race) {
        super.base=race;
        super.name = "GoblinRace";
    }

    @Override
    public int affect(ACard thisCard, ACard opponentCard) {

        if(opponentCard.getClass()== MonsterCard.class)
            if(((MonsterCard) opponentCard).getAttackRace().getClass()== DragonRace.class)
                return 0;

        return thisCard.getDamage();
    }
}