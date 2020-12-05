package Model.User.Trade;

import Model.Cards.ACard;
import Model.User.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Trade1To1 implements ITrade {

    User userOffer;
    ACard cardTradedFor;
    ITradeCardRequirements requirements;

    @Override
    public boolean trade(User user, ACard card) {
        if(requirements.evaluateRequirements(card)) {
            userOffer.getStack().getCards().remove(cardTradedFor);
            userOffer.getStack().getCards().add(card);

            user.getStack().getCards().remove(card);
            user.getStack().getCards().add(cardTradedFor);
            return true;
        }
        return false;

    }
}
