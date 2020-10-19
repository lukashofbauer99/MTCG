package Model.Cards;

import lombok.Data;

@Data
public abstract class ACard {
    String name;
    int damage;

    IEffect effect;
    public abstract void searchBattle();
}
