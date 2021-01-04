package UnitTests.Domain.Cards.DataBase.Postgres;

import Domain.Cards.DataBase.Postgres.PostgresACardRepository;
import Domain.Cards.DataBase.Postgres.PostgresIEffectRepository;
import Domain.Cards.Interfaces.IACardRepository;
import Domain.Cards.Interfaces.IEffectRepository;
import Model.Cards.ACard;
import Model.Cards.Effects_Races.Effects.BaseEffect;
import Model.Cards.Effects_Races.Effects.FireEffect;
import Model.Cards.Effects_Races.Effects.IEffect;
import Model.Cards.Effects_Races.Effects.NormalEffect;
import Model.Cards.SpellCard;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TestPostgresACardRepository {

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

    public TestPostgresACardRepository() {
    }

    @BeforeEach()
    void SetupConnection(){
        cardRepository = new PostgresACardRepository(connection);
        effectRepository = new PostgresIEffectRepository(connection);
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
    @DisplayName("Create ACard")
    void testCreateACard() {
        // arrange
        baseEf.setId(effectRepository.persistEntity(baseEf));
        ACard card= new SpellCard("name",10,baseEf);

        // act
        String id = cardRepository.persistEntity(card);

        // assert
        assertNotNull(id);
    }

    @Test
    @DisplayName("Create ACard with id")
    void testCreateACardwithId() {
        // arrange
        String id ="givenID";
        baseEf.setId(effectRepository.persistEntity(baseEf));
        ACard card= new SpellCard("name",10,baseEf);
        card.setId(id);

        // act
        String setId = cardRepository.persistEntityGenNoId(card);

        // assert
        assertEquals(setId,id);
    }

    @Test
    @DisplayName("Find ACard")
    void testFindACard() {
        // arrange
        baseEf.setId(effectRepository.persistEntity(baseEf));
        ACard card= new SpellCard("name",10,baseEf);
        card.setId(cardRepository.persistEntity(card));

        // act
        ACard foundCard = cardRepository.findEntity(card.getId());

        // assert
        assertEquals(card.getName(), foundCard.getName());
    }

    @Test
    @DisplayName("Find ACard with Subeffects")
    void testFindACardWithSubeffects() {
        // arrange
        baseEf.setId(effectRepository.persistEntity(baseEf));
        IEffect fireEffect= new FireEffect(baseEf);
        fireEffect.setId(effectRepository.persistEntity(fireEffect));
        IEffect normalEffect= new NormalEffect(fireEffect);
        fireEffect.setId(effectRepository.persistEntity(normalEffect));
        ACard card= new SpellCard("name",10,normalEffect);
        card.setId(cardRepository.persistEntity(card));

        // act
        ACard foundCard = cardRepository.findEntity(card.getId());

        // assert
        assertEquals(foundCard.getEffect().getBase().getBase().getId(), baseEf.getId());
    }

    @Test
    @DisplayName("Update ACard")
    void testUpdateACard() {
        // arrange
        baseEf.setId(effectRepository.persistEntity(baseEf));
        ACard card= new SpellCard("name",10,baseEf);
        card.setId(cardRepository.persistEntity(card));

        card.setName("Changed");
        // act

        boolean works = cardRepository.updateEntity(card);
        ACard foundCard = cardRepository.findEntity(card.getId());

        // assert
        assertTrue(works);
        assertEquals(card.getName(), foundCard.getName());

    }

    @Test
    @DisplayName("Update ACard Wrong ID")
    void testUpdateACardWrongId() {
        // arrange
        baseEf.setId(effectRepository.persistEntity(baseEf));
        ACard card= new SpellCard("name",10,baseEf);
        card.setId("5000");
        // act
        boolean works = cardRepository.updateEntity(card);

        // assert
        assertFalse(works);
    }

    @Test
    @DisplayName("Delete ACard")
    void testDeleteACard() {
        // arrange
        baseEf.setId(effectRepository.persistEntity(baseEf));
        ACard card= new SpellCard("name",10,baseEf);
        card.setId(cardRepository.persistEntity(card));

        // act
        boolean works = cardRepository.deleteEntity(card.getId());
        ACard foundCard = cardRepository.findEntity(card.getId());


        // assert
        assertTrue(works);
        assertNull(foundCard);
    }

    @Test
    @DisplayName("Delete ACard Wrong ID")
    void testDeleteACardWrongId() {
        // arrange

        // act
        boolean works = cardRepository.deleteEntity("50000");

        // assert
        assertFalse(works);
    }

    @Test
    @DisplayName("Get all ACards")
    void getAllCards() {
        // arrange
        baseEf.setId(effectRepository.persistEntity(baseEf));
        ACard card= new SpellCard("name",10,baseEf);
        ACard card2= new SpellCard("name2",10,baseEf);
        card.setId(cardRepository.persistEntity(card));
        card2.setId(cardRepository.persistEntity(card2));

        // act
        List<ACard> foundCards = cardRepository.getAllEntities();

        // assert
        assertEquals(2, foundCards.size());
    }

}