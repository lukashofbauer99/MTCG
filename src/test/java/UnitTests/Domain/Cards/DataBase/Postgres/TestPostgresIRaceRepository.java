package UnitTests.Domain.Cards.DataBase.Postgres;

import Domain.Cards.DataBase.Postgres.PostgresIRaceRepository;
import Domain.Cards.Interfaces.IRaceRepository;
import Model.Cards.Effects_Races.Races.BaseRace;
import Model.Cards.Effects_Races.Races.DragonRace;
import Model.Cards.Effects_Races.Races.IRace;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TestPostgresIRaceRepository {


    IRace baseRace = new BaseRace();

    static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://172.17.0.2:5432/mtcg_testing","postgres", "postgres");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    static IRaceRepository raceRepository;

    public TestPostgresIRaceRepository() {
    }

    @BeforeEach()
    void SetupConnection(){
        raceRepository = new PostgresIRaceRepository(connection,false);
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
                    .execute("ALTER SEQUENCE effects_id_seq RESTART;");
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
    @DisplayName("Create IRace")
    void testCreateIRace() {
        // arrange
        // act
        Long id = raceRepository.persistEntity(baseRace);

        // assert
        assertNotNull(id);
    }

    @Test
    @DisplayName("Find IRace")
    void testFindIRace() {
        // arrange
        Long id = raceRepository.persistEntity(baseRace);
        // act
        IRace foundeffect = raceRepository.findEntity(id);

        // assert
        assertEquals(baseRace.getName(), foundeffect.getName());
    }

    @Test
    @DisplayName("Update IRace")
    void testUpdateIRace() {
        // arrange
        Long id1 = raceRepository.persistEntity(baseRace);
        baseRace.setId(id1);

        IRace raceB = new DragonRace(baseRace);
        raceB.setName("test");
        Long id2 = raceRepository.persistEntity(raceB);
        raceB.setId(id2);

        raceB.setName("changed");
        // act

        boolean works = raceRepository.updateEntity(raceB);
        IRace foundRace = raceRepository.findEntity(raceB.getId());

        // assert
        assertTrue(works);
        assertEquals(raceB.getName(), foundRace.getName());

    }

    @Test
    @DisplayName("Update IRace Wrong ID")
    void testUpdateUserWrongId() {
        // arrange
        baseRace = new DragonRace();
        baseRace.setId(1000L);
        // act
        boolean works = raceRepository.updateEntity(baseRace);

        // assert
        assertFalse(works);
    }

    @Test
    @DisplayName("Delete IRace")
    void testDeleteIRace() {
        // arrange
        Long id = raceRepository.persistEntity(baseRace);

        // act
        boolean works = raceRepository.deleteEntity(id);
        IRace foundIRAce = raceRepository.findEntity(id);


        // assert
        assertTrue(works);
        assertNull(foundIRAce);
    }

    @Test
    @DisplayName("Delete IRace Wrong ID")
    void testDeleteIRaceWrongId() {
        // arrange

        // act
        boolean works = raceRepository.deleteEntity(50000L);

        // assert
        assertFalse(works);
    }


    @Test
    @DisplayName("Get all IRaces")
    void getAllIRaces(){
        // arrange
        baseRace.setId(raceRepository.persistEntity(baseRace));

        IRace dragonRace = new DragonRace(baseRace);
        dragonRace.setId(raceRepository.persistEntity(dragonRace));

        // act
        List<IRace> foundRaces = raceRepository.getAllEntities();

        // assert
        assertEquals(2, foundRaces.size());
    }


}