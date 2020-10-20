package Model.Cards.Effects_Races;

import Model.Cards.ACard;

public class WaterEffect extends AEffect {

    public WaterEffect() {
        super.name = "WaterEffect";
    }

    @Override
    public int affect(ACard thisCard,ACard opponentCard) {
        if(opponentCard.getEffect().getClass()== FireEffect.class)
            return super.base.affect(thisCard,opponentCard)*2;
        else
            return thisCard.getDamage();
    }
}
