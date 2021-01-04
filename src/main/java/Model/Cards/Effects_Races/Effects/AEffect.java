package Model.Cards.Effects_Races.Effects;

import Model.Cards.ACard;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AEffect implements IEffect { //DECORATOR PATTERN

    @Setter
    Long id;
    protected String name;
    protected IEffect base;

    public abstract double affect(ACard thisCard, ACard opponentCard);
}
