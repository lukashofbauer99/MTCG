package Domain.User.Interfaces;

import Domain.IRepository;
import Model.User.Deck;

public interface IDeckRepository extends IRepository<Deck, Long> {
    Deck findDeckOfUser(String Username);
}
