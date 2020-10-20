package UnitTests.User;

import Model.Cards.ACard;
import Model.Cards.MonsterCard;
import Model.User.Stack;
import Model.User.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestTradeCard {
    User userA;
    @Mock
    List<ACard> cardsA;
    @Mock
    MonsterCard monsterCardA;

    User userB;
    @Mock
    List<ACard> cardsB;
    @Mock
    MonsterCard monsterCardB;

    @Test
    @DisplayName("Trade Card")
    void testTradeCard() {
        // arrange
        userA = new User();
        userB = new User();

        cardsA.add(monsterCardA);
        cardsB.add(monsterCardB);

        userA.getStack().setCards(cardsA);
        userB.getStack().setCards(cardsB);

        // act
        userA.tradeCard(userB,monsterCardA,monsterCardB);

        // assert
        verify(cardsA).add(monsterCardB);
        verify(cardsA).remove(monsterCardA);
        verify(cardsB).add(monsterCardA);
        verify(cardsB).remove(monsterCardB);
    }

}