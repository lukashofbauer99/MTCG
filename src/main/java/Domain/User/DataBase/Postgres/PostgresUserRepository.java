package Domain.User.DataBase.Postgres;

import Domain.User.Interfaces.IUserRepository;
import Model.Cards.ACard;
import Model.Cards.Effects_Races.Effects.IEffect;
import Model.Cards.Effects_Races.Races.IRace;
import Model.Cards.MonsterCard;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//TODO: Clean up code, use private Methods, Test, Implement PostgresITradeRepository
public class PostgresUserRepository implements IUserRepository {

    Map<String, User> usersInSession = new HashMap<>();

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
        PreparedStatement statement;
        try {
            statement = _connection.prepareStatement("""
                    SELECT 
                    id
                    FROM users 
                    WHERE username=? AND password=?
                    """);


            statement.setString(1, cred.getUsername());
            statement.setString(2, cred.getPassword());
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
            {
                String sessionToken = "Basic " + cred.getUsername() + "-mtcgToken";
                if (!usersInSession.containsKey(sessionToken)) {
                    User us =findEntity(resultSet.getLong(1));
                    usersInSession.put(sessionToken, us);
                }
                return sessionToken;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public Boolean UserLoggedIn(String token) {
        return usersInSession.containsKey(token);
    }

    @Override
    public List<ACard> getCardsOfUserWithToken(String token) {
        User user = getUserWithToken(token);
        if (user != null) {
            List<ACard> cardsOfUser = findEntity(user.getId()).getStack().getCards();
            cardsOfUser.addAll(user.getDeck().getCards());
            return cardsOfUser;
        }
        return null;
    }

    @Override
    public Deck getDeckOfUserWithToken(String token) {
        User user = getUserWithToken(token);
        if (user != null) {
            return findEntity(user.getId()).getDeck();
        }
        return null;
    }

    @Override
    public User getUserWithToken(String token) {
        if (usersInSession.containsKey(token)) {
            return findEntity(usersInSession.get(token).getId());
        }
        return null;
    }

    @Override
    public User getUserWithUsername(String username) {
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
                    where username=?
                    """);

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                user = new User(
                        resultSet.getLong(1),
                        new Credentials(resultSet.getString(2), resultSet.getString(3)),
                        resultSet.getInt(4),
                        resultSet.getInt(5),
                        new EditableUserData(resultSet.getString(6), resultSet.getString(7), resultSet.getString(8)));

                //region stack
                PreparedStatement statementStack = _connection.prepareStatement("""
                        SELECT 
                        s.userid,
                        s.cardid
                        FROM stacks s
                        where s.userid=?
                        """);

                statementStack.setLong(1, user.getId());
                ResultSet resultSetStack = statementStack.executeQuery();
                while (resultSetStack.next()) {
                    user.getStack().getCards().add(findCard(resultSetStack.getString(2)));
                }
                //endregion
                //region deck
                PreparedStatement statementDeck = _connection.prepareStatement("""
                        SELECT 
                        d.userid,
                        d.cardid
                        FROM decks d
                        where d.userid=?
                        """);

                statementDeck.setLong(1, user.getId());
                ResultSet resultSetDeck = statementDeck.executeQuery();
                while (resultSetDeck.next()) {
                    user.getDeck().getCards().add(findCard(resultSet.getString(2)));
                }
                //endregion
            }
            //endregion

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
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
                stackEntriesDB.add(new entry(resultSetStack.getLong(1), resultSetStack.getString(2)));
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
    public User findEntity(Long id) {
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
                        new EditableUserData(resultSet.getString(6), resultSet.getString(7), resultSet.getString(8)));

                //region stack
                PreparedStatement statementStack = _connection.prepareStatement("""
                        SELECT 
                        s.userid,
                        s.cardid
                        FROM stacks s
                        where s.userid=?
                        """);

                statementStack.setLong(1, id);
                ResultSet resultSetStack = statementStack.executeQuery();
                while (resultSetStack.next()) {
                    user.getStack().getCards().add(findCard(resultSetStack.getString(2)));
                }
                //endregion
                //region deck
                PreparedStatement statementDeck = _connection.prepareStatement("""
                        SELECT 
                        d.userid,
                        d.cardid
                        FROM decks d
                        where d.userid=?
                        """);

                statementDeck.setLong(1, id);
                ResultSet resultSetDeck = statementDeck.executeQuery();
                while (resultSetDeck.next()) {
                    user.getDeck().getCards().add(findCard(resultSetDeck.getString(2)));
                }
                //endregion
            }
            //endregion

        } catch (SQLException throwables) {
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
                    returning id
                    """);
            statement.setLong(1, id);
            ResultSet resultSet= statement.executeQuery();
            if(!resultSet.next())
                return false;

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
                User user = new User(
                        resultSet.getLong(1),
                        new Credentials(resultSet.getString(2), resultSet.getString(3)),
                        resultSet.getInt(4),
                        resultSet.getInt(5),
                        new EditableUserData(resultSet.getString(6), resultSet.getString(7), resultSet.getString(8)));

                //region stack
                PreparedStatement statementStack = _connection.prepareStatement("""
                        SELECT 
                        s.userid,
                        s.cardid
                        FROM stacks s
                        where s.userid=?
                        """);

                statementStack.setLong(1, user.getId());
                ResultSet resultSetStack = statementStack.executeQuery();
                while (resultSetStack.next()) {
                    user.getStack().getCards().add(findCard(resultSetStack.getString(2)));
                }
                //endregion
                //region deck
                PreparedStatement statementDeck = _connection.prepareStatement("""
                        SELECT 
                        d.userid,
                        d.cardid
                        FROM decks d
                        where d.userid=?
                        """);

                statementDeck.setLong(1, user.getId());
                ResultSet resultSetDeck = statementDeck.executeQuery();
                while (resultSetDeck.next()) {
                    user.getDeck().getCards().add(findCard(resultSet.getString(2)));
                }
                //endregion
                users.add(user);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //endregion
        return users;
    }

    public ACard findCard(String id) {
        ACard card = null;
        try {
            PreparedStatement statement = _connection.prepareStatement("""
                    SELECT 
                    type,
                    id, 
                    name,
                    damage,
                    effectid,
                    raceid
                    FROM cards
                    where id=?
                    """);

            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Class cardClass = Class.forName(resultSet.getString(1));
                card = (ACard) ((Class<?>) cardClass).getDeclaredConstructor().newInstance();
                card.setId(resultSet.getString(2));
                card.setName(resultSet.getString(3));
                card.setDamage(resultSet.getDouble(4));
                card.setEffect(findEffect(resultSet.getLong(5)));

                if (card.getClass() == MonsterCard.class) {
                    ((MonsterCard) card).setRace(findRace(resultSet.getLong(6)));

                }
            }
        } catch (SQLException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException throwables) {
            throwables.printStackTrace();
        }
        return card;
    }

    private IEffect findEffect(Long id) {
        IEffect effect = null;
        try {
            PreparedStatement statement = _connection.prepareStatement("""
                    SELECT 
                    id, 
                    type,
                    name,
                    baseeffectid
                    FROM effects 
                    where id=?
                    """);

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Class effectClass = Class.forName(resultSet.getString(2));
                effect = (IEffect) ((Class<?>) effectClass).getDeclaredConstructor().newInstance();
                effect.setId(resultSet.getLong(1));
                effect.setName(resultSet.getString(3));
                resultSet.getLong(4);
                effect.setBase(findEffect(resultSet.getLong(4)));
            }
        } catch (SQLException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException throwables) {
            throwables.printStackTrace();
        }
        return effect;
    }

    private IRace findRace(Long id) {
        IRace race = null;
        try {
            PreparedStatement statement = _connection.prepareStatement("""
                    SELECT 
                    id, 
                    type,
                    name,
                    baseraceid
                    FROM races 
                    where id=?
                    """);

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Class raceClass = Class.forName(resultSet.getString(2));
                race = (IRace) ((Class<?>) raceClass).getDeclaredConstructor().newInstance();
                race.setId(resultSet.getLong(1));
                race.setName(resultSet.getString(3));
                resultSet.getLong(4);
                race.setBase(findRace(resultSet.getLong(4)));
            }
        } catch (SQLException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException throwables) {
            throwables.printStackTrace();
        }
        return race;
    }
}
