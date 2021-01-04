package UnitTests.Domain.User.DataBase.Postgres;

import Domain.User.DataBase.Postgres.PostgresUserRepository;
import Domain.User.InMemory.InMemoryUserRepository;
import Domain.User.Interfaces.IUserRepository;
import Model.Cards.Effects_Races.Effects.BaseEffect;
import Model.Cards.Effects_Races.Effects.FireEffect;
import Model.Cards.Effects_Races.Races.BaseRace;
import Model.Cards.Effects_Races.Races.FireElfRace;
import Model.Cards.MonsterCard;
import Model.User.Credentials;
import Model.User.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TestPostgresUserRepository {

    User userA = new User(new Credentials("user", "pw"));

    static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://172.17.0.2:5432/mtcg","postgres", "postgres");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    static IUserRepository userRepository ;

    public TestPostgresUserRepository() {
    }

    @BeforeEach()
    void SetupConnection() {
        userRepository = new PostgresUserRepository(connection);
    }

    @AfterEach()
    void CleanUpDB() {
        try {
            connection
                    .createStatement()
                    .execute("truncate table battledecks,battles,cardpacks,cards,cardsinpack,decks,effects,normaltrades,races,rounds,stacks,users,vendors cascade ");

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
    @DisplayName("Create User")
    void testCreateUser() {
        // arrange
        // act
        Long id = userRepository.persistEntity(userA);

        // assert
        assertEquals(1, id);
    }

    @Test
    @DisplayName("Find User")
    void testFindUser() {
        // arrange
        userA.getStack().getCards().add(new MonsterCard("Fireelf", 20, new FireEffect(new BaseEffect()), new BaseRace()));
        Long id = userRepository.persistEntity(userA);
        // act
        User foundUser = userRepository.findEntity(id);

        // assert
        assertEquals(userA.getCredentials().getUsername(), foundUser.getCredentials().getUsername());
    }

    @Test
    @DisplayName("Update User")
    void testUpdateUser() {
        // arrange
        userA.setId(userRepository.persistEntity(userA));
        userA.getCredentials().setUsername("newName");
        userA.getStack().getCards().add(new MonsterCard("Fireelf", 20, new BaseEffect(), new BaseRace()));
        // act
        boolean works = userRepository.updateEntity(userA);
        User foundUser = userRepository.findEntity(1L);

        // assert
        assertTrue(works);
        assertEquals(userA.getCredentials().getUsername(), foundUser.getCredentials().getUsername());

    }

    @Test
    @DisplayName("Update User Wrong ID")
    void testUpdateUserWrongId() {
        // arrange
        userA.setId(1L);
        userA.getCredentials().setUsername("newName");
        // act
        boolean works = userRepository.updateEntity(userA);

        // assert
        assertFalse(works);
    }

    @Test
    @DisplayName("Delete User")
    void testDeleteUser() {
        // arrange
        Long id = userRepository.persistEntity(userA);

        // act
        boolean works = userRepository.deleteEntity(id);
        User foundUser = userRepository.findEntity(id);


        // assert
        assertTrue(works);
        assertNull(foundUser);
    }

    @Test
    @DisplayName("Delete User Wrong ID")
    void testDeleteUserWrongId() {
        // arrange

        // act
        boolean works = userRepository.deleteEntity(0L);

        // assert
        assertFalse(works);
    }

    @Test
    @DisplayName("Login")
    void testLogin() {
        // arrange
        userRepository.persistEntity(userA);
        // act
        String token = userRepository.loginUser(userA.getCredentials());

        // assert
        assertEquals("Basic " + userA.getCredentials().getUsername() + "-mtcgToken", token);
    }

    @Test
    @DisplayName("Login Wrong Credentials")
    void testLoginWrongCredentials() {
        // arrange
        // act
        String token = userRepository.loginUser(userA.getCredentials());

        // assert
        assertNull(token);
    }


    @Test
    @DisplayName("get User With Token")
    void testGetUserWithToken() {
        userRepository.persistEntity(userA);
        // act
        String token = userRepository.loginUser(userA.getCredentials());
        User user = userRepository.getUserWithToken("Basic " + userA.getCredentials().getUsername() + "-mtcgToken");

        // assert
        assertEquals(userA, user);
    }

    @Test
    @DisplayName("get User With Username")
    void testGetUserWithUsername() {
        userRepository.persistEntity(userA);
        // act
        User user = userRepository.getUserWithUsername("user");

        // assert
        assertEquals(userA, user);
    }

}