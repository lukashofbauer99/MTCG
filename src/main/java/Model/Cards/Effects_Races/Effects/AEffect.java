package Model.Cards.Effects_Races.Effects;

import Model.Cards.ACard;
import lombok.Getter;

@Getter
public abstract class AEffect implements IEffect { //DECORATOR PATTERN

    protected String name;
    protected IEffect base;

    public abstract int affect(ACard thisCard, ACard opponentCard);
}
