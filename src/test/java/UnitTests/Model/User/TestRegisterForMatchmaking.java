package UnitTests.Model.User;

import Domain.PlayerHub;
import Model.User.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TestRegisterForMatchmaking {
    @Mock
    User userA;
    @Mock
    User userB;
    @Mock
    User userC;

    @Mock
    PlayerHub playerhubA;


    @Test
    @DisplayName("Match Register 1 Player")
    void testRegisterOnePlayer() {
        // arrange
        userA = new User();

        // act
        userA.searchBattle(playerhubA);

        // assert
        verify(playerhubA).matchPlayers(userA);
    }

    @Test
    @DisplayName("Match Register Multiple Players")
    void testRegisterMultiplePlayer() {
        // arrange
        userA = new User();
        userB = new User();
        userC = new User();

        // act
        userA.searchBattle(playerhubA);
        userB.searchBattle(playerhubA);
        userC.searchBattle(playerhubA);

        // assert
        verify(playerhubA).matchPlayers(userA);
        verify(playerhubA).matchPlayers(userB);
        verify(playerhubA).matchPlayers(userC);
    }

}