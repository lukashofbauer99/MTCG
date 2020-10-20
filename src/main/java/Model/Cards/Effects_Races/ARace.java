package Model.Cards.Effects_Races;

import Model.Cards.ACard;
public abstract class ARace extends AEffect {

    @Override
    public abstract int affect(ACard thisCard,ACard opponentCard);
}