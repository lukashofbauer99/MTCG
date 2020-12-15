package Model.Cards.Effects_Races.Races;

import Model.Battle.State;
import Model.Cards.ACard;
import Model.Cards.MonsterCard;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class WizardRace extends ABaseRace {
    public WizardRace(IRace race) {
        super.base=race;
        super.name = "wizard";
    }

    @Override
    public double affect(ACard thisCard, ACard opponentCard, State state) {

        return thisCard.getDamage();
    }
}