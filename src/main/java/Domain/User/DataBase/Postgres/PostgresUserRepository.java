package Domain.User.DataBase.Postgres;

import Domain.User.Interfaces.IUserRepository;
import Model.Cards.ACard;
import Model.Cards.Effects_Races.Effects.IEffect;
import Model.Cards.Effects_Races.Races.IRace;
import Model.Cards.MonsterCard;
import Model.Cards.SpellCard;
import Model.User.Credentials;
import Model.User.Deck;
import Model.User.EditableUserData;
import Model.User.User;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//TODO: Clean up code, use private Methods, Test, Implement PostgresITradeRepository
public class PostgresUserRepository implements IUserRepository {

    @AllArgsConstructor
    class entry {
        public Long userid;
        public String cardid;
    }

    private Connection _connection = null;

    public PostgresUserRepository(Connection connection) {
        this._connection = connection;
    }

    @Override
    public String loginUser(Credentials cred) {
        return null;
    }

    @Override
    public Boolean UserLoggedIn(String token) {
        return null;
    }

    @Override
    public List<ACard> getCardsOfUserWithToken(String token) {
        return null;
    }

    @Override
    public Deck getDeckOfUserWithToken(String token) {
        return null;
    }

    @Override
    public User getUserWithToken(String token) {
        return null;
    }

    @Override
    public User getUserWithUsername(String username) {
        return null;
    }

    @Override
    public Long persistEntity(User entity) {
        Long id = null;
        try {
            PreparedStatement statement = _connection.prepareStatement("""
                    INSERT 
                    INTO
                    users
                    ( username, password, coins,mmr,name,image,bio)
                    VALUES 
                    (
                    ?,?,?,?,?,?,?
                    )
                    returning id
                    """);
            statement.setString(1, entity.getCredentials().getUsername());
            statement.setString(2, entity.getCredentials().getPassword());
            statement.setInt(3, entity.getCoins());
            statement.setInt(4, entity.getMmr());
            statement.setString(5, entity.getEditableUserData().getName());
            statement.setString(6, entity.getEditableUserData().getImage());
            statement.setString(7, entity.getEditableUserData().getBio());
            //statement.execute();

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            id = resultSet.getLong(1);
            entity.setId(id);

            //region stack
            List<entry> stackEntriesMemory = entity.getStack().getCards().stream().map(x -> new entry(entity.getId(), x.getId())).collect(Collectors.toList());

            for (entry item : stackEntriesMemory) {
                PreparedStatement statement3 = _connection.prepareStatement("""
                        INSERT
                        INTO Stacks(userid,cardid)
                        VALUES(?,?)
                        """);
                statement3.setLong(1, item.userid);
                statement3.setString(2, item.cardid);
                statement3.execute();
            }
            //endregion

            //region deck
            List<entry> deckEntriesMemory = entity.getDeck().getCards().stream().map(x -> new entry(entity.getId(), x.getId())).collect(Collectors.toList());

            for (entry item : deckEntriesMemory) {
                PreparedStatement statement3 = _connection.prepareStatement("""
                        INSERT
                        INTO Decks(userid,cardid)
                        VALUES(?,?)
                        """);
                statement3.setLong(1, item.userid);
                statement3.setString(2, item.cardid);
                statement3.execute();
            }
            //endregion


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return id;
    }

    @Override
    public boolean updateEntity(User entity) {
        try {
            //region user data
            PreparedStatement statement = _connection.prepareStatement("""
                    Update 
                    users
                    SET
                    username=?, password=?, coins=?,mmr=?,name=?,image=?,bio=?
                    WHERE id=?
                    RETURNING id
                    """);
            statement.setString(1, entity.getCredentials().getUsername());
            statement.setString(2, entity.getCredentials().getPassword());
            statement.setInt(3, entity.getCoins());
            statement.setInt(4, entity.getMmr());
            statement.setString(5, entity.getEditableUserData().getName());
            statement.setString(6, entity.getEditableUserData().getImage());
            statement.setString(7, entity.getEditableUserData().getBio());
            statement.setLong(8, entity.getId());
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next())
                return false;
            //endregion

            //region stack
            PreparedStatement statementStack = _connection.prepareStatement("""
                    Select 
                    userid,
                    cardid
                    FROM Stacks
                    WHERE userid=?
                    """);
            statementStack.setLong(1, entity.getId());
            ResultSet resultSetStack = statementStack.executeQuery();
            List<entry> stackEntriesDB = new ArrayList<>();
            while (resultSetStack.next()) {
                stackEntriesDB.add(new entry(resultSet.getLong(1), resultSet.getString(2)));
            }
            List<entry> stackEntriesMemory = entity.getStack().getCards().stream().map(x -> new entry(entity.getId(), x.getId())).collect(Collectors.toList());


            List<entry> toDeleteStack = stackEntriesDB.stream().filter(x -> !stackEntriesMemory.contains(x)).collect(Collectors.toList());
            for (entry item : toDeleteStack) {
                PreparedStatement statement3 = _connection.prepareStatement("""
                        DELETE
                        FROM Stacks
                        WHERE userid=? AND cardid=?
                        """);
                statement3.setLong(1, item.userid);
                statement3.setString(2, item.cardid);
                statement3.execute();
            }

            List<entry> toInsertStack = stackEntriesMemory.stream().filter(x -> !stackEntriesDB.contains(x)).collect(Collectors.toList());
            for (entry item : toInsertStack) {
                PreparedStatement statement3 = _connection.prepareStatement("""
                        INSERT
                        INTO Stacks(userid,cardid)
                        VALUES(?,?)
                        """);
                statement3.setLong(1, item.userid);
                statement3.setString(2, item.cardid);
                statement3.execute();
            }
            //endregion

            //region deck
            PreparedStatement statementDeck = _connection.prepareStatement("""
                    Select 
                    userid,
                    cardid
                    FROM Decks
                    WHERE userid=?
                    """);
            statementDeck.setLong(1, entity.getId());
            ResultSet resultSetDeck = statementDeck.executeQuery();
            List<entry> deckEntriesDB = new ArrayList<>();
            while (resultSetDeck.next()) {
                deckEntriesDB.add(new entry(resultSet.getLong(1), resultSet.getString(2)));
            }
            List<entry> deckEntriesMemory = entity.getDeck().getCards().stream().map(x -> new entry(entity.getId(), x.getId())).collect(Collectors.toList());


            List<entry> toDeleteDeck = deckEntriesDB.stream().filter(x -> !deckEntriesMemory.contains(x)).collect(Collectors.toList());
            for (entry item : toDeleteDeck) {
                PreparedStatement statement3 = _connection.prepareStatement("""
                        DELETE
                        FROM Decks
                        WHERE userid=? AND cardid=?
                        """);
                statement3.setLong(1, item.userid);
                statement3.setString(2, item.cardid);
                statement3.execute();
            }

            List<entry> toInsertDeck = deckEntriesMemory.stream().filter(x -> !deckEntriesDB.contains(x)).collect(Collectors.toList());
            for (entry item : toInsertDeck) {
                PreparedStatement statement3 = _connection.prepareStatement("""
                        INSERT
                        INTO Decks(userid,cardid)
                        VALUES(?,?)
                        """);
                statement3.setLong(1, item.userid);
                statement3.setString(2, item.cardid);
                statement3.execute();
            }
            //endregion


        } catch (SQLException throwables) {
            return false;
        }
        return true;
    }

    @Override
    public User findEntity(Long id) { //TODO: check if cards and effects are added to user and cards | add cards of deck and effecs and races
        User user = null;
        try {
            //region user data
            PreparedStatement statement = _connection.prepareStatement("""
                    SELECT 
                    id, 
                    username,
                    password,
                    coins,
                    mmr,
                    name,
                    image,
                    bio
                    FROM users 
                    where id=?
                    """);

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                user = new User(
                        resultSet.getLong(1),
                        new Credentials(resultSet.getString(2), resultSet.getString(3)),
                        resultSet.getInt(4),
                        resultSet.getInt(5),
                        new EditableUserData(resultSet.getString(6), resultSet.getString(7), resultSet.getString(8))
                );
            }
            //endregion
            //region stack
            PreparedStatement statementStack = _connection.prepareStatement("""
                    SELECT 
                    cs.type,
                    cs.id,
                    cs.name,
                    cs.damage,
                    e.type,
                    cs.effectid,
                    e.name,
                    e.baseeffectid,
                    r.type,
                    cs.raceid,
                    r.name,
                    r.baseraceid
                    FROM stacks s
                    JOIN cards cs on cs.id = s.cardid
                    JOIN effects e on e.id = cs.effectID
                    JOIN races r on r.id = cs.raceID
                    where s.userid=?
                    """);

            statementStack.setLong(1, id);
            ResultSet resultSetStack = statementStack.executeQuery();
            while (resultSetStack.next()) {
                //Class.forName("Model.Cards.MonsterCard");
                ACard card;
                Class cardClass = Class.forName(resultSetStack.getString(1));
                if (cardClass == SpellCard.class) {
                    card = new MonsterCard(
                            resultSetStack.getString(2),
                            resultSetStack.getString(3),
                            resultSetStack.getDouble(4)

                    );

                    //region Race
                    Class raceClass = Class.forName(resultSetStack.getString(9));
                    IRace topRace = (IRace) ((Class<?>) raceClass).getDeclaredConstructor().newInstance();
                    topRace.setId(resultSet.getLong(10));
                    topRace.setName(resultSet.getString(11));

                    if (resultSet.getString(9) != null) {
                        IRace currentRace;
                        PreparedStatement outerStatementEffect = _connection.prepareStatement("""
                                    SELECT 
                                    type,
                                    id,
                                    name,
                                    baseraceid
                                    FROM races
                                    where id=?
                                """);

                        outerStatementEffect.setLong(1, resultSet.getLong(8));
                        ResultSet outerResultSetRace = outerStatementEffect.executeQuery();
                        Class outerRaceClass = Class.forName(outerResultSetRace.getString(1));
                        currentRace = (IRace) ((Class<?>) outerRaceClass).getDeclaredConstructor().newInstance();
                        currentRace.setId(outerResultSetRace.getLong(2));
                        currentRace.setName(outerResultSetRace.getString(3));
                        topRace.setBase(currentRace);
                        while (currentRace.getBase() != null) {
                            PreparedStatement innerStatementEffect = _connection.prepareStatement("""
                                        SELECT 
                                        type,
                                        id,
                                        name,
                                        baseraceid
                                        FROM races
                                        where id=?
                                    """);

                            innerStatementEffect.setLong(1, resultSet.getLong(8));
                            ResultSet innerResultSetEffect = innerStatementEffect.executeQuery();
                            Class innerEffectClass = Class.forName(innerResultSetEffect.getString(1));
                            currentRace = (IRace) ((Class<?>) innerEffectClass).getDeclaredConstructor().newInstance();
                            currentRace.setId(innerResultSetEffect.getLong(2));
                            currentRace.setName(innerResultSetEffect.getString(3));
                        }
                    }
                    //endregion
                } else {
                    card = new SpellCard(
                            resultSetStack.getString(2),
                            resultSetStack.getString(3),
                            resultSetStack.getDouble(4)
                    );
                }

                //region Effect
                Class effectClass = Class.forName(resultSetStack.getString(5));
                IEffect topEffect = (IEffect) ((Class<?>) effectClass).getDeclaredConstructor().newInstance();
                topEffect.setId(resultSet.getLong(6));
                topEffect.setName(resultSet.getString(7));

                if (resultSet.getString(8) != null) {
                    IEffect currentEffect = null;
                    PreparedStatement outerStatementEffect = _connection.prepareStatement("""
                                SELECT 
                                type,
                                id,
                                name,
                                baseeffectid
                                FROM effects
                                where id=?
                            """);

                    outerStatementEffect.setLong(1, resultSet.getLong(8));
                    ResultSet outerResultSetEffect = outerStatementEffect.executeQuery();
                    Class outerEffectClass = Class.forName(outerResultSetEffect.getString(1));
                    currentEffect = (IEffect) ((Class<?>) outerEffectClass).getDeclaredConstructor().newInstance();
                    currentEffect.setId(outerResultSetEffect.getLong(2));
                    currentEffect.setName(outerResultSetEffect.getString(3));
                    topEffect.setBase(currentEffect);
                    while (currentEffect.getBase() != null) {
                        PreparedStatement innerStatementEffect = _connection.prepareStatement("""
                                    SELECT 
                                    type,
                                    id,
                                    name,
                                    baseeffectid
                                    FROM effects
                                    where id=?
                                """);

                        innerStatementEffect.setLong(1, resultSet.getLong(8));
                        ResultSet innerResultSetEffect = innerStatementEffect.executeQuery();
                        Class innerEffectClass = Class.forName(innerResultSetEffect.getString(1));
                        currentEffect = (IEffect) ((Class<?>) innerEffectClass).getDeclaredConstructor().newInstance();
                        currentEffect.setId(innerResultSetEffect.getLong(2));
                        currentEffect.setName(innerResultSetEffect.getString(3));
                    }
                }
                //endregion
            }
            //endregion
            //region deck
            PreparedStatement statementDeck = _connection.prepareStatement("""
                    SELECT 
                    cs.type,
                    cs.id,
                    cs.name,
                    cs.damage,
                    e.type,
                    cs.effectid,
                    e.name,
                    e.baseeffectid,
                    r.type,
                    cs.raceid,
                    r.name,
                    r.baseraceid
                    FROM decks d
                    JOIN cards cs on cs.id = d.cardid
                    JOIN effects e on e.id = cs.effectID
                    JOIN races r on r.id = cs.raceID
                    where d.userid=?
                    """);

            statementDeck.setLong(1, id);
            ResultSet resultSetDeck = statementDeck.executeQuery();
            while (resultSetDeck.next()) {
                //Class.forName("Model.Cards.MonsterCard");
                ACard card;
                Class cardClass = Class.forName(resultSetDeck.getString(1));
                if (cardClass == SpellCard.class) {
                    card = new MonsterCard(
                            resultSetDeck.getString(2),
                            resultSetDeck.getString(3),
                            resultSetDeck.getDouble(4)

                    );

                    //region Race
                    Class raceClass = Class.forName(resultSetDeck.getString(9));
                    IRace topRace = (IRace) ((Class<?>) raceClass).getDeclaredConstructor().newInstance();
                    topRace.setId(resultSet.getLong(10));
                    topRace.setName(resultSet.getString(11));

                    if (resultSet.getString(9) != null) {
                        IRace currentRace;
                        PreparedStatement outerStatementEffect = _connection.prepareStatement("""
                                    SELECT 
                                    type,
                                    id,
                                    name,
                                    baseraceid
                                    FROM races
                                    where id=?
                                """);

                        outerStatementEffect.setLong(1, resultSet.getLong(8));
                        ResultSet outerResultSetRace = outerStatementEffect.executeQuery();
                        Class outerRaceClass = Class.forName(outerResultSetRace.getString(1));
                        currentRace = (IRace) ((Class<?>) outerRaceClass).getDeclaredConstructor().newInstance();
                        currentRace.setId(outerResultSetRace.getLong(2));
                        currentRace.setName(outerResultSetRace.getString(3));
                        topRace.setBase(currentRace);
                        while (currentRace.getBase() != null) {
                            PreparedStatement innerStatementEffect = _connection.prepareStatement("""
                                        SELECT 
                                        type,
                                        id,
                                        name,
                                        baseraceid
                                        FROM races
                                        where id=?
                                    """);

                            innerStatementEffect.setLong(1, resultSet.getLong(8));
                            ResultSet innerResultSetEffect = innerStatementEffect.executeQuery();
                            Class innerEffectClass = Class.forName(innerResultSetEffect.getString(1));
                            currentRace = (IRace) ((Class<?>) innerEffectClass).getDeclaredConstructor().newInstance();
                            currentRace.setId(innerResultSetEffect.getLong(2));
                            currentRace.setName(innerResultSetEffect.getString(3));
                        }
                    }
                    //endregion
                } else {
                    card = new SpellCard(
                            resultSetDeck.getString(2),
                            resultSetDeck.getString(3),
                            resultSetDeck.getDouble(4)
                    );
                }

                //region Effect
                Class effectClass = Class.forName(resultSetDeck.getString(5));
                IEffect topEffect = (IEffect) ((Class<?>) effectClass).getDeclaredConstructor().newInstance();
                topEffect.setId(resultSet.getLong(6));
                topEffect.setName(resultSet.getString(7));

                if (resultSet.getString(8) != null) {
                    IEffect currentEffect;
                    PreparedStatement outerStatementEffect = _connection.prepareStatement("""
                                SELECT 
                                type,
                                id,
                                name,
                                baseeffectid
                                FROM effects
                                where id=?
                            """);

                    outerStatementEffect.setLong(1, resultSet.getLong(8));
                    ResultSet outerResultSetEffect = outerStatementEffect.executeQuery();
                    Class outerEffectClass = Class.forName(outerResultSetEffect.getString(1));
                    currentEffect = (IEffect) ((Class<?>) outerEffectClass).getDeclaredConstructor().newInstance();
                    currentEffect.setId(outerResultSetEffect.getLong(2));
                    currentEffect.setName(outerResultSetEffect.getString(3));
                    topEffect.setBase(currentEffect);
                    while (currentEffect.getBase() != null) {
                        PreparedStatement innerStatementEffect = _connection.prepareStatement("""
                                    SELECT 
                                    type,
                                    id,
                                    name,
                                    baseeffectid
                                    FROM effects
                                    where id=?
                                """);

                        innerStatementEffect.setLong(1, resultSet.getLong(8));
                        ResultSet innerResultSetEffect = innerStatementEffect.executeQuery();
                        Class innerEffectClass = Class.forName(innerResultSetEffect.getString(1));
                        currentEffect = (IEffect) ((Class<?>) innerEffectClass).getDeclaredConstructor().newInstance();
                        currentEffect.setId(innerResultSetEffect.getLong(2));
                        currentEffect.setName(innerResultSetEffect.getString(3));
                    }
                }
                //endregion
            }

            //endregion

        } catch (SQLException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }

    @Override
    public boolean deleteEntity(Long id) {

        PreparedStatement statement;
        try {
            statement = _connection.prepareStatement("""
                    DELETE
                    FROM users
                    WHERE id=?
                    """);
            statement.setLong(1, id);
            statement.execute();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<User> getAllEntities() {
        //region user data
        List<User> users = new ArrayList<>();
        PreparedStatement statement;
        try {
            statement = _connection.prepareStatement("""
                        SELECT 
                        id, 
                        username,
                        password,
                        coins,
                        mmr,
                        name,
                        image,
                        bio
                        FROM users 
                        """);


        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            users.add(new User(
                    resultSet.getLong(1),
                    new Credentials(resultSet.getString(2), resultSet.getString(3)),
                    resultSet.getInt(4),
                    resultSet.getInt(5),
                    new EditableUserData(resultSet.getString(6), resultSet.getString(7), resultSet.getString(8))
            ));
        }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //endregion
        return users;
    }
}
