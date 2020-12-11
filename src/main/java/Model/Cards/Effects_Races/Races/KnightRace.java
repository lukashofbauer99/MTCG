package Model.Cards.Effects_Races.Races;

import Model.Battle.State;
import Model.Cards.ACard;
import Model.Cards.Effects_Races.Effects.WaterEffect;
import Model.Cards.MonsterCard;
import Model.Cards.SpellCard;

public class KnightRace extends ABaseRace {
    public KnightRace(IRace race) {
        super.base=race;
        super.name = "goblin";
    }

    @Override
    public double affect(ACard thisCard, ACard opponentCard, State state) {

        if(opponentCard.getClass()== SpellCard.class)
            if(((SpellCard) opponentCard).getAttackEffect().getClass()== WaterEffect.class | ((SpellCard) opponentCard).getDefendEffect().getClass()== WaterEffect.class )
                return 0;

        return thisCard.getDamage();
    }
}