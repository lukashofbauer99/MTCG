package Domain.Cards.DataBase.Postgres;

import Domain.Cards.Interfaces.IACardRepository;
import Model.Cards.ACard;
import Model.Cards.Effects_Races.Effects.IEffect;
import Model.Cards.Effects_Races.Races.IRace;
import Model.Cards.MonsterCard;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostgresACardRepository implements IACardRepository {

    private Connection _connection = null;
    Long currentID = 1L;


    public PostgresACardRepository(Connection connection) {
        this._connection = connection;
    }


    @Override
    public synchronized String persistEntity(ACard entity) { //TODO Change everything to fit ACard, persist should already work

        String id = null;
        PreparedStatement statement;
        try {
            List<String> ids = new ArrayList<>();
            statement = _connection.prepareStatement("""
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

            if (entity.getClass() == MonsterCard.class) {
                statement = _connection.prepareStatement("""
                        INSERT 
                        INTO
                        cards
                        (id, type,damage, name, effectid,raceid)
                        VALUES 
                        (
                        ?,?,?,?,?,?
                        )
                        returning id
                        """);

                statement.setString(1, entity.getId());
                statement.setString(2, entity.getClass().getTypeName());
                statement.setDouble(3, entity.getDamage());
                statement.setString(4, entity.getName());
                statement.setLong(5, entity.getEffect().getId());
                statement.setLong(6, ((MonsterCard) entity).getRace().getId());
            } else {
                statement = _connection.prepareStatement("""
                        INSERT 
                        INTO
                        cards
                        (id, type,damage, name, effectid)
                        VALUES 
                        (
                        ?,?,?,?,?
                        )
                        returning id
                        """);

                statement.setString(1, entity.getId());
                statement.setString(2, entity.getClass().getTypeName());
                statement.setDouble(3, entity.getDamage());
                statement.setString(4, entity.getName());
                statement.setLong(5, entity.getEffect().getId());
            }
            resultSet = statement.executeQuery();
            resultSet.next();
            id = resultSet.getString(1);
            entity.setId(id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return id;
    }

    @Override
    public String persistEntityGenNoId(ACard card) {

        String id = null;
        PreparedStatement statement;
        try {
            if (card.getClass() == MonsterCard.class) {
                statement = _connection.prepareStatement("""
                        INSERT 
                        INTO
                        cards
                        (id, type,damage, name, effectid,raceid)
                        VALUES 
                        (
                        ?,?,?,?,?,?
                        )
                        returning id
                        """);

                statement.setString(1, card.getId());
                statement.setString(2, card.getClass().getTypeName());
                statement.setDouble(3, card.getDamage());
                statement.setString(4, card.getName());
                statement.setLong(5, card.getEffect().getId());
                statement.setLong(6, ((MonsterCard) card).getRace().getId());
            } else {
                statement = _connection.prepareStatement("""
                        INSERT 
                        INTO
                        cards
                        (id, type,damage, name, effectid)
                        VALUES 
                        (
                        ?,?,?,?,?
                        )
                        returning id
                        """);

                statement.setString(1, card.getId());
                statement.setString(2, card.getClass().getTypeName());
                statement.setDouble(3, card.getDamage());
                statement.setString(4, card.getName());
                statement.setLong(5, card.getEffect().getId());
            }
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            id = resultSet.getString(1);
            card.setId(id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return id;
    }

    @Override
    public synchronized boolean updateEntity(ACard entity) {
        PreparedStatement statement;
        try {
            if (entity.getClass() == MonsterCard.class) {
                statement = _connection.prepareStatement("""
                        Update 
                        cards
                        SET
                        damage=?, name=?,effectid=?,raceid=?
                        WHERE id=?
                        RETURNING id
                        """);


                statement.setDouble(1, entity.getDamage());
                statement.setString(2, entity.getName());
                statement.setLong(3, entity.getEffect().getId());
                statement.setLong(4, ((MonsterCard) entity).getRace().getId());
                statement.setString(5, entity.getId());
            } else {
                statement = _connection.prepareStatement("""
                        Update 
                        cards
                        SET
                        damage=?, name=?,effectid=?
                        WHERE id=?
                        RETURNING id
                        """);


                statement.setDouble(1, entity.getDamage());
                statement.setString(2, entity.getName());
                statement.setLong(3, entity.getEffect().getId());
                statement.setString(4, entity.getId());
            }
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next())
                return false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

    @Override
    public ACard findEntity(String id) {
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

    @Override
    public synchronized boolean deleteEntity(String id) {
        PreparedStatement statement;
        try {
            statement = _connection.prepareStatement("""
                    DELETE
                    FROM cards
                    WHERE id=?
                    returning id
                    """);
            statement.setString(1, id);
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
    public List<ACard> getAllEntities() {
        List<ACard> cards = new ArrayList<>();
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
                    """);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ACard card;
                Class cardClass = Class.forName(resultSet.getString(1));
                card = (ACard) ((Class<?>) cardClass).getDeclaredConstructor().newInstance();
                card.setId(resultSet.getString(2));
                card.setName(resultSet.getString(3));
                card.setDamage(resultSet.getDouble(4));
                card.setEffect(findEffect(resultSet.getLong(5)));

                if (card.getClass() == MonsterCard.class) {
                    ((MonsterCard) card).setRace(findRace(resultSet.getLong(6)));
                }
                cards.add(card);
            }
        } catch (SQLException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException throwables) {
            throwables.printStackTrace();
        }
        return cards;
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
