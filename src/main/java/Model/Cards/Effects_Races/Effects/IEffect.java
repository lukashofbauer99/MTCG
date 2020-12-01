package Model.Cards.Effects_Races.Effects;

import Model.Cards.ACard;

public interface IEffect  {

    int affect(ACard thisCard, ACard opponentCard);

    IEffect getBase();
}
