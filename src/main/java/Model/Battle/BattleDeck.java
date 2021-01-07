package Model.Battle;

import Model.Cards.ACard;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
public class BattleDeck {

    Long id;
    List<ACard> cards = new ArrayList<>();
    @JsonIgnore
    Random rand = new Random();


    public ACard drawCard() {
        int index = 0;
        if (cards.size() != 1)
            index = rand.nextInt(cards.size() - 1);
        ACard drawnCard = cards.get(index);
        cards.remove(drawnCard);
        return drawnCard;
    }
}
