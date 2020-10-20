package Model.Cards;

import Model.Battle.State;
import Model.Cards.Effects_Races.Effects.AEffect;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public abstract class ACard {

    String name;
    int damage;

    AEffect attackEffect;
    AEffect defendEffect;
    public abstract int calcDamage(State state, ACard oppenentCard);
}
