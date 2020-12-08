package Domain.User.Interfaces;

import Domain.IRepository;
import Model.User.Deck;
import Model.User.Stack;

public interface IStackRepository extends IRepository<Stack, Long> {
    Deck findDeckOfUser(String Username);
}
