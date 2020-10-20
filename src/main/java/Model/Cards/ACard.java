package Model.Cards;

import Model.Cards.Effects_Races.AEffect;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public abstract class ACard {

    String name;
    int damage;

    AEffect effect;
    public abstract int calcDamage(ACard oppenentCard);
}
