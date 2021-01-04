package UnitTests.Domain.Cards.DataBase.Postgres;

import Domain.Cards.DataBase.Postgres.PostgresIEffectRepository;
import Domain.Cards.InMemory.InMemoryIEffectRepository;
import Domain.Cards.Interfaces.IEffectRepository;
import Domain.User.Interfaces.IUserRepository;
import Model.Cards.ACard;
import Model.Cards.Effects_Races.Effects.BaseEffect;
import Model.Cards.Effects_Races.Effects.FireEffect;
import Model.Cards.Effects_Races.Effects.IEffect;
import Model.Cards.Effects_Races.Effects.WaterEffect;
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
public class TestPostgresIEffectRepository {


    IEffect effectA = new BaseEffect();

    static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://172.17.0.2:5432/mtcg","postgres", "postgres");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    static IEffectRepository effectRepository;

    public TestPostgresIEffectRepository() {
    }

    @BeforeEach()
    void SetupConnection(){
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
        IEffect foundeffect = effectRepository.findEntity(id);

        // assert
        assertEquals(effectA.getName(), foundeffect.getName());
    }

    @Test
    @DisplayName("Update IEffect")
    void testUpdateIEffect() {
        // arrange
        Long id1 = effectRepository.persistEntity(effectA);
        effectA.setId(id1);

        IEffect effectB = new WaterEffect(effectA);
        effectB.setName("test");
        Long id2 = effectRepository.persistEntity(effectB);
        effectB.setId(id2);

        effectB.setName("changed");
        // act

        boolean works = effectRepository.updateEntity(effectB);
        IEffect foundEffect = effectRepository.findEntity(effectB.getId());

        // assert
        assertTrue(works);
        assertEquals(effectB.getName(), foundEffect.getName());

    }

    @Test
    @DisplayName("Update IEffect Wrong ID")
    void testUpdateUserWrongId() {
        // arrange
        effectA = new WaterEffect();
        effectA.setId(1000L);
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

    @Test
    @DisplayName("Get all IEffecs")
    void getAllIEffecs(){
        // arrange
        effectA.setId(effectRepository.persistEntity(effectA));

        IEffect fireEffect = new FireEffect(effectA);
        fireEffect.setId(effectRepository.persistEntity(fireEffect));

        // act
        List<IEffect> foundEffects = effectRepository.getAllEntities();

        // assert
        assertEquals(2, foundEffects.size());
    }

}