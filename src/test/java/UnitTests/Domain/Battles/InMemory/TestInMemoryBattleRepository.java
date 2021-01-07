package UnitTests.Domain.Battles.InMemory;

import Domain.Battle.InMemory.InMemoryBattleRepository;
import Domain.Battle.Interfaces.IBattleRepository;
import Model.Battle.Battle;
import Model.User.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TestInMemoryBattleRepository {


    Battle battleA = new Battle();

    IBattleRepository battleRepository = new InMemoryBattleRepository();

    @BeforeEach()
    void cleanUpRepo() {
        battleRepository = new InMemoryBattleRepository();
    }

    @Test
    @DisplayName("Create Battle")
    void testCreateBattle() {
        // arrange
        // act
        Long id = battleRepository.persistEntity(battleA);

        // assert
        assertNotNull(id);
    }

    @Test
    @DisplayName("Find Battle")
    void testFindBattle() {
        // arrange
        Long id = battleRepository.persistEntity(battleA);
        // act
        Battle foundCardPack = battleRepository.findEntity(id);

        // assert
        assertEquals(battleA, foundCardPack);
    }

    @Test
    @DisplayName("Update Battle")
    void testUpdateBattle() {
        // arrange
        Long id = battleRepository.persistEntity(battleA);

        battleA.setWinner(new User());
        // act
        boolean works = battleRepository.updateEntity(battleA);
        Battle foundBattle = battleRepository.findEntity(battleA.getId());

        // assert
        assertTrue(works);
        assertEquals(battleA.getWinner(), foundBattle.getWinner());

    }

    @Test
    @DisplayName("Update Battle Wrong ID")
    void testUpdateUserWrongId() {
        // arrange
        battleA.setWinner(new User());
        // act
        boolean works = battleRepository.updateEntity(battleA);

        // assert
        assertFalse(works);
    }

    @Test
    @DisplayName("Delete Battle")
    void testDeleteBattle() {
        // arrange
        Long id = battleRepository.persistEntity(battleA);

        // act
        boolean works = battleRepository.deleteEntity(id);
        Battle foundBattle = battleRepository.findEntity(id);


        // assert
        assertTrue(works);
        assertNull(foundBattle);
    }

    @Test
    @DisplayName("Delete Battle Wrong ID")
    void testDeleteBattleWrongId() {
        // arrange

        // act
        boolean works = battleRepository.deleteEntity(50000L);

        // assert
        assertFalse(works);
    }


}