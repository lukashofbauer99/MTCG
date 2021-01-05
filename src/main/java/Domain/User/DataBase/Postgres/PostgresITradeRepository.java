package Domain.User.DataBase.Postgres;

import Domain.User.Interfaces.ITradeRepository;
import Domain.User.Interfaces.IUserRepository;
import Model.Cards.ACard;
import Model.Cards.Effects_Races.Effects.IEffect;
import Model.Cards.Effects_Races.Races.IRace;
import Model.Cards.MonsterCard;
import Model.User.Credentials;
import Model.User.Deck;
import Model.User.EditableUserData;
import Model.User.Trade.ITrade;
import Model.User.Trade.NormalTradeCardRequirements;
import Model.User.Trade.Trade1To1;
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

public class PostgresITradeRepository implements ITradeRepository {
    Long currentID = 1L;

    @AllArgsConstructor
    class entry {
        public Long userid;
        public String cardid;
    }

    private Connection _connection = null;

    public PostgresITradeRepository(Connection connection) {
        this._connection = connection;
    }

    @Override
    public String persistEntity(ITrade entity) {
        String id = null;
        try {
            List<String> ids = new ArrayList<>();
            PreparedStatement statement = _connection.prepareStatement("""
                    SELECT 
                    id
                    FROM
                    cards
                    """);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ids.add(resultSet.getString(1));
            }
            while (ids.contains(currentID.toString())) {
                currentID++;
            }
            entity.setId(currentID.toString());

            statement = _connection.prepareStatement("""
                    INSERT 
                    INTO
                    normaltrades
                    (id, minimumdamage, cardtype, effectid, raceid, userid, cardid)
                    VALUES 
                    (
                    ?,?,?,?,?,?,?
                    )
                    returning id
                    """);
            statement.setString(1, currentID.toString());
            statement.setDouble(2, entity.getRequirements().getMinimumDamage());
            statement.setString(3, entity.getRequirements().getClass().getTypeName());
            statement.setLong(4, entity.getRequirements().getEffect().getId());
            statement.setLong(5, entity.getRequirements().getRace().getId());
            statement.setLong(6, entity.getUserOffer().getId());
            statement.setString(7, entity.getCardTradedFor().getId());

            ResultSet resultSet2 = statement.executeQuery();
            resultSet2.next();
            id = resultSet2.getString(1);
            entity.setId(id);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return id;
    }

    @Override
    public String persistEntityGenNoId(ITrade entity) {

        String id = null;
        PreparedStatement statement;
        try {
            if(entity.getRequirements().getEffect()!=null&&entity.getRequirements().getRace()!=null) {
                statement = _connection.prepareStatement("""
                    INSERT 
                    INTO
                    normaltrades
                    (id, minimumdamage, cardtype, effectid, raceid, userid, cardid)
                    VALUES 
                    (
                    ?,?,?,?,?,?,?
                    )
                    returning id
                    """);
            statement.setString(1,entity.getId());
            statement.setDouble(2, entity.getRequirements().getMinimumDamage());
            statement.setString(3, entity.getRequirements().getCardType().getTypeName());
            statement.setLong(4, entity.getRequirements().getEffect().getId());
            statement.setLong(5, entity.getRequirements().getRace().getId());
            statement.setLong(6, entity.getUserOffer().getId());
            statement.setString(7, entity.getCardTradedFor().getId());
            //statement.execute();

            ResultSet resultSet2 = statement.executeQuery();
            resultSet2.next();
            id = resultSet2.getString(1);
            entity.setId(id);
            }
            else if(entity.getRequirements().getEffect()!=null)
            {
                statement = _connection.prepareStatement("""
                    INSERT 
                    INTO
                    normaltrades
                    (id, minimumdamage, cardtype, effectid, userid, cardid)
                    VALUES 
                    (
                    ?,?,?,?,?,?
                    )
                    returning id
                    """);
                statement.setString(1,entity.getId());
                statement.setDouble(2, entity.getRequirements().getMinimumDamage());
                statement.setString(3, entity.getRequirements().getCardType().getTypeName());
                statement.setLong(4, entity.getRequirements().getEffect().getId());
                statement.setLong(5, entity.getUserOffer().getId());
                statement.setString(6, entity.getCardTradedFor().getId());
                //statement.execute();

                ResultSet resultSet2 = statement.executeQuery();
                resultSet2.next();
                id = resultSet2.getString(1);
                entity.setId(id);
            }
            else if(entity.getRequirements().getRace()!=null)
            {
                statement = _connection.prepareStatement("""
                    INSERT 
                    INTO
                    normaltrades
                    (id, minimumdamage, cardtype, raceid, userid, cardid)
                    VALUES 
                    (
                    ?,?,?,?,?,?
                    )
                    returning id
                    """);
                statement.setString(1,entity.getId());
                statement.setDouble(2, entity.getRequirements().getMinimumDamage());
                statement.setString(3, entity.getRequirements().getCardType().getTypeName());
                statement.setLong(4, entity.getRequirements().getRace().getId());
                statement.setLong(5, entity.getUserOffer().getId());
                statement.setString(6, entity.getCardTradedFor().getId());
                //statement.execute();

                ResultSet resultSet2 = statement.executeQuery();
                resultSet2.next();
                id = resultSet2.getString(1);
                entity.setId(id);
            }
            else
            {
                statement = _connection.prepareStatement("""
                    INSERT 
                    INTO
                    normaltrades
                    (id, minimumdamage, cardtype, userid, cardid)
                    VALUES 
                    (
                    ?,?,?,?,?
                    )
                    returning id
                    """);
                statement.setString(1,entity.getId());
                statement.setDouble(2, entity.getRequirements().getMinimumDamage());
                statement.setString(3, entity.getRequirements().getCardType().getTypeName());
                statement.setLong(4, entity.getUserOffer().getId());
                statement.setString(5, entity.getCardTradedFor().getId());
                //statement.execute();

                ResultSet resultSet2 = statement.executeQuery();
                resultSet2.next();
                id = resultSet2.getString(1);
                entity.setId(id);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return id;
    }

    @Override
    public boolean updateEntity(ITrade entity) {
        try {
            PreparedStatement statement = _connection.prepareStatement("""
                    Update 
                    normaltrades
                    SET
                    minimumdamage=?, cardtype=?, effectid=?, raceid=?, userid=?, cardid=?
                    WHERE id=?
                    RETURNING id
                    """);
            statement.setDouble(1, entity.getRequirements().getMinimumDamage());
            statement.setString(2, entity.getRequirements().getClass().getTypeName());
            statement.setLong(3, entity.getRequirements().getEffect().getId());
            statement.setLong(4, entity.getRequirements().getRace().getId());
            statement.setLong(5, entity.getUserOffer().getId());
            statement.setString(6, entity.getCardTradedFor().getId());
            statement.setString(7, entity.getId());
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next())
                return false;

        } catch (SQLException throwables) {
            return false;
        }
        return true;
    }

    @Override
    public ITrade findEntity(String id) {
        ITrade trade = null;
        try {
            PreparedStatement statement = _connection.prepareStatement("""
                    SELECT 
                    id, 
                    minimumdamage,
                    cardtype,
                    effectid,
                    raceid,
                    userid,
                    cardid
                    FROM normaltrades 
                    where id=?
                    """);

            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                trade = new Trade1To1(
                        resultSet.getString(1),
                        findUser(resultSet.getLong(6)),
                        findCard(resultSet.getString(7)),
                        new NormalTradeCardRequirements(
                                resultSet.getDouble(2),
                                Class.forName(resultSet.getString(3)),
                                findEffect(resultSet.getLong(4)),
                                findRace(resultSet.getLong(5)))
                        );
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return trade;
    }

    @Override
    public boolean deleteEntity(String id) {

        PreparedStatement statement;
        try {
            statement = _connection.prepareStatement("""
                    DELETE
                    FROM normaltrades
                    WHERE id=?
                    returning id
                    """);
            statement.setString(1, id);
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
    public List<ITrade> getAllEntities() {
        List<ITrade> trades = new ArrayList<>();
        try {
            PreparedStatement statement = _connection.prepareStatement("""
                    SELECT 
                    id, 
                    minimumdamage,
                    cardtype,
                    effectid,
                    raceid,
                    userid,
                    cardid
                    FROM normaltrades 
                    """);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ITrade trade = new Trade1To1(
                        resultSet.getString(1),
                        findUser(resultSet.getLong(6)),
                        findCard(resultSet.getString(7)),
                        new NormalTradeCardRequirements(
                                resultSet.getDouble(2),
                                Class.forName(resultSet.getString(3)),
                                findEffect(resultSet.getLong(4)),
                                findRace(resultSet.getLong(5)))
                );
                trades.add(trade);
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return trades;
    }

    public User findUser(Long id) {
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
