package UnitTests.User;

import Model.User.PlayerHub;
import Model.User.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TestBuyCardPack {
    @Mock
    PlayerHub playerHub;
    @Mock
    User userA= new User(playerHub);

    @Test
    @DisplayName("Buy CardPack")
    void testBuyCardPack() {
        // arrange

        // act
        userA.buyCardPackage();

        // assert
        verify(playerHub).buyCards(userA);
        verify(userA).setCoins(userA.getCoins()-5);
    }

    @Test
    @DisplayName("Buy CardPack Fail not enough coins")
    void testBuyCardPackFail() {
        // arrange

        userA.setCoins(3);

        // act
        userA.buyCardPackage();


        // assert
        verify(playerHub).buyCards(userA);
        verify(userA,never()).setCoins(userA.getCoins()-5);
    }

}