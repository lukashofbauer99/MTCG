package Model.Cards;

import Model.Cards.ACard;
import lombok.Data;

@Data
public class SpellCard extends ACard {

    @Override
    public int calcDamage(ACard oppenentCard) {
        return 0;
    }
}
