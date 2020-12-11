package Model.Cards.Effects_Races.Effects;

import Model.Cards.ACard;

public interface IEffect  {

    Long getId();
    void setId(Long id);
    String getName();
    double affect(ACard thisCard, ACard opponentCard);

    IEffect getBase();
}
