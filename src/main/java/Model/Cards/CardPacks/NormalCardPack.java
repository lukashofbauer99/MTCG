package Model.Cards.CardPacks;

import Model.Cards.ACard;
import Model.Cards.Effects_Races.Effects.BaseEffect;
import Model.Cards.Effects_Races.Races.BaseRace;
import lombok.Getter;

import java.util.List;
import java.util.Random;

@Getter
public class NormalCardPack extends ACardPackFixedSizeAndCost {

    PackType packType= PackType.Normal;

    Random rand = new Random();

    public NormalCardPack(List<ACard> cards) { //TODO check in serevice layer if card.size()==5
        super(5, 5);
        super.cards = cards;

    }

    BaseEffect baseEffect= new BaseEffect();
    BaseRace baseRace = new BaseRace();

}
