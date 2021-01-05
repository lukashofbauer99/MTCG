package UnitTests.Model.User;

import Model.Cards.ACard;
import Model.Cards.SpellCard;
import Model.User.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TestDefineDeck {
    User userA = new User();

    @Mock
    List<ACard> cards;

    @Mock
    ACard cardA;
    @Mock
    ACard cardB;
    @Mock
    ACard cardC;
    @Mock
    ACard cardD;
    @Mock
    ACard cardE;

    @Test
    @DisplayName("DefineDeck")
    void testDefineDeck() {
        // arrange

        userA.getStack().getCards().add(cardA);
        userA.getStack().getCards().add(cardB);
        userA.getStack().getCards().add(cardC);
        userA.getStack().getCards().add(cardD);

        List<ACard> cardstoAdd = new ArrayList<>();
        cards.add(cardA);
        cards.add(cardB);
        cards.add(cardC);
        cards.add(cardD);

        // act
        userA.defineDeck(cardstoAdd);

        // assert
        verify(cards).add(cardA);
        verify(cards).add(cardB);
        verify(cards).add(cardC);
        verify(cards).add(cardD);
    }

    @Test
    @DisplayName("DefineDeck card not owned")
    void testDefineDeckFail() {
        // arrange

        ACard cardE= new SpellCard();
        userA.getStack().getCards().add(cardE);
        cardE.setId("1");

        ACard cardF= new SpellCard();
        userA.getStack().getCards().add(cardF);
        cardF.setId("2");

        ACard cardG= new SpellCard();
        userA.getStack().getCards().add(cardG);
        cardG.setId("3");



        List<ACard> cardstoAdd = new ArrayList<>();
        cardstoAdd.add(cardE);
        cardstoAdd.add(cardF);
        cardstoAdd.add(cardG);

        // act
        boolean worked = userA.defineDeck(cardstoAdd);

        // assert
        assertFalse(worked);
    }

}