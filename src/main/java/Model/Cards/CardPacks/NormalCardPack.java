package Model.Cards.CardPacks;

import Model.Cards.ACard;

import java.util.ArrayList;
import java.util.List;

public class NormalCardPack implements ICardPack {

    int cardAmount=5;
    List<ACard> Cards = new ArrayList<>();

    @Override
    public void genereateCards() {
        //generate Cards
    }
}
