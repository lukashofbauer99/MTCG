package UnitTests.Domain.Cards.InMemory;

import Domain.Cards.InMemory.InMemoryCardPackRepository;
import Domain.Cards.Interfaces.ICardPackRepository;
import Model.Cards.ACard;
import Model.Cards.CardPacks.ICardPack;
import Model.Cards.CardPacks.NormalCardPack;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TestInMemoryICardPackRepository {


    ICardPack cardPackA = new NormalCardPack(null);

    ICardPackRepository cardPackRepository = new InMemoryCardPackRepository();

    @BeforeEach()
    void cleanUpRepo() {
        cardPackRepository = new InMemoryCardPackRepository();
    }

    @Test
    @DisplayName("Create ICardPack")
    void testCreateICardPack() {
        // arrange
        // act
        Long id = cardPackRepository.persistEntity(cardPackA);

        // assert
        assertEquals(1L, id);
    }

    @Test
    @DisplayName("Find ICardPack")
    void testFindICardPack() {
        // arrange
        cardPackRepository.persistEntity(cardPackA);
        // act
        ICardPack foundCardPack = cardPackRepository.findEntity(1L);

        // assert
        assertEquals(cardPackA, foundCardPack);
    }

    @Test
    @DisplayName("Update ICardPack")
    void testUpdateICardPack() {
        // arrange
        Long id = cardPackRepository.persistEntity(cardPackA);


        IEffect baseEffect = new BaseEffect();
        IRace baseRace = new BaseRace();
        ACard cardA = new MonsterCard("Fireelf", 20, new FireEffect(baseEffect), new FireElfRace(baseRace));
        List<ACard> cards = new ArrayList<>();
        cards.add(cardA);
        cardPackA = new NormalCardPack(cards);
        cardPackA.setId(id);

        // act
        boolean works = cardPackRepository.updateEntity(cardPackA);
        ICardPack foundCard = cardPackRepository.findEntity(cardPackA.getId());

        // assert
        assertTrue(works);
        assertEquals(cardPackA.getCards(), foundCard.getCards());

    }

    @Test
    @DisplayName("Update ICardPack Wrong ID")
    void testUpdateICardPackWrongId() {
        // arrange
        IEffect baseEffect = new BaseEffect();
        IRace baseRace = new BaseRace();
        ACard cardA = new MonsterCard("Fireelf", 20, new FireEffect(baseEffect), new FireElfRace(baseRace));
        List<ACard> cards = new ArrayList<>();
        cards.add(cardA);
        cardPackA = new NormalCardPack(cards);
        // act
        boolean works = cardPackRepository.updateEntity(cardPackA);

        // assert
        assertFalse(works);
    }

    @Test
    @DisplayName("Delete ICardPack")
    void testDeleteICardPack() {
        // arrange
        Long id = cardPackRepository.persistEntity(cardPackA);

        // act
        boolean works = cardPackRepository.deleteEntity(id);
        ICardPack foundCardPack = cardPackRepository.findEntity(id);


        // assert
        assertTrue(works);
        assertNull(foundCardPack);
    }

    @Test
    @DisplayName("Delete ICardPack Wrong ID")
    void testDeleteICardPackWrongId() {
        // arrange

        // act
        boolean works = cardPackRepository.deleteEntity(1L);

        // assert
        assertFalse(works);
    }


}