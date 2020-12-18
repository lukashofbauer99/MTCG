package Model.Cards;

import Model.Battle.State;
import Model.Cards.Effects_Races.Effects.AEffect;
import Model.Cards.Effects_Races.Effects.IEffect;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class SpellCard extends ACard {

    public SpellCard(String name, double damage, IEffect effect) {
        super(name, damage, effect);
    }

    public SpellCard(String name, double damage, AEffect attackEffect, AEffect defendEffect) {
        super(name, damage, attackEffect);
    }

    @Override
    public double calcDamage(State state, ACard oppenentCard) {
        return this.effect.affect(this, oppenentCard);
    }

}
