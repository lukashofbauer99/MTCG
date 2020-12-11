package Model.Cards;

import Model.Battle.State;
import Model.Cards.Effects_Races.Effects.AEffect;
import Model.Cards.Effects_Races.Effects.BaseEffect;
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
//region JsonConfig
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MonsterCard.class, name = "Monster"),

        @JsonSubTypes.Type(value = SpellCard.class, name = "Spell") }
)
//endregion
public abstract class ACard {

    @Setter
    String id;
    String name;
    double damage;

    //Example water shield and fire sword
    @Setter
    IEffect attackEffect= new BaseEffect();
    @Setter
    IEffect defendEffect= new BaseEffect(); //TODO: ENTWEDER State bei affect hinzufügen oder defenseEffect entfernen


    public ACard(String name, double damage, IEffect attackEffect, IEffect defendEffect) {
        this.name = name;
        this.damage = damage;
        this.attackEffect = attackEffect;
        this.defendEffect = defendEffect;
    }

    public abstract double calcDamage(State state, ACard oppenentCard);
}
