package Model.Cards.Effects_Races.Races;

import Model.Battle.State;
import Model.Cards.ACard;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class KrakenRace extends ABaseRace {
    public KrakenRace(IRace race) {
        super.base = race;
        super.name = "kraken";
    }

    @Override
    public double affect(ACard thisCard, ACard opponentCard, State state) {
        return thisCard.getDamage();
    }
}