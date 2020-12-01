package Model.Cards.Effects_Races.Effects;

import Model.Cards.ACard;

public class FireEffect extends AEffect {

    public FireEffect(IEffect effect) {
        super.base=effect;
        super.name = "FireEffect";
    }

    @Override
    public int affect(ACard thisCard, ACard opponentCard) {
        if(opponentCard.getAttackEffect().getClass()== WaterEffect.class)
            return super.base.affect(thisCard,opponentCard)/2;
        else
            return thisCard.getDamage();
    }
}
