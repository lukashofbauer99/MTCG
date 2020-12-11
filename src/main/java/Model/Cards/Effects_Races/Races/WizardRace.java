package Model.Cards.Effects_Races.Races;

import Model.Battle.State;
import Model.Cards.ACard;
import Model.Cards.MonsterCard;

public class WizardRace extends ABaseRace {
    public WizardRace(IRace race) {
        super.base=race;
        super.name = "wizard";
    }

    @Override
    public double affect(ACard thisCard, ACard opponentCard, State state) {

        if(opponentCard.getClass()== MonsterCard.class)
            if(((MonsterCard) opponentCard).getRace().getClass()== DragonRace.class)
                return 0;

        return thisCard.getDamage();
    }
}