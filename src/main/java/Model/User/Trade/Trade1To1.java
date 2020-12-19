package Model.User.Trade;

import Model.Cards.ACard;
import Model.User.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Trade1To1 implements ITrade {

    String id;
    User userOffer;
    ACard cardTradedFor;
    ITradeCardRequirements requirements;

    @Override
    public boolean trade(User user, ACard card) {
        if(user!=userOffer) {
            if (requirements.evaluateRequirements(card)) {
                if (userOffer.getStack().getCards().contains(cardTradedFor) && user.getStack().getCards().contains(card)) {
                    userOffer.getStack().getCards().remove(cardTradedFor);
                    userOffer.getStack().getCards().add(card);
                    user.getStack().getCards().remove(card);
                    user.getStack().getCards().add(cardTradedFor);
                    return true;
                }
            }
        }
        return false;

    }
}
