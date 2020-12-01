package UnitTests.Model.User;

import Model.User.PlayerHub;
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
        userA= new User(playerhubA);

        // act
        userA.searchBattle();

        // assert
        verify(playerhubA).registerForMatchmaking(userA);
    }

    @Test
    @DisplayName("Match Register Multiple Players")
    void testRegisterMultiplePlayer() {
        // arrange

        userA= new User(playerhubA);
        userB= new User(playerhubA);
        userC= new User(playerhubA);



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