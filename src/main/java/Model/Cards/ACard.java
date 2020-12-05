package Model.Cards;

import Model.Battle.State;
import Model.Cards.Effects_Races.Effects.AEffect;
import Model.Cards.Effects_Races.Effects.IEffect;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public abstract class ACard {

    String id;
    String name;
    int damage;

    IEffect attackEffect;

    public ACard(String name, int damage, IEffect attackEffect, IEffect defendEffect) {
        this.name = name;
        this.damage = damage;
        this.attackEffect = attackEffect;
        this.defendEffect = defendEffect;
    }

    IEffect defendEffect;


    public abstract int calcDamage(State state, ACard oppenentCard);
}
