package Model.Cards;

import Model.Battle.State;
import Model.Cards.Effects_Races.Effects.AEffect;

public class SpellCard extends ACard {

    public SpellCard(String name, int damage, AEffect effect) {
        super(name, damage, effect,effect);
    }
    public SpellCard(String name, int damage, AEffect attackEffect,AEffect defendEffect) {
        super(name, damage, attackEffect,defendEffect);
    }

    @Override
    public int calcDamage(State state, ACard oppenentCard) {
        return 0;
    }
}
