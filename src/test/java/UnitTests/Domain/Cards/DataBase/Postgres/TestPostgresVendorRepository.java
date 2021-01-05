package UnitTests.Domain.Cards.DataBase.Postgres;

import Domain.Cards.DataBase.Postgres.PostgresACardRepository;
import Domain.Cards.DataBase.Postgres.PostgresCardPackRepository;
import Domain.Cards.DataBase.Postgres.PostgresIEffectRepository;
import Domain.Cards.DataBase.Postgres.PostgresIVendorRepository;
import Domain.Cards.Interfaces.IACardRepository;
import Domain.Cards.Interfaces.ICardPackRepository;
import Domain.Cards.Interfaces.IEffectRepository;
import Domain.Cards.Interfaces.IVendorRepository;
import Model.Cards.ACard;
import Model.Cards.CardPacks.ICardPack;
import Model.Cards.CardPacks.NormalCardPack;
import Model.Cards.Effects_Races.Effects.BaseEffect;
import Model.Cards.Effects_Races.Effects.IEffect;
import Model.Cards.SpellCard;
import Model.Cards.Vendor.IVendor;
import Model.Cards.Vendor.NormalVendor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TestPostgresVendorRepository {

    IEffect baseEf = new BaseEffect();

    static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://172.17.0.2:5432/mtcg","postgres", "postgres");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    static IACardRepository cardRepository;
    static IEffectRepository effectRepository;
    static ICardPackRepository cardPackRepository;
    static IVendorRepository vendorRepository;


    public TestPostgresVendorRepository() {
    }

    @BeforeEach()
    void SetupConnection(){
        cardRepository = new PostgresACardRepository(connection);
        effectRepository = new PostgresIEffectRepository(connection,false);
        cardPackRepository = new PostgresCardPackRepository(connection);
        vendorRepository = new PostgresIVendorRepository(connection,false);
    }

    @AfterEach()
    void CleanUpDB() {
        try {
            connection
                    .createStatement()
                    .execute("truncate table battles,cardpacks,cards,cardsinpack,decks,effects,normaltrades,races,rounds,stacks,users,vendors cascade ");

            //region Reset Sequences
            connection
                    .createStatement()
                    .execute("ALTER SEQUENCE battledecks_id_seq RESTART;");

            connection
                    .createStatement()
                    .execute("ALTER SEQUENCE battles_id_seq RESTART;");
            connection
                    .createStatement()
                    .execute("ALTER SEQUENCE cardpacks_id_seq RESTART;");
            connection
                    .createStatement()
                    .execute("ALTER SEQUENCE cardsinpack_id_seq RESTART;");
            connection
                    .createStatement()
                    .execute("ALTER SEQUENCE decks_id_seq RESTART;");
            connection
                    .createStatement()
                    .execute("ALTER SEQUENCE races_id_seq RESTART;");
            connection
                    .createStatement()
                    .execute("ALTER SEQUENCE rounds_id_seq RESTART;");
            connection
                    .createStatement()
                    .execute("ALTER SEQUENCE stacks_id_seq RESTART;");
            connection
                    .createStatement()
                    .execute("ALTER SEQUENCE users_id_seq RESTART;");
            connection
                    .createStatement()
                    .execute("ALTER SEQUENCE vendors_id_seq RESTART;");

            connection
                    .createStatement()
                    .execute("UPDATE battledecks SET id = DEFAULT;");

            connection
                    .createStatement()
                    .execute("UPDATE battles SET id = DEFAULT;");
            connection
                    .createStatement()
                    .execute("UPDATE cardpacks SET id = DEFAULT;");
            connection
                    .createStatement()
                    .execute("UPDATE cards SET id = DEFAULT;");
            connection
                    .createStatement()
                    .execute("UPDATE cardsinpack SET id = DEFAULT;");

            connection
                    .createStatement()
                    .execute("UPDATE decks SET id = DEFAULT;");

            connection
                    .createStatement()
                    .execute("UPDATE effects SET id = DEFAULT;");

            connection
                    .createStatement()
                    .execute("UPDATE normaltrades SET id = DEFAULT;");

            connection
                    .createStatement()
                    .execute("UPDATE races SET id = DEFAULT;");

            connection
                    .createStatement()
                    .execute("UPDATE rounds SET id = DEFAULT;");

            connection
                    .createStatement()
                    .execute("UPDATE stacks SET id = DEFAULT;");

            connection
                    .createStatement()
                    .execute("UPDATE users SET id = DEFAULT;");

            connection
                    .createStatement()
                    .execute("UPDATE vendors SET id = DEFAULT;");
            //endregion

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }



    @AfterAll()
    static void CloseConnection() {
        try {
            connection.close();
            connection= null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    @Test
    @DisplayName("Create ICardPack with id")
    void testCreateICardPackwithId() {
        // arrange
        baseEf.setId(effectRepository.persistEntity(baseEf));

        ACard card= new SpellCard("name",10,baseEf);
        card.setId(cardRepository.persistEntity(card));

        ACard card2= new SpellCard("name",10,baseEf);
        card2.setId(cardRepository.persistEntity(card2));

        ACard card3= new SpellCard("name",10,baseEf);
        card3.setId(cardRepository.persistEntity(card3));

        ACard card4= new SpellCard("name",10,baseEf);
        card4.setId(cardRepository.persistEntity(card4));

        ACard card5= new SpellCard("name",10,baseEf);
        card5.setId(cardRepository.persistEntity(card5));

        List<ACard> cards = new ArrayList<>();
        cards.add(card);
        cards.add(card2);
        cards.add(card3);
        cards.add(card4);
        cards.add(card5);

        ICardPack cardPack = new NormalCardPack(cards);
        cardPack.setId( cardPackRepository.persistEntity(cardPack));

        IVendor vendor= new NormalVendor();
        vendor.getAvailibleCardPacks().add(cardPack);
        // act
        Long id = vendorRepository.persistEntity(vendor);

        // assert
        assertNotNull(id);
    }

    @Test
    @DisplayName("Find ICardPack")
    void testFindICardPack() {
        // arrange
        baseEf.setId(effectRepository.persistEntity(baseEf));

        ACard card= new SpellCard("name",10,baseEf);
        card.setId(cardRepository.persistEntity(card));

        ACard card2= new SpellCard("name",10,baseEf);
        card2.setId(cardRepository.persistEntity(card2));

        ACard card3= new SpellCard("name",10,baseEf);
        card3.setId(cardRepository.persistEntity(card3));

        ACard card4= new SpellCard("name",10,baseEf);
        card4.setId(cardRepository.persistEntity(card4));

        ACard card5= new SpellCard("name",10,baseEf);
        card5.setId(cardRepository.persistEntity(card5));

        List<ACard> cards = new ArrayList<>();
        cards.add(card);
        cards.add(card2);
        cards.add(card3);
        cards.add(card4);
        cards.add(card5);

        ICardPack cardPack = new NormalCardPack(cards);
        cardPack.setId( cardPackRepository.persistEntity(cardPack));

        IVendor vendor= new NormalVendor();
        vendor.getAvailibleCardPacks().add(cardPack);
        vendor.setId(vendorRepository.persistEntity(vendor));

        // act
        IVendor foundVendor = vendorRepository.findEntity(vendor.getId());

        // assert
        assertEquals(vendor.getAvailibleCardPacks().size(),foundVendor.getAvailibleCardPacks().size());
    }

    @Test
    @DisplayName("Update ICardPack")
    void testUpdateICardPack() {
        // arrange
        baseEf.setId(effectRepository.persistEntity(baseEf));

        ACard card= new SpellCard("name",10,baseEf);
        card.setId(cardRepository.persistEntity(card));

        ACard card2= new SpellCard("name",10,baseEf);
        card2.setId(cardRepository.persistEntity(card2));

        ACard card3= new SpellCard("name",10,baseEf);
        card3.setId(cardRepository.persistEntity(card3));

        ACard card4= new SpellCard("name",10,baseEf);
        card4.setId(cardRepository.persistEntity(card4));

        ACard card5= new SpellCard("name",10,baseEf);
        card5.setId(cardRepository.persistEntity(card5));

        List<ACard> cards = new ArrayList<>();
        cards.add(card);
        cards.add(card2);
        cards.add(card3);
        cards.add(card4);
        cards.add(card5);

        ICardPack cardPack = new NormalCardPack(cards);
        cardPack.setId( cardPackRepository.persistEntity(cardPack));

        IVendor vendor= new NormalVendor();
        vendor.getAvailibleCardPacks().add(cardPack);
        vendor.setId(vendorRepository.persistEntity(vendor));
        vendor.getAvailibleCardPacks().remove(cardPack);
        // act

        boolean works = vendorRepository.updateEntity(vendor);
        IVendor foundVendor = vendorRepository.findEntity(vendor.getId());

        // assert
        assertTrue(works);
        assertEquals(vendor.getAvailibleCardPacks().size(),foundVendor.getAvailibleCardPacks().size());

    }

    @Test
    @DisplayName("Update ICardPack Wrong ID")
    void testUpdateICardPackWrongId() {
        // arrange
        baseEf.setId(effectRepository.persistEntity(baseEf));

        ACard card= new SpellCard("name",10,baseEf);
        card.setId(cardRepository.persistEntity(card));

        ACard card2= new SpellCard("name",10,baseEf);
        card2.setId(cardRepository.persistEntity(card2));

        ACard card3= new SpellCard("name",10,baseEf);
        card3.setId(cardRepository.persistEntity(card3));

        ACard card4= new SpellCard("name",10,baseEf);
        card4.setId(cardRepository.persistEntity(card4));

        ACard card5= new SpellCard("name",10,baseEf);
        card5.setId(cardRepository.persistEntity(card5));

        List<ACard> cards = new ArrayList<>();
        cards.add(card);
        cards.add(card2);
        cards.add(card3);
        cards.add(card4);
        cards.add(card5);

        ICardPack cardPack = new NormalCardPack(cards);
        cardPack.setId( cardPackRepository.persistEntity(cardPack));

        IVendor vendor= new NormalVendor();
        vendor.getAvailibleCardPacks().add(cardPack);
        vendor.getAvailibleCardPacks().remove(cardPack);
        vendor.setId(5000L);
        // act
        boolean works = vendorRepository.updateEntity(vendor);

        // assert
        assertFalse(works);
    }

    @Test
    @DisplayName("Delete ICardPack")
    void testDeleteICardPack() {
        // arrange
        baseEf.setId(effectRepository.persistEntity(baseEf));

        ACard card= new SpellCard("name",10,baseEf);
        card.setId(cardRepository.persistEntity(card));

        ACard card2= new SpellCard("name",10,baseEf);
        card2.setId(cardRepository.persistEntity(card2));

        ACard card3= new SpellCard("name",10,baseEf);
        card3.setId(cardRepository.persistEntity(card3));

        ACard card4= new SpellCard("name",10,baseEf);
        card4.setId(cardRepository.persistEntity(card4));

        ACard card5= new SpellCard("name",10,baseEf);
        card5.setId(cardRepository.persistEntity(card5));

        List<ACard> cards = new ArrayList<>();
        cards.add(card);
        cards.add(card2);
        cards.add(card3);
        cards.add(card4);
        cards.add(card5);

        ICardPack cardPack = new NormalCardPack(cards);
        cardPack.setId( cardPackRepository.persistEntity(cardPack));

        IVendor vendor= new NormalVendor();
        vendor.getAvailibleCardPacks().add(cardPack);
        vendor.setId(vendorRepository.persistEntity(vendor));
        // act
        boolean works = vendorRepository.deleteEntity(vendor.getId());
        IVendor foundVendor = vendorRepository.findEntity(vendor.getId());


        // assert
        assertTrue(works);
        assertNull(foundVendor);
    }

    @Test
    @DisplayName("Delete ICardPack Wrong ID")
    void testDeleteICardPackWrongId() {
        // arrange

        // act
        boolean works = vendorRepository.deleteEntity(5000L);


        // assert
        assertFalse(works);
    }

    @Test
    @DisplayName("Get all ICardPacks")
    void getAllCards() {
        // arrange
        baseEf.setId(effectRepository.persistEntity(baseEf));

        ACard card= new SpellCard("name",10,baseEf);
        card.setId(cardRepository.persistEntity(card));

        ACard card2= new SpellCard("name",10,baseEf);
        card2.setId(cardRepository.persistEntity(card2));

        ACard card3= new SpellCard("name",10,baseEf);
        card3.setId(cardRepository.persistEntity(card3));

        ACard card4= new SpellCard("name",10,baseEf);
        card4.setId(cardRepository.persistEntity(card4));

        ACard card5= new SpellCard("name",10,baseEf);
        card5.setId(cardRepository.persistEntity(card5));

        List<ACard> cards = new ArrayList<>();
        cards.add(card);
        cards.add(card2);
        cards.add(card3);
        cards.add(card4);
        cards.add(card5);

        ICardPack cardPack = new NormalCardPack(cards);
        cardPack.setId( cardPackRepository.persistEntity(cardPack));

        IVendor vendor= new NormalVendor();
        vendor.getAvailibleCardPacks().add(cardPack);
        vendor.setId(vendorRepository.persistEntity(vendor));

        IVendor vendor2= new NormalVendor();
        vendor2.getAvailibleCardPacks().add(cardPack);
        vendor2.setId(vendorRepository.persistEntity(vendor2));

        // act
        List<IVendor> foundVendors = vendorRepository.getAllEntities();

        // assert
        assertEquals(2, foundVendors.size());
    }

}