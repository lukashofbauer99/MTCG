package Model.Cards.Effects_Races.Races;

import Model.Battle.State;
import Model.Cards.ACard;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
public abstract class ABaseRace implements IRace {

    @Setter
    Long id;
    protected String name;
    protected IRace base;

    public abstract double affect(ACard thisCard, ACard opponentCard, State state);
}