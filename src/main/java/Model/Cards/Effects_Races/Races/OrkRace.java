package Model.Cards.Effects_Races.Races;

import Model.Battle.State;
import Model.Cards.ACard;
import Model.Cards.MonsterCard;

public class OrkRace extends ABaseRace {
    public OrkRace(IRace race) {
        super.base=race;
        super.name = "ork";
    }

    @Override
    public double affect(ACard thisCard, ACard opponentCard, State state) {

        if(state == State.ATTACK) {
            if (opponentCard.getClass() == MonsterCard.class)
                if (((MonsterCard) opponentCard).getRace().getClass() == WizardRace.class)
                    return 0;
        }

        return thisCard.getDamage();
    }
}