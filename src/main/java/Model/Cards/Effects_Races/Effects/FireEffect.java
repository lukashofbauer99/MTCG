package Model.Cards.Effects_Races.Effects;

import Model.Cards.ACard;
import Model.Cards.Effects_Races.Races.KrakenRace;
import Model.Cards.MonsterCard;
import Model.Cards.SpellCard;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FireEffect extends AEffect {

    public FireEffect(IEffect effect) {
        super.base = effect;
        super.name = "fire";
    }

    @Override
    public double affect(ACard thisCard, ACard opponentCard) {
        if (thisCard.getClass() == SpellCard.class && opponentCard.getClass() == MonsterCard.class) {
            if (((MonsterCard) opponentCard).getRace().getClass() == KrakenRace.class) {
                return 0;
            }
        }

        if (opponentCard.getEffect().getClass() == NormalEffect.class)
            return super.base.affect(thisCard, opponentCard) * 2;
        if (opponentCard.getEffect().getClass() == WaterEffect.class)
            return super.base.affect(thisCard, opponentCard) / 2;
        if (opponentCard.getEffect().getClass() == IceEffect.class)
            return super.base.affect(thisCard, opponentCard) *4;

        return thisCard.getDamage();
    }
}
