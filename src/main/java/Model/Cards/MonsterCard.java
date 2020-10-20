package Model.Cards;

import Model.Cards.Effects_Races.AEffect;
import Model.Cards.Effects_Races.ARace;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class MonsterCard extends ACard {
    ARace race;

    public MonsterCard(String name, int damage, AEffect effect,ARace race) {
        super(name, damage, effect);
        this.race=race;
    }

    @Override
    public int calcDamage(ACard oppenentCard) {
        return 0;
    }
}
