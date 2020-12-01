package Model.Cards.Effects_Races.Races;

import Model.Cards.ACard;
import lombok.Getter;

@Getter
public abstract class ABaseRace implements IRace {

    protected String name;
    protected IRace base;

    public abstract int affect(ACard thisCard, ACard opponentCard);
}