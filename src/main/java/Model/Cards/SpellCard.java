package Model.Cards;

import Model.Cards.ACard;
import Model.Cards.Effects_Races.AEffect;
import lombok.Data;

public class SpellCard extends ACard {

    public SpellCard(String name, int damage, AEffect effect) {
        super(name, damage, effect);
    }

    @Override
    public int calcDamage(ACard oppenentCard) {
        return 0;
    }
}
