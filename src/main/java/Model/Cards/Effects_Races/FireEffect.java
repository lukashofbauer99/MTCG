package Model.Cards.Effects_Races;

import Model.Cards.ACard;
import net.bytebuddy.implementation.bytecode.member.MethodInvocation;

public class FireEffect extends AEffect {

    public FireEffect() {
        super.name = "FireEffect";
    }

    @Override
    public int affect(ACard thisCard,ACard opponentCard) {
        if(opponentCard.getEffect().getClass()== WaterEffect.class)
            return super.base.affect(thisCard,opponentCard)/2;
        else
            return thisCard.getDamage();
    }
}
