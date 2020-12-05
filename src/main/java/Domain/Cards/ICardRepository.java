package Domain.Cards;

import Domain.IRepository;
import Model.Cards.ACard;

import java.util.List;

public interface ICardRepository extends IRepository<ACard, Long> {

    List<ACard> findCardsOfUser(String Username);
}
