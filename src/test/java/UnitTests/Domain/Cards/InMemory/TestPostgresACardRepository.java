package UnitTests.Domain.Cards.InMemory;

import Domain.Cards.InMemory.InMemoryACardRepository;
import Domain.Cards.Interfaces.IACardRepository;
import Model.Cards.ACard;
import Model.Cards.Effects_Races.Effects.BaseEffect;
import Model.Cards.Effects_Races.Effects.FireEffect;
import Model.Cards.Effects_Races.Effects.IEffect;
import Model.Cards.Effects_Races.Races.BaseRace;
import Model.Cards.Effects_Races.Races.FireElfRace;
import Model.Cards.Effects_Races.Races.IRace;
import Model.Cards.MonsterCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TestPostgresACardRepository {


    IEffect baseEffect = new BaseEffect();
    IRace baseRace = new BaseRace();
    ACard cardA = new MonsterCard("Fireelf", 20, new FireEffect(baseEffect), new FireElfRace(baseRace));

    IACardRepository cardRepository = new InMemoryACardRepository();

    @BeforeEach()
    void cleanUpRepo() {
        cardRepository = new InMemoryACardRepository();
    }

    @Test
    @DisplayName("Create ACard")
    void testCreateACard() {
        // arrange
        // act
        String id = cardRepository.persistEntity(cardA);

        // assert
        assertEquals("1", id);
    }

    @Test
    @DisplayName("Create ACard Gen No Id")
    void testCreateACardNoId() {
        // arrange
        cardA.setId("predef");
        // act
        String id = cardRepository.persistEntityGenNoId(cardA);

        // assert
        assertEquals("predef", id);
    }

    @Test
    @DisplayName("Find ACard")
    void testFindACard() {
        // arrange
        cardRepository.persistEntity(cardA);
        // act
        ACard foundCard = cardRepository.findEntity("1");

        // assert
        assertEquals(cardA, foundCard);
    }

    @Test
    @DisplayName("Update ACard")
    void testUpdateACard() {
        // arrange
        cardRepository.persistEntity(cardA);
        cardA.setName("newName");
        // act
        boolean works = cardRepository.updateEntity(cardA);
        ACard foundCard = cardRepository.findEntity(cardA.getId());

        // assert
        assertTrue(works);
        assertEquals(cardA.getName(), foundCard.getName());

    }

    @Test
    @DisplayName("Update ACard Wrong ID")
    void testUpdateACardWrongId() {
        // arrange
        cardA.setName("newName");
        // act
        boolean works = cardRepository.updateEntity(cardA);

        // assert
        assertFalse(works);
    }

    @Test
    @DisplayName("Delete ACard")
    void testDeleteACard() {
        // arrange
        String id = cardRepository.persistEntity(cardA);

        // act
        boolean works = cardRepository.deleteEntity(id);
        ACard foundACard = cardRepository.findEntity(id);


        // assert
        assertTrue(works);
        assertNull(foundACard);
    }

    @Test
    @DisplayName("Delete ACard Wrong ID")
    void testDeleteACardWrongId() {
        // arrange

        // act
        boolean works = cardRepository.deleteEntity("0");

        // assert
        assertFalse(works);
    }


}