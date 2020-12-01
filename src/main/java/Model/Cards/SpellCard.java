package Model.Cards;

import Model.Battle.State;
import Model.Cards.Effects_Races.Effects.AEffect;
import Model.Cards.Effects_Races.Effects.IEffect;

public class SpellCard extends ACard {

    public SpellCard(String name, int damage, IEffect effect) {
        super(name, damage, effect,effect);
    }
    public SpellCard(String name, int damage, AEffect attackEffect,AEffect defendEffect) {
        super(name, damage, attackEffect,defendEffect);
    }

    @Override
    public int calcDamage(State state, ACard oppenentCard) {

        if (state==State.ATTACK) {
            return this.attackEffect.affect(this,oppenentCard);
        }
        else
            {
                if (state==State.DEFEND) {
                    return this.defendEffect.affect(this,oppenentCard);
                }
                else
                    throw new IllegalArgumentException() ; //no allowed State
            }

    }
}
