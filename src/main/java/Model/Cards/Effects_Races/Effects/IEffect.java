package Model.Cards.Effects_Races.Effects;

import Model.Cards.ACard;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

//region JsonConfig
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = NormalEffect.class, name = "Normal"),
        @JsonSubTypes.Type(value = FireEffect.class, name = "Fire"),
        @JsonSubTypes.Type(value = WaterEffect.class, name = "Water"),
        @JsonSubTypes.Type(value = BaseEffect.class, name = "Base")
}
)
//endregion
public interface IEffect {

    Long getId();

    void setId(Long id);

    String getName();

    void setName(String name);

    double affect(ACard thisCard, ACard opponentCard);

    IEffect getBase();

    void setBase(IEffect base);
}
