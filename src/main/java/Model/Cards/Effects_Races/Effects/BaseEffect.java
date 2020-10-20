package Model.Cards.Effects_Races.Effects;

import Model.Cards.ACard;

public class BaseEffect extends AEffect {

    public BaseEffect() {
        super.name = "baseEffect";
    }

    @Override
    public int affect(ACard thisCard, ACard opponentCard) {
        return thisCard.getDamage();
    }
}
