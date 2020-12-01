package Model.Cards.Effects_Races.Races;

import Model.Cards.ACard;

public interface IRace {
    int affect(ACard thisCard, ACard opponentCard);
    IRace getBase();
}
