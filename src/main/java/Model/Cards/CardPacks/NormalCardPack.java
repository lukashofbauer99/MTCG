package Model.Cards.CardPacks;

import Model.Cards.ACard;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class NormalCardPack extends ACardPack {

    //ENUM CARDPACKS
    List<ACard> Cards = new ArrayList<>();

    public NormalCardPack() {
        super(5, 5);
        genereateCards();
    }

    @Override
    public void genereateCards() {
        //generate Cards
    }
}
