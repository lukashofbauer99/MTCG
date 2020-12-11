package Model.Cards.CardPacks;

import Model.Cards.ACard;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class ACardPackFixedSizeAndCost implements ICardPack {

    @Setter
    protected Long id;
    protected int cardAmount;
    protected int costs;
    protected List<ACard> cards = new ArrayList<>();

    public ACardPackFixedSizeAndCost(int costs, int cardAmount) {
        this.costs = costs;
        this.cardAmount = cardAmount;
    }
}
