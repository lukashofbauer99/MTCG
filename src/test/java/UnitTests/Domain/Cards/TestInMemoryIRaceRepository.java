package UnitTests.Domain.Cards;

import Domain.Cards.InMemory.InMemoryIRaceRepository;
import Domain.Cards.Interfaces.IRaceRepository;
import Model.Cards.Effects_Races.Effects.BaseEffect;
import Model.Cards.Effects_Races.Effects.WaterEffect;
import Model.Cards.Effects_Races.Races.BaseRace;
import Model.Cards.Effects_Races.Races.FireElfRace;
import Model.Cards.Effects_Races.Races.IRace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TestInMemoryIRaceRepository {


    IRace iRaceA =new BaseRace();

    IRaceRepository effectRepository = new InMemoryIRaceRepository();

    @BeforeEach()
    void cleanUpRepo()
    {
        effectRepository = new InMemoryIRaceRepository();
    }

    @Test
    @DisplayName("Create IRace")
    void testCreateIRace() {
        // arrange
        // act
        Long id = effectRepository.persistEntity(iRaceA);

        // assert
        assertNotNull(id);
    }

    @Test
    @DisplayName("Find IRace")
    void testFindIRace() {
        // arrange
        Long id = effectRepository.persistEntity(iRaceA);
        // act
        IRace foundCardPack = effectRepository.findEntity(id);

        // assert
        assertEquals(iRaceA, foundCardPack);
    }

    @Test
    @DisplayName("Update IRace")
    void testUpdateIRace() {
        // arrange
        Long id =effectRepository.persistEntity(iRaceA);

        iRaceA.setId(id);
        // act
        boolean works = effectRepository.updateEntity(iRaceA);
        IRace foundEffect = effectRepository.findEntity(iRaceA.getId());

        // assert
        assertTrue(works);
        assertEquals(iRaceA.getName(), foundEffect.getName());

    }

    @Test
    @DisplayName("Update IRace Wrong ID")
    void testUpdateUserWrongId() {
        // arrange
        iRaceA = new FireElfRace(new BaseRace());
        // act
        boolean works = effectRepository.updateEntity(iRaceA);

        // assert
        assertFalse(works);
    }

    @Test
    @DisplayName("Delete IRace")
    void testDeleteIRace() {
        // arrange
        Long id = effectRepository.persistEntity(iRaceA);

        // act
        boolean works= effectRepository.deleteEntity(id);
        IRace foundIRace = effectRepository.findEntity(id);


        // assert
        assertTrue(works);
        assertNull(foundIRace);
    }

    @Test
    @DisplayName("Delete IRace Wrong ID")
    void testDeleteIRaceWrongId() {
        // arrange

        // act
        boolean works= effectRepository.deleteEntity(50000L);

        // assert
        assertFalse(works);
    }


}