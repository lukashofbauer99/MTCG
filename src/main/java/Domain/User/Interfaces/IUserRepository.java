package Domain.User.Interfaces;

import Domain.IRepository;
import Model.Cards.ACard;
import Model.User.Credentials;
import Model.User.Deck;
import Model.User.User;

import java.util.List;

public interface IUserRepository extends IRepository<User, Long> {

    String loginUser(Credentials cred);
    List<ACard> getCardsOfUserWithToken(String token);
    Deck getDeckOfUserWithToken(String token);
    User getUserWithToken(String token);

}
