package Model.User.Trade;

import Model.Cards.ACard;
import Model.User.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static java.util.stream.Collectors.toList;

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
        if(user.getId()!=userOffer.getId()) {
            if (requirements.evaluateRequirements(card)) {
                if (userOffer.getStack().getCards().stream().map(x->x.getId()).collect(toList()).contains(cardTradedFor.getId())
                        && user.getStack().getCards().stream().map(x->x.getId()).collect(toList()).contains(card.getId())) {
                    userOffer.getStack().getCards().removeIf(x->x.getId().equals(cardTradedFor.getId()));
                    userOffer.getStack().getCards().add(card);
                    user.getStack().getCards().removeIf(x->x.getId().equals(card.getId()));
                    user.getStack().getCards().add(cardTradedFor);
                    return true;
                }
            }
        }
        return false;

    }
}
