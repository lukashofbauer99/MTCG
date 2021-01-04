package Model.Cards.CardPacks;

import Model.Cards.ACard;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Random;

@Getter
@NoArgsConstructor
public class NormalCardPack extends ACardPackFixedSizeAndCost {

    Random rand = new Random();

    public NormalCardPack(List<ACard> cards) {
        super(5, 5,PackType.Normal);
        super.cards = cards;

    }

}
