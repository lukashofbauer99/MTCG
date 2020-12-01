package UnitTests.Model.User;

import Model.Cards.CardPacks.PackType;
import Model.User.PlayerHub;
import Model.User.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TestBuyCardPack {
    PlayerHub playerHub= new PlayerHub();

    @Mock
    User userA;

    @Test
    @DisplayName("Buy CardPack")
    void testBuyCardPack() {
        // arrange
        userA= new User(playerHub);

        // act
        userA.buyCardPackage(PackType.Normal);

        // assert
        //verify(playerHub).buyCards(userA,PackType.Normal);
        assertEquals(15,userA.getCoins());
        assertEquals(4,userA.getStack().getCards().size());
    }

    @Test
    @DisplayName("Buy CardPack Fail not enough coins")
    void testBuyCardPackFail() {
        // arrange

        userA=new User(playerHub);
        userA.setCoins(3);

        // act
        userA.buyCardPackage(PackType.Normal);


        // assert
        assertEquals(3,userA.getCoins());
        assertEquals(0,userA.getStack().getCards().size());
    }

}