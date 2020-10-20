package Model.Cards.CardPacks;

public abstract class ACardPack {

    public ACardPack(int costs, int cardAmount) {
        this.costs = costs;
        this.cardAmount = cardAmount;
    }
    protected int costs;
    protected int cardAmount;


    public abstract void genereateCards();
}
