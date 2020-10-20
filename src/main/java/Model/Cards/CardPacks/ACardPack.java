package Model.Cards.CardPacks;

import Model.Cards.ACard;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public abstract class ACardPack {

    public ACardPack(int costs, int cardAmount) {
        this.costs = costs;
        this.cardAmount = cardAmount;
    }
    protected int cardAmount;

    @Getter
    protected int costs;
    @Getter
    protected List<ACard> Cards = new ArrayList<>();


    public abstract void genereateCards();
}
