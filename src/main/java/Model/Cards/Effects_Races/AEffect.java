package Model.Cards.Effects_Races;

import Model.Cards.ACard;
import lombok.Getter;

@Getter
public abstract class AEffect { //DECORATOR PATTERN

    protected String name;  //ENUM
    protected AEffect base;

    public abstract int affect(ACard thisCard,ACard opponentCard);
    //affectAttack und affectDefend
}
