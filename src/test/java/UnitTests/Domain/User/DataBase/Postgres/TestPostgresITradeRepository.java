package UnitTests.Domain.User.DataBase.Postgres;

import Domain.Cards.DataBase.Postgres.PostgresACardRepository;
import Domain.Cards.DataBase.Postgres.PostgresIEffectRepository;
import Domain.Cards.DataBase.Postgres.PostgresIRaceRepository;
import Domain.Cards.Interfaces.IACardRepository;
import Domain.Cards.Interfaces.IEffectRepository;
import Domain.Cards.Interfaces.IRaceRepository;
import Domain.User.DataBase.Postgres.PostgresITradeRepository;
import Domain.User.DataBase.Postgres.PostgresUserRepository;
import Domain.User.Interfaces.ITradeRepository;
import Domain.User.Interfaces.IUserRepository;
import Model.Cards.ACard;
import Model.Cards.Effects_Races.Effects.BaseEffect;
import Model.Cards.Effects_Races.Effects.FireEffect;
import Model.Cards.Effects_Races.Effects.IEffect;
import Model.Cards.Effects_Races.Races.BaseRace;
import Model.Cards.Effects_Races.Races.IRace;
import Model.Cards.MonsterCard;
import Model.User.Credentials;
import Model.User.Trade.ITrade;
import Model.User.Trade.ITradeCardRequirements;
import Model.User.Trade.NormalTradeCardRequirements;
import Model.User.Trade.Trade1To1;
import Model.User.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TestPostgresITradeRepository {

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
    static IACardRepository cardRepository;
    static IEffectRepository effectRepository;
    static IRaceRepository iRaceRepository;
    static ITradeRepository iTradeRepository;

    public TestPostgresITradeRepository() {
    }

    @BeforeEach()
    void SetupConnection() {

        userRepository = new PostgresUserRepository(connection);
        cardRepository = new PostgresACardRepository(connection);
        effectRepository = new PostgresIEffectRepository(connection);
        iRaceRepository = new PostgresIRaceRepository(connection);
        iTradeRepository = new PostgresITradeRepository(connection);

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
    @DisplayName("Create ITrade")
    void testCreateITrade() {
        // arrange
        IEffect baseEf= new BaseEffect();
        baseEf.setId(effectRepository.persistEntity(baseEf));
        IEffect fireEffect= new FireEffect(baseEf);
        fireEffect.setId(effectRepository.persistEntity(fireEffect));

        IRace baseRace = new BaseRace();
        baseRace.setId(iRaceRepository.persistEntity(baseRace));
        ACard card = new MonsterCard("Fireelf", 20, fireEffect,baseRace);
        card.setId(cardRepository.persistEntity(card));

        userA.getStack().getCards().add(card);
        userA.setId(userRepository.persistEntity(userA));
        ITrade trade = new Trade1To1("id",userA,card,new NormalTradeCardRequirements(10d,MonsterCard.class,baseEf,baseRace));
        // act

        String id = iTradeRepository.persistEntityGenNoId(trade);
        // assert
        assertEquals("id", id);
    }

    @Test
    @DisplayName("Find ITrade")
    void testFindITrade() {
        // arrange
        IEffect baseEf= new BaseEffect();
        baseEf.setId(effectRepository.persistEntity(baseEf));
        IEffect fireEffect= new FireEffect(baseEf);
        fireEffect.setId(effectRepository.persistEntity(fireEffect));

        IRace baseRace = new BaseRace();
        baseRace.setId(iRaceRepository.persistEntity(baseRace));
        ACard card = new MonsterCard("Fireelf", 20, fireEffect,baseRace);
        card.setId(cardRepository.persistEntity(card));

        userA.getStack().getCards().add(card);
        userA.setId(userRepository.persistEntity(userA));
        ITrade trade = new Trade1To1("id",userA,card,new NormalTradeCardRequirements(10d,MonsterCard.class,baseEf,baseRace));

        trade.setId(iTradeRepository.persistEntityGenNoId(trade));
        // act
        ITrade foundTrade = iTradeRepository.findEntity(trade.getId());

        // assert
        assertEquals(foundTrade.getCardTradedFor().getId(), card.getId());
    }

    @Test
    @DisplayName("Update User")
    void testUpdateUser() {
        // arrange
        IEffect baseEf= new BaseEffect();
        baseEf.setId(effectRepository.persistEntity(baseEf));
        IEffect fireEffect= new FireEffect(baseEf);
        fireEffect.setId(effectRepository.persistEntity(fireEffect));

        IRace baseRace = new BaseRace();
        baseRace.setId(iRaceRepository.persistEntity(baseRace));
        ACard card = new MonsterCard("Fireelf", 20, fireEffect,baseRace);
        card.setId(cardRepository.persistEntity(card));

        userA.getStack().getCards().add(card);
        userA.setId(userRepository.persistEntity(userA));
        ITrade trade = new Trade1To1("id",userA,card,new NormalTradeCardRequirements(10d,MonsterCard.class,baseEf,baseRace));
        trade.setId(iTradeRepository.persistEntityGenNoId(trade));
        trade.getRequirements().setMinimumDamage(5d);
        // act
        boolean works = iTradeRepository.updateEntity(trade);
        ITrade foundTrade = iTradeRepository.findEntity(trade.getId());

        // assert
        assertTrue(works);
        assertEquals(trade.getRequirements().getMinimumDamage(), foundTrade.getRequirements().getMinimumDamage());

    }

    @Test
    @DisplayName("Update User Wrong ID")
    void testUpdateUserWrongId() {
        // arrange
        IEffect baseEf= new BaseEffect();
        baseEf.setId(effectRepository.persistEntity(baseEf));
        IEffect fireEffect= new FireEffect(baseEf);
        fireEffect.setId(effectRepository.persistEntity(fireEffect));

        IRace baseRace = new BaseRace();
        baseRace.setId(iRaceRepository.persistEntity(baseRace));
        ACard card = new MonsterCard("Fireelf", 20, fireEffect,baseRace);
        card.setId(cardRepository.persistEntity(card));

        userA.getStack().getCards().add(card);
        userA.setId(userRepository.persistEntity(userA));
        ITrade trade = new Trade1To1("id",userA,card,new NormalTradeCardRequirements(10d,MonsterCard.class,baseEf,baseRace));
        trade.setId(iTradeRepository.persistEntityGenNoId(trade));
        trade.getRequirements().setMinimumDamage(5d);
        trade.setId("wrongid");
        // act
        boolean works = iTradeRepository.updateEntity(trade);

        // assert
        assertFalse(works);
    }

    @Test
    @DisplayName("Delete User")
    void testDeleteUser() {
        // arrange
        IEffect baseEf= new BaseEffect();
        baseEf.setId(effectRepository.persistEntity(baseEf));
        IEffect fireEffect= new FireEffect(baseEf);
        fireEffect.setId(effectRepository.persistEntity(fireEffect));

        IRace baseRace = new BaseRace();
        baseRace.setId(iRaceRepository.persistEntity(baseRace));
        ACard card = new MonsterCard("Fireelf", 20, fireEffect,baseRace);
        card.setId(cardRepository.persistEntity(card));

        userA.getStack().getCards().add(card);
        userA.setId(userRepository.persistEntity(userA));
        ITrade trade = new Trade1To1("id",userA,card,new NormalTradeCardRequirements(10d,MonsterCard.class,baseEf,baseRace));

        trade.setId(iTradeRepository.persistEntityGenNoId(trade));

        // act
        boolean works = iTradeRepository.deleteEntity(trade.getId());
        ITrade foundTrade = iTradeRepository.findEntity(trade.getId());


        // assert
        assertTrue(works);
        assertNull(foundTrade);
    }

    @Test
    @DisplayName("Delete User Wrong ID")
    void testDeleteUserWrongId() {
        // arrange

        // act
        boolean works = iTradeRepository.deleteEntity("ids");

        // assert
        assertFalse(works);
    }


}