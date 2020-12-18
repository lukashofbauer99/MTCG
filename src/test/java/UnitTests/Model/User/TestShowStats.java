package UnitTests.Model.User;

import Model.User.Statistics.Stats;
import Model.User.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TestShowStats {

    User userA = new User();

    @Test
    @DisplayName("Show Stats")
    void testBuyCardPack() {
        // arrange

        // act
        Stats stats;
        stats = userA.showStats();
        // assert
        //verify(playerHub).buyCards(userA,PackType.Normal);
        assertEquals(100, stats.getMmr());
    }


}