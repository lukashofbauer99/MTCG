package UnitTests.User;

import Model.User.PlayerHub;
import Model.User.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
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
        /*
        userA = mock(User.class);
        playerhubA = mock(PlayerHub.class);
        */


        // act
        userA.searchBattle();

        // assert
        verify(playerhubA).registerForMatchmaking(userA);
    }

    @Test
    @DisplayName("Match Register Multiple Players")
    void testRegisterMultiplePlayer() {
        // arrange
        /*
        userA = mock(User.class) ;
        userB = mock(User.class);
        userC = mock(User.class);
        playerhubA = mock(PlayerHub.class);
        */


        // act
        userA.searchBattle();
        userB.searchBattle();
        userC.searchBattle();

        // assert
        verify(playerhubA).registerForMatchmaking(userA);
        verify(playerhubA).registerForMatchmaking(userB);
        verify(playerhubA).registerForMatchmaking(userC);
    }

}