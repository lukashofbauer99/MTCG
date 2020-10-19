package Model.Cards;

import Model.Cards.Effects_Races.ARace;
import lombok.Data;

@Data
public class MonsterCard extends ACard {
    ARace race;

    @Override
    public int calcDamage(ACard oppenentCard) {
        return 0;
    }
}
