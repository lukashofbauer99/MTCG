package UnitTests.Model.Cards.CardPackage;

import Model.Cards.CardPacks.ACardPackFixedSizeAndCost;
import Model.Cards.CardPacks.NormalCardPack;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TestGenerateCardPack {

    ACardPackFixedSizeAndCost cardPackA;

    @Test
    @DisplayName("Generate Normal CardPack")
    void TestGenerateNormalCardPack() {
        // arrange

        cardPackA= new NormalCardPack();

        // act

        //cardPackA.genereateCards(); //gets called in constructor

        // assert
        assertEquals(4,cardPackA.getCards().size());
    }
}
