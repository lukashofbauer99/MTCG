package Model.Cards.Effects_Races.Races;

import Model.Battle.State;
import Model.Cards.ACard;

public interface IRace {

    Long getId();
    void setId(Long id);
    String getName();
    double affect(ACard thisCard, ACard opponentCard, State state);
    IRace getBase();
}
