package UnitTests.Model.User;

import Model.Cards.ACard;
import Model.Cards.Effects_Races.Effects.BaseEffect;
import Model.Cards.Effects_Races.Effects.FireEffect;
import Model.Cards.Effects_Races.Effects.IEffect;
import Model.Cards.Effects_Races.Effects.WaterEffect;
import Model.Cards.Effects_Races.Races.*;
import Model.Cards.MonsterCard;
import Model.User.Trade.ITrade;
import Model.User.Trade.ITradeCardRequirements;
import Model.User.Trade.NormalTradeCardRequirements;
import Model.User.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TestTrade1To1Card {
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

    IEffect baseEffect = new BaseEffect();
    IRace baseRace = new BaseRace();
    ACard cardC = new MonsterCard("Dragon", 10, new FireEffect(baseEffect), new DragonRace(baseRace));
    ACard cardD = new MonsterCard("Water Goblin", 100, new WaterEffect(baseEffect), new GoblinRace(baseRace));

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
        userA.tradeCard(userB, monsterCardA, monsterCardB);

        // assert
        verify(cardsA).add(monsterCardB);
        verify(cardsA).remove(monsterCardA);
        verify(cardsB).add(monsterCardA);
        verify(cardsB).remove(monsterCardB);
    }

    @Test
    @DisplayName("Trade Card with Offer")
    void testTradeCardWithOffer() {
        // arrange
        userA = new User();
        userA.setId(1l);
        userB = new User();
        userB.setId(2l);
        cardC.setId("1");
        cardD.setId("2");


        userA.getStack().getCards().add(cardC);
        userB.getStack().getCards().add(cardD);

        ITradeCardRequirements requirements = new NormalTradeCardRequirements(15,MonsterCard.class,null,null);

        // act
        ITrade trade = userA.createTrade("ID",cardC,requirements);
        boolean worked=userB.accectTradeOffer(trade,cardD);

        // assert
        assertTrue(worked);
        assertTrue(userA.getStack().getCards().contains(cardD));

    }

    @Test
    @DisplayName("Trade Card with Offer Fail")
    void testTradeCardWithOfferFail() {
        // arrange
        userA = new User();
        userB = new User();

        userA.getStack().getCards().add(cardD);
        userB.getStack().getCards().add(cardC);

        ITradeCardRequirements requirements = new NormalTradeCardRequirements(15,MonsterCard.class,null,null);

        // act
        ITrade trade = userA.createTrade("ID",cardD,requirements);
        boolean worked=userB.accectTradeOffer(trade,cardC);

        // assert
        assertFalse(worked);
        assertFalse(userA.getStack().getCards().contains(cardC));

    }

}