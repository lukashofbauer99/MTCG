package Model.User.Trade;

import Model.Cards.ACard;
import Model.Cards.MonsterCard;
import Model.Cards.SpellCard;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

//region JsonConfig
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = NormalTradeCardRequirements.class, name = "NormalTradeCardRequirements")
}
)
//endregion
public interface ITradeCardRequirements {
    boolean evaluateRequirements(ACard card);
}
