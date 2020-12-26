package UnitTests.Model.User;

import Model.Cards.CardPacks.AutoGenCardPack;
import Model.Cards.CardPacks.PackType;
import Model.Cards.Vendor.IVendor;
import Model.Cards.Vendor.NormalVendor;
import Domain.PlayerHub;
import Model.User.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TestBuyCardPack {
    PlayerHub playerHub = new PlayerHub();

    IVendor vendor = new NormalVendor();

    User userA = new User();

    @Test
    @DisplayName("Buy CardPack")
    void testBuyCardPack() {
        // arrange
        vendor.getAvailibleCardPacks().add(new AutoGenCardPack());
        userA.setCoins(20);

        // act
        userA.buyCardPackage(PackType.AutoGen, vendor);

        // assert
        //verify(playerHub).buyCards(userA,PackType.Normal);
        assertEquals(15, userA.getCoins());
        assertEquals(4, userA.getStack().getCards().size());
    }

    @Test
    @DisplayName("Buy CardPack Fail not enough coins")
    void testBuyCardPackFail() {
        // arrange

        vendor.getAvailibleCardPacks().add(new AutoGenCardPack());
        userA.setCoins(3);

        // act
        userA.buyCardPackage(PackType.AutoGen, vendor);


        // assert
        assertEquals(3, userA.getCoins());
        assertEquals(0, userA.getStack().getCards().size());
    }

}