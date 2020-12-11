package Model.Cards.Effects_Races.Effects;

import Model.Cards.ACard;
import Model.Cards.Effects_Races.Races.KrakenRace;
import Model.Cards.MonsterCard;
import Model.Cards.SpellCard;

public class WaterEffect extends AEffect {

    public WaterEffect(IEffect effect) {
        super.base= effect;
        super.name = "water";
    }

    @Override
    public double affect(ACard thisCard, ACard opponentCard) {
        if(thisCard.getClass()== SpellCard.class&&opponentCard.getClass()== MonsterCard.class)
        {
            if(((MonsterCard) opponentCard).getRace().getClass()== KrakenRace.class)
            {
                return 0;
            }
        }
        if(opponentCard.getAttackEffect().getClass()== FireEffect.class)
            return super.base.affect(thisCard,opponentCard)*2;
        if(opponentCard.getAttackEffect().getClass()== NormalEffect.class)
            return super.base.affect(thisCard,opponentCard)/2;

        return thisCard.getDamage();
    }
}
