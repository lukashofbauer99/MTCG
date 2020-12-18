package Model.Cards.CardPacks;

import Model.Cards.ACard;

import java.util.List;

public interface ICardPack {

    Long getId();

    void setId(Long id);

    List<ACard> getCards();

    int getCosts();

    PackType getPackType();
}
