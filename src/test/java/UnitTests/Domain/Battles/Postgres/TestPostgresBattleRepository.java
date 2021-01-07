package UnitTests.Domain.Battles.Postgres;

import Domain.Battle.DataBase.Postgres.PostgresBattleRepository;
import Domain.Battle.Interfaces.IBattleRepository;
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
import Model.Battle.Battle;
import Model.Cards.ACard;
import Model.Cards.Effects_Races.Effects.BaseEffect;
import Model.Cards.Effects_Races.Effects.FireEffect;
import Model.Cards.Effects_Races.Effects.IEffect;
import Model.Cards.Effects_Races.Races.BaseRace;
import Model.Cards.Effects_Races.Races.IRace;
import Model.Cards.MonsterCard;
import Model.User.Credentials;
import Model.User.Trade.ITrade;
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
public class TestPostgresBattleRepository {

    User userA = new User(new Credentials("user", "pw"));
    User userB = new User(new Credentials("user2", "pw"));


    static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://172.17.0.2:5432/mtcg_testing","postgres", "postgres");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    static IBattleRepository battleRepository;
    static IUserRepository userRepository ;
    static IACardRepository cardRepository;
    static IEffectRepository effectRepository;
    static IRaceRepository iRaceRepository;
    static ITradeRepository iTradeRepository;

    public TestPostgresBattleRepository() {
    }

    @BeforeEach()
    void SetupConnection() {

        userRepository = new PostgresUserRepository(connection);
        cardRepository = new PostgresACardRepository(connection);
        effectRepository = new PostgresIEffectRepository(connection,false);
        iRaceRepository = new PostgresIRaceRepository(connection,false);
        iTradeRepository = new PostgresITradeRepository(connection);
        battleRepository = new PostgresBattleRepository(connection);

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
    @DisplayName("Create Battle")
    void testCreateBattle() {
        // arrange
        IEffect baseEf= new BaseEffect();
        baseEf.setId(effectRepository.persistEntity(baseEf));
        IEffect fireEffect= new FireEffect(baseEf);
        fireEffect.setId(effectRepository.persistEntity(fireEffect));
        IRace baseRace = new BaseRace();
        baseRace.setId(iRaceRepository.persistEntity(baseRace));

        ACard card = new MonsterCard("Fireelf", 20, fireEffect,baseRace);
        card.setId(cardRepository.persistEntity(card));
        userA.getDeck().getCards().add(card);
        userA.setId(userRepository.persistEntity(userA));

        ACard card2 = new MonsterCard("Fireelf2", 20, fireEffect,baseRace);
        card2.setId(cardRepository.persistEntity(card2));
        userB.getDeck().getCards().add(card2);
        userB.setId(userRepository.persistEntity(userB));

        Battle battle = new Battle();
        battle.setUser1(userA);
        battle.setUser2(userB);
        battle.start();
        // act

        Long id = battleRepository.persistEntity(battle);
        // assert
        assertEquals(1, id);
    }

    @Test
    @DisplayName("Find Battle")
    void testFindBattle() {
        // arrange
        IEffect baseEf= new BaseEffect();
        baseEf.setId(effectRepository.persistEntity(baseEf));
        IEffect fireEffect= new FireEffect(baseEf);
        fireEffect.setId(effectRepository.persistEntity(fireEffect));
        IRace baseRace = new BaseRace();
        baseRace.setId(iRaceRepository.persistEntity(baseRace));

        ACard card = new MonsterCard("Fireelf", 20, fireEffect,baseRace);
        card.setId(cardRepository.persistEntity(card));
        userA.getDeck().getCards().add(card);
        userA.setId(userRepository.persistEntity(userA));

        ACard card2 = new MonsterCard("Fireelf2", 20, fireEffect,baseRace);
        card2.setId(cardRepository.persistEntity(card2));
        userB.getDeck().getCards().add(card2);
        userB.setId(userRepository.persistEntity(userB));

        Battle battle = new Battle();
        battle.setUser1(userA);
        battle.setUser2(userB);
        battle.start();
        battle.setId(battleRepository.persistEntity(battle));

        // act
        Battle foundBattle = battleRepository.findEntity(battle.getId());

        // assert
        assertEquals(foundBattle.getUser1().getId(), userA.getId());
    }

    @Test
    @DisplayName("Update Battle")
    void testUpdateBattle() {
        // arrange
        IEffect baseEf= new BaseEffect();
        baseEf.setId(effectRepository.persistEntity(baseEf));
        IEffect fireEffect= new FireEffect(baseEf);
        fireEffect.setId(effectRepository.persistEntity(fireEffect));
        IRace baseRace = new BaseRace();
        baseRace.setId(iRaceRepository.persistEntity(baseRace));

        ACard card = new MonsterCard("Fireelf", 20, fireEffect,baseRace);
        card.setId(cardRepository.persistEntity(card));
        userA.getDeck().getCards().add(card);
        userA.setId(userRepository.persistEntity(userA));

        ACard card2 = new MonsterCard("Fireelf2", 20, fireEffect,baseRace);
        card2.setId(cardRepository.persistEntity(card2));
        userB.getDeck().getCards().add(card2);
        userB.setId(userRepository.persistEntity(userB));

        Battle battle = new Battle();
        battle.setUser1(userA);
        battle.setUser2(userB);
        battle.start();
        battle.setId(battleRepository.persistEntity(battle));
        battle.setWinner(userA);
        //act

        boolean works = battleRepository.updateEntity(battle);
        Battle foundBattle = battleRepository.findEntity(battle.getId());

        // assert
        assertTrue(works);
        assertNotNull(foundBattle.getWinner());

    }

    @Test
    @DisplayName("Update Battle Wrong ID")
    void testUpdateBattleWrongId() {
        // arrange
        IEffect baseEf= new BaseEffect();
        baseEf.setId(effectRepository.persistEntity(baseEf));
        IEffect fireEffect= new FireEffect(baseEf);
        fireEffect.setId(effectRepository.persistEntity(fireEffect));
        IRace baseRace = new BaseRace();
        baseRace.setId(iRaceRepository.persistEntity(baseRace));

        ACard card = new MonsterCard("Fireelf", 20, fireEffect,baseRace);
        card.setId(cardRepository.persistEntity(card));
        userA.getDeck().getCards().add(card);
        userA.setId(userRepository.persistEntity(userA));

        ACard card2 = new MonsterCard("Fireelf2", 20, fireEffect,baseRace);
        card2.setId(cardRepository.persistEntity(card2));
        userB.getDeck().getCards().add(card2);
        userB.setId(userRepository.persistEntity(userB));

        Battle battle = new Battle();
        battle.setUser1(userA);
        battle.setUser2(userB);
        battle.start();
        battle.setId(battleRepository.persistEntity(battle));
        battle.setWinner(userA);
        battle.setId(5000L);
        //act

        boolean works = battleRepository.updateEntity(battle);

        // assert
        assertFalse(works);
    }

    @Test
    @DisplayName("Delete Battle")
    void testDeleteBattle() {
        // arrange
        IEffect baseEf= new BaseEffect();
        baseEf.setId(effectRepository.persistEntity(baseEf));
        IEffect fireEffect= new FireEffect(baseEf);
        fireEffect.setId(effectRepository.persistEntity(fireEffect));
        IRace baseRace = new BaseRace();
        baseRace.setId(iRaceRepository.persistEntity(baseRace));

        ACard card = new MonsterCard("Fireelf", 20, fireEffect,baseRace);
        card.setId(cardRepository.persistEntity(card));
        userA.getDeck().getCards().add(card);
        userA.setId(userRepository.persistEntity(userA));

        ACard card2 = new MonsterCard("Fireelf2", 20, fireEffect,baseRace);
        card2.setId(cardRepository.persistEntity(card2));
        userB.getDeck().getCards().add(card2);
        userB.setId(userRepository.persistEntity(userB));

        Battle battle = new Battle();
        battle.setUser1(userA);
        battle.setUser2(userB);
        battle.start();
        battle.setId(battleRepository.persistEntity(battle));
        battle.setWinner(userA);

        // act
        boolean works = battleRepository.deleteEntity(battle.getId());
        Battle foundBattle = battleRepository.findEntity(battle.getId());

        // assert
        assertTrue(works);
        assertNull(foundBattle);
    }

    @Test
    @DisplayName("Delete Battle Wrong ID")
    void testDeleteBattleWrongId() {
        // arrange

        // act
        boolean works = battleRepository.deleteEntity(5000L);

        // assert
        assertFalse(works);
    }


}