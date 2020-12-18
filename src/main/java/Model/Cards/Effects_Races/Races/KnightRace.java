package Model.Cards.Effects_Races.Races;

import Model.Battle.State;
import Model.Cards.ACard;
import Model.Cards.Effects_Races.Effects.WaterEffect;
import Model.Cards.SpellCard;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class KnightRace extends ABaseRace {
    public KnightRace(IRace race) {
        super.base = race;
        super.name = "knight";
    }

    @Override
    public double affect(ACard thisCard, ACard opponentCard, State state) {

        if (opponentCard.getClass() == SpellCard.class)
            if (opponentCard.getEffect().getClass() == WaterEffect.class)
                return 0;

        return thisCard.getDamage();
    }
}