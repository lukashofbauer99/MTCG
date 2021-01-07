package Domain.Battle.DataBase.Postgres;

import Domain.Battle.Interfaces.IBattleRepository;
import Model.Battle.Battle;
import Model.Battle.Round;
import Model.Battle.RoundOutcome;
import Model.Cards.ACard;
import Model.Cards.Effects_Races.Effects.IEffect;
import Model.Cards.Effects_Races.Races.IRace;
import Model.Cards.MonsterCard;
import Model.User.Credentials;
import Model.User.EditableUserData;
import Model.User.User;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostgresBattleRepository implements IBattleRepository {

    private Connection _connection;

    public PostgresBattleRepository(Connection connection) {
        this._connection = connection;
    }

    @Override
    public Long persistEntity(Battle entity) {
        Long id = null;
        try {
            PreparedStatement statement;
            if (entity.getWinner()!=null) {
                statement = _connection.prepareStatement("""
                    INSERT 
                    INTO
                    battles
                    (user1, user2, winner)
                    VALUES 
                    (
                    ?,?,?
                    )
                    returning id
                    """);
            statement.setLong(1, entity.getUser1().getId());
            statement.setLong(2, entity.getUser2().getId());
            statement.setLong(3, entity.getWinner().getId());
            }
            else
            {
                statement = _connection.prepareStatement("""
                    INSERT 
                    INTO
                    battles
                    (user1, user2)
                    VALUES 
                    (
                    ?,?
                    )
                    returning id
                    """);
                statement.setLong(1, entity.getUser1().getId());
                statement.setLong(2, entity.getUser2().getId());
            }
            //statement.execute();

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            id = resultSet.getLong(1);
            entity.setId(id);

            //region rounds

            for (Round item : entity.getRounds()) {
                PreparedStatement statement3 = _connection.prepareStatement("""
                        INSERT
                        INTO rounds(roundoutcome, winnerdmg, looserdmg, winnercardid, loosercardid, battleid)
                        VALUES(?,?,?,?,?,?)
                        """);
                statement3.setString(1, item.getRoundOutcome().toString());
                statement3.setDouble(2, item.getWinnerDmg());
                statement3.setDouble(3, item.getLooserDmg());
                statement3.setString(4, item.getWinner().getId());
                statement3.setString(5, item.getLooser().getId());
                statement3.setLong(6, entity.getId());
                statement3.execute();
            }
            //endregion


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return id;
    }

    @Override
    public boolean updateEntity(Battle entity) {
        try {
            //region user data
            PreparedStatement statement = _connection.prepareStatement("""
                    Update 
                    battles
                    SET
                    user1=?, user2=?, winner=?
                    WHERE id=?
                    RETURNING id
                    """);
            statement.setLong(1, entity.getUser1().getId());
            statement.setLong(2, entity.getUser2().getId());
            statement.setLong(3, entity.getWinner().getId());
            statement.setLong(4, entity.getId());
            //statement.execute();

            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next())
                return false;
            //endregion


        } catch (SQLException throwables) {
            return false;
        }
        return true;
    }

    @Override
    public Battle findEntity(Long id) {
        Battle battle = null;
        try {
            //region battle data
            PreparedStatement statement = _connection.prepareStatement("""
                    SELECT 
                    id, 
                    user1,
                    user2,
                    winner
                    FROM battles 
                    where id=?
                    """);

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                battle = new Battle(
                        resultSet.getLong(1),
                        findUser(resultSet.getLong(2)),
                        findUser(resultSet.getLong(3)),
                        findUser(resultSet.getLong(4))
                );
                //endregion
                //region rounds
                PreparedStatement statementStack = _connection.prepareStatement("""
                        SELECT 
                        id,
                        roundoutcome,
                        winnerdmg,
                        looserdmg,
                        winnercardid,
                        loosercardid
                        FROM rounds 
                        where battleid=?
                        """);

                statementStack.setLong(1, id);
                ResultSet resultSetRounds = statementStack.executeQuery();
                while (resultSetRounds.next()) {
                    Round round = new Round(
                            resultSetRounds.getLong(1),
                            RoundOutcome.valueOf(resultSetRounds.getString(2)),
                            resultSetRounds.getDouble(3),
                            resultSetRounds.getDouble(4),
                            findCard(resultSetRounds.getString(5)),
                            findCard(resultSetRounds.getString(6))
                    );
                    battle.getRounds().add(round);
                }
                //endregion

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return battle;
    }

    @Override
    public boolean deleteEntity(Long id) {

        PreparedStatement statement;
        try {
            statement = _connection.prepareStatement("""
                    DELETE
                    FROM battles
                    WHERE id=?
                    returning id
                    """);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next())
                return false;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<Battle> getAllEntities() {
        List<Battle> battles = new ArrayList<>();
        try {
            //region battle data
            PreparedStatement statement = _connection.prepareStatement("""
                    SELECT 
                    id, 
                    user1,
                    user2,
                    winner
                    FROM battles 
                    """);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Battle battle = new Battle(
                        resultSet.getLong(1),
                        findUser(resultSet.getLong(2)),
                        findUser(resultSet.getLong(3)),
                        findUser(resultSet.getLong(4))
                );
                //endregion
                //region rounds
                PreparedStatement statementStack = _connection.prepareStatement("""
                        SELECT 
                        id
                        roundoutcome,
                        winnerdmg,
                        looserdmg,
                        winnercardid,
                        loosercardid
                        FROM rounds 
                        where battleid=?
                        """);

                statementStack.setLong(1, battle.getId());
                ResultSet resultSetRounds = statementStack.executeQuery();
                while (resultSetRounds.next()) {
                    Round round = new Round(
                            resultSetRounds.getLong(1),
                            RoundOutcome.valueOf(resultSetRounds.getString(resultSetRounds.getString(2))),
                            resultSetRounds.getDouble(3),
                            resultSetRounds.getDouble(4),
                            findCard(resultSetRounds.getString(5)),
                            findCard(resultSetRounds.getString(6))
                    );
                    battle.getRounds().add(round);
                }
                //endregion
                battles.add(battle);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return battles;
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
