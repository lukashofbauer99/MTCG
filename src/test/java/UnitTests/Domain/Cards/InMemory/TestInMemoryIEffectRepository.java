package UnitTests.Domain.Cards.InMemory;

import Domain.Cards.InMemory.InMemoryIEffectRepository;
import Domain.Cards.Interfaces.IEffectRepository;
import Model.Cards.Effects_Races.Effects.BaseEffect;
import Model.Cards.Effects_Races.Effects.IEffect;
import Model.Cards.Effects_Races.Effects.WaterEffect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TestInMemoryIEffectRepository {


    IEffect effectA = new BaseEffect();

    IEffectRepository effectRepository = new InMemoryIEffectRepository();

    @BeforeEach()
    void cleanUpRepo() {
        effectRepository = new InMemoryIEffectRepository();
    }

    @Test
    @DisplayName("Create IEffect")
    void testCreateIEffect() {
        // arrange
        // act
        Long id = effectRepository.persistEntity(effectA);

        // assert
        assertNotNull(id);
    }

    @Test
    @DisplayName("Find IEffect")
    void testFindIEffect() {
        // arrange
        Long id = effectRepository.persistEntity(effectA);
        // act
        IEffect foundCardPack = effectRepository.findEntity(id);

        // assert
        assertEquals(effectA, foundCardPack);
    }

    @Test
    @DisplayName("Update IEffect")
    void testUpdateIEffect() {
        // arrange
        Long id = effectRepository.persistEntity(effectA);

        effectA = new WaterEffect(new BaseEffect());
        effectA.setId(id);
        // act
        boolean works = effectRepository.updateEntity(effectA);
        IEffect foundEffect = effectRepository.findEntity(effectA.getId());

        // assert
        assertTrue(works);
        assertEquals(effectA.getName(), foundEffect.getName());

    }

    @Test
    @DisplayName("Update IEffect Wrong ID")
    void testUpdateUserWrongId() {
        // arrange
        effectA = new WaterEffect(new BaseEffect());
        // act
        boolean works = effectRepository.updateEntity(effectA);

        // assert
        assertFalse(works);
    }

    @Test
    @DisplayName("Delete IEffect")
    void testDeleteIEffect() {
        // arrange
        Long id = effectRepository.persistEntity(effectA);

        // act
        boolean works = effectRepository.deleteEntity(id);
        IEffect foundIEffect = effectRepository.findEntity(id);


        // assert
        assertTrue(works);
        assertNull(foundIEffect);
    }

    @Test
    @DisplayName("Delete IEffect Wrong ID")
    void testDeleteIEffectWrongId() {
        // arrange

        // act
        boolean works = effectRepository.deleteEntity(50000L);

        // assert
        assertFalse(works);
    }


}