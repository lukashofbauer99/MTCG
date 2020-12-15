package Model.Cards.Effects_Races.Effects;

import Model.Cards.ACard;
import Model.Cards.Effects_Races.Races.KrakenRace;
import Model.Cards.MonsterCard;
import Model.Cards.SpellCard;
import lombok.NoArgsConstructor;

public class BaseEffect extends AEffect {

    public BaseEffect() {
        super.name = "base";
    }

    @Override
    public double affect(ACard thisCard, ACard opponentCard) {
        if(thisCard.getClass()== SpellCard.class&&opponentCard.getClass()== MonsterCard.class)
        {
            if(((MonsterCard) opponentCard).getRace().getClass()==KrakenRace.class)
            {
                return 0;
            }
        }
        return thisCard.getDamage();
    }
}
