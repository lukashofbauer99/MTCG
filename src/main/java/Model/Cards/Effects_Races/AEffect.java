package Model.Cards.Effects_Races;

import Model.Cards.ACard;

public abstract class AEffect { //DECORATOR PATTERN

    protected String name;
    protected AEffect base;

    public abstract int affect(ACard thisCard,ACard opponentCard);
}
