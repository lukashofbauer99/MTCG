package UnitTests.Model.User;

import Model.Cards.ACard;
import Model.User.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TestDefineDeck {
    User userA= new User();

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

    @Test
    @DisplayName("DefineDeck")
    void testDefineDeck() {
        // arrange

        userA.getDeck().setCards(cards);
        userA.getStack().getCards().add(cardA);
        userA.getStack().getCards().add(cardB);
        userA.getStack().getCards().add(cardC);
        userA.getStack().getCards().add(cardD);


        // act
        userA.defineDeck(0,1,2,3);

        // assert
        verify(cards).add(cardA);
        verify(cards).add(cardB);
        verify(cards).add(cardC);
        verify(cards).add(cardD);
    }

}