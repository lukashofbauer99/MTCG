package Model.User.Trade;

import Model.Cards.ACard;
import Model.User.User;

public interface ITrade {

    String getId();
    void setId(String id);
    boolean trade(User user, ACard card);
}
