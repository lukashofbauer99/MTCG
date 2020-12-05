package UnitTests.Model.Cards.CardPackage;

import Model.Cards.CardPacks.AutoGenCardPack;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TestGenerateCardPack {

    AutoGenCardPack cardPackA;

    @Test
    @DisplayName("Generate AutoGen CardPack")
    void TestGenerateNormalCardPack() {
        // arrange

        cardPackA= new AutoGenCardPack();

        // act


        // assert
        assertEquals(4,cardPackA.getCards().size());
    }
}
