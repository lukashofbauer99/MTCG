package Model.User.Trade;

import Model.Cards.ACard;
import Model.Cards.MonsterCard;
import Model.Cards.SpellCard;
import Model.User.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

//region JsonConfig
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Trade1To1.class, name = "Trade1To1")
}
)
//endregion
public interface ITrade {

    User getUserOffer();
    void setUserOffer(User user);
    ACard getCardTradedFor();
    void setCardTradedFor(ACard cardTradedFor);
    ITradeCardRequirements getRequirements();
    void setRequirements(ITradeCardRequirements tradeCardRequirements);
    String getId();
    void setId(String id);
    boolean trade(User user, ACard card);
}
