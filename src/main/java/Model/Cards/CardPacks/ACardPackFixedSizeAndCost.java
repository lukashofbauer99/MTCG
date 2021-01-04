package Model.Cards.CardPacks;

import Model.Cards.ACard;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public abstract class ACardPackFixedSizeAndCost implements ICardPack {

    protected Long id;
    protected int cardAmount;
    protected int costs;
    protected List<ACard> cards = new ArrayList<>();
    protected  PackType packType;

    public ACardPackFixedSizeAndCost(int costs, int cardAmount,PackType packType) {
        this.costs = costs;
        this.cardAmount = cardAmount;
        this.packType=packType;
    }
}
