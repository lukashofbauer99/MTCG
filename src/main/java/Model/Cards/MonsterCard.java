package Model.Cards;

import lombok.Data;

@Data
public class MonsterCard extends ACard {
    Specialty specialty;

    @Override
    public void searchBattle() {

    }
}
