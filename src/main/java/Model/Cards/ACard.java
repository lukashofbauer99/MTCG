package Model.Cards;

import Model.Battle.State;
import Model.Cards.Effects_Races.Effects.IEffect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//region JsonConfig
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MonsterCard.class, name = "Monster"),
        @JsonSubTypes.Type(value = SpellCard.class, name = "Spell")}
)
//endregion
public abstract class ACard {

    String id;
    String name;
    double damage;
    IEffect effect;


    public ACard(String name, double damage, IEffect effect) {
        this.name = name;
        this.damage = damage;
        this.effect = effect;
    }

    public ACard(String id,String name, double damage) {
        this.id=id;
        this.name = name;
        this.damage = damage;
    }

    public abstract double calcDamage(State state, ACard oppenentCard);
}
