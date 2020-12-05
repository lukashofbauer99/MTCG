package Model.Cards.CardPacks;

import Model.Cards.ACard;

import java.util.List;

public interface ICardPack {

   List<ACard> getCards();
   int getCosts();
   PackType getPackType();
}
