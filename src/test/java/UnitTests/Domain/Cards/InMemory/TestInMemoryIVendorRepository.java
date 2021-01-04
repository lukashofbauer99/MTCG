package UnitTests.Domain.Cards.InMemory;

import Domain.Cards.InMemory.InMemoryIVendorRepository;
import Domain.Cards.Interfaces.IVendorRepository;
import Model.Cards.CardPacks.ICardPack;
import Model.Cards.CardPacks.NormalCardPack;
import Model.Cards.Vendor.IVendor;
import Model.Cards.Vendor.NormalVendor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TestInMemoryIVendorRepository {


    IVendor iVendorA = new NormalVendor();

    IVendorRepository vendorRepository = new InMemoryIVendorRepository();

    @BeforeEach()
    void cleanUpRepo() {
        vendorRepository = new InMemoryIVendorRepository();
    }

    @Test
    @DisplayName("Create IVendor")
    void testCreateIRace() {
        // arrange
        // act
        Long id = vendorRepository.persistEntity(iVendorA);

        // assert
        assertNotNull(id);
    }

    @Test
    @DisplayName("Find IVendor")
    void testFindIVendor() {
        // arrange
        Long id = vendorRepository.persistEntity(iVendorA);
        // act
        IVendor foundVendor = vendorRepository.findEntity(id);

        // assert
        assertEquals(iVendorA, foundVendor);
    }

    @Test
    @DisplayName("Update IVendor")
    void testUpdateIVendor() {
        // arrange
        Long id = vendorRepository.persistEntity(iVendorA);

        ICardPack cardPack = new NormalCardPack(null);
        iVendorA.addICardPack(cardPack);

        iVendorA.setId(id);
        // act
        boolean works = vendorRepository.updateEntity(iVendorA);
        IVendor foundVendor = vendorRepository.findEntity(iVendorA.getId());

        // assert
        assertTrue(works);
        assertEquals(iVendorA, foundVendor);

    }

    @Test
    @DisplayName("Update IVendor Wrong ID")
    void testUpdateUserWrongId() {
        // arrange
        ICardPack cardPack = new NormalCardPack(null);
        iVendorA.addICardPack(cardPack);
        // act
        boolean works = vendorRepository.updateEntity(iVendorA);

        // assert
        assertFalse(works);
    }

    @Test
    @DisplayName("Delete IVendor")
    void testDeleteIVendor() {
        // arrange
        Long id = vendorRepository.persistEntity(iVendorA);

        // act
        boolean works = vendorRepository.deleteEntity(id);
        IVendor foundIVendor = vendorRepository.findEntity(id);


        // assert
        assertTrue(works);
        assertNull(foundIVendor);
    }

    @Test
    @DisplayName("Delete IVendor Wrong ID")
    void testDeleteIVendorWrongId() {
        // arrange

        // act
        boolean works = vendorRepository.deleteEntity(50000L);

        // assert
        assertFalse(works);
    }


}