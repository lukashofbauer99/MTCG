package Model.User.Trade;

import Model.Cards.ACard;
import Model.Cards.Effects_Races.Effects.IEffect;
import Model.Cards.Effects_Races.Races.IRace;
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

    double getMinimumDamage();
    void setMinimumDamage(double minimumDamage);

    Class getCardType();
    void setCardType(Class cl);

    IEffect getEffect();
    void setEffect(IEffect effect);

    IRace getRace();
    void setRace(IRace race);

}
