package Model.Cards.Effects_Races.Effects;

import Model.Cards.ACard;

public class WaterEffect extends AEffect {

    public WaterEffect(AEffect effect) {
        super.base= effect;
        super.name = "WaterEffect";
    }

    @Override
    public int affect(ACard thisCard, ACard opponentCard) {
        if(opponentCard.getAttackEffect().getClass()== FireEffect.class)
            return super.base.affect(thisCard,opponentCard)*2;
        else
            return thisCard.getDamage();
    }
}
