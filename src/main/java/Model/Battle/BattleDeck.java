package Model.Battle;

import Model.Cards.ACard;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
public class BattleDeck {

    List<ACard> cards ;
    Random rand = new Random();

    public BattleDeck(List<ACard> cards) {
        this.cards = cards;
    }

    public ACard drawCard() {
        int index=0;
        if(cards.size()!=1)
            index = rand.nextInt(cards.size()-1);
        ACard drawnCard = cards.get(index);
        cards.remove(drawnCard);
        return drawnCard;
    }
}