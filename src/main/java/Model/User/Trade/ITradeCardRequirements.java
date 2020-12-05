package Model.User.Trade;

import Model.Cards.ACard;

public interface ITradeCardRequirements {
    boolean evaluateRequirements(ACard card);
}
