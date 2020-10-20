package UnitTests.Cards.CardPackage;

import Model.Cards.ACard;
import Model.Cards.CardPacks.ACardPack;
import Model.Cards.CardPacks.NormalCardPack;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TestGenerateCardPack {

    ACardPack cardPackA;

    @Test
    @DisplayName("Generate Normal CardPack")
    void TestGenerateNormalCardPack() {
        // arrange

        cardPackA= new NormalCardPack();

        // act

        cardPackA.genereateCards();

        // assert
        assertEquals(cardPackA.getCards().size(),4);
    }
}
