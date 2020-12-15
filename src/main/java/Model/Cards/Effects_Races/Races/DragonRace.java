package Model.Cards.Effects_Races.Races;

import Model.Battle.State;
import Model.Cards.ACard;
import Model.Cards.MonsterCard;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DragonRace extends ABaseRace {
    public DragonRace(IRace race) {
        super.base = race;
        super.name = "dragon";
    }

    @Override
    public double affect(ACard thisCard, ACard opponentCard, State state) {
        if(state == State.ATTACK) {
            if (opponentCard.getClass() == MonsterCard.class)
                if (((MonsterCard) opponentCard).getRace().getClass() == FireElfRace.class)
                    return 0;
        }

        return thisCard.getDamage();
    }
}