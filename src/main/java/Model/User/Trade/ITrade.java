package Model.User.Trade;

import Model.Cards.ACard;
import Model.User.User;

public interface ITrade {

    boolean trade(User user, ACard card);
}
