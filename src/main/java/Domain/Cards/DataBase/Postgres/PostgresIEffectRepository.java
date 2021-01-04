package Domain.Cards.DataBase.Postgres;

import Domain.Cards.Interfaces.IEffectRepository;
import Model.Cards.Effects_Races.Effects.*;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostgresIEffectRepository implements IEffectRepository {

    private Connection _connection = null;

    public PostgresIEffectRepository(Connection connection) {
        this._connection = connection;
    }

    public PostgresIEffectRepository() {
        IEffect base = new BaseEffect();
        persistEntity(base);
        persistEntity(new FireEffect(base));
        persistEntity(new WaterEffect(base));
        persistEntity(new NormalEffect(base));
    }

    @Override
    public synchronized Long persistEntity(IEffect entity) {

        Long id = null;
        PreparedStatement statement;
        try {
            //if baseeffect exists
            if (entity.getBase() != null) {
                statement = _connection.prepareStatement("""
                        INSERT 
                        INTO
                        effects
                        ( type, name, baseeffectid)
                        VALUES 
                        (
                        ?,?,?
                        )
                        returning id
                        """);

                statement.setString(1, entity.getClass().getTypeName());
                statement.setString(2, entity.getName());
                statement.setLong(3, entity.getBase().getId());
            } else {
                statement = _connection.prepareStatement("""
                        INSERT 
                        INTO
                        effects
                        ( type, name)
                        VALUES 
                        (
                        ?,?
                        )
                        returning id
                        """);
                System.out.println(entity.getClass().getTypeName());

                statement.setString(1, entity.getClass().getTypeName());
                statement.setString(2, entity.getName());
            }

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            id = resultSet.getLong(1);
            entity.setId(id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return id;
    }

    @Override
    public synchronized boolean updateEntity(IEffect entity) {
        PreparedStatement statement;
        try {
            if (entity.getBase() != null) {
                statement = _connection.prepareStatement("""
                        Update 
                        effects
                        SET
                        type=?, name=?, baseeffectid=?
                        WHERE id=?
                        RETURNING id
                        """);


                statement.setString(1, entity.getClass().getTypeName());
                statement.setString(2, entity.getName());
                statement.setLong(3, entity.getBase().getId());
                statement.setLong(4, entity.getId());
            } else {
                statement = _connection.prepareStatement("""
                        Update 
                        effects
                        SET
                        type=?, name=?
                        WHERE id=?
                        RETURNING id
                        """);


                statement.setString(1, entity.getClass().getTypeName());
                statement.setString(2, entity.getName());
                statement.setLong(3, entity.getId());
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
    public IEffect findEntity(Long id) {
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
                effect.setBase(findEntity(resultSet.getLong(4)));
            }
        } catch (SQLException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException throwables) {
            throwables.printStackTrace();
        }
        return effect;
    }

    @Override
    public synchronized boolean deleteEntity(Long id) {
        PreparedStatement statement;
        try {
            statement = _connection.prepareStatement("""
                    DELETE
                    FROM effects
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
    public List<IEffect> getAllEntities() {
        List<IEffect> effects = new ArrayList<>();
        try {
            PreparedStatement statement = _connection.prepareStatement("""
                    SELECT 
                    id, 
                    type,
                    name,
                    baseeffectid
                    FROM effects 
                    """);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Class effectClass = Class.forName(resultSet.getString(2));
                IEffect effect = (IEffect) ((Class<?>) effectClass).getDeclaredConstructor().newInstance();
                effect.setId(resultSet.getLong(1));
                effect.setName(resultSet.getString(3));
                resultSet.getLong(4);
                effect.setBase(findEntity(resultSet.getLong(4)));
                effects.add(effect);
            }
        } catch (SQLException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException throwables) {
            throwables.printStackTrace();
        }
        return effects;
    }

    @Override
    public IEffect getIEffectWithName(String Name) {
        IEffect effect = null;
        try {
            PreparedStatement statement = _connection.prepareStatement("""
                    SELECT 
                    id, 
                    type,
                    name,
                    baseeffectid
                    FROM effects 
                    where name =?
                    """);

            statement.setString(1, Name);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Class effectClass = Class.forName(resultSet.getString(2));
                effect = (IEffect) ((Class<?>) effectClass).getDeclaredConstructor().newInstance();
                effect.setId(resultSet.getLong(1));
                effect.setName(resultSet.getString(3));
                resultSet.getLong(4);
                effect.setBase(findEntity(resultSet.getLong(4)));
            }
        } catch (SQLException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException throwables) {
            throwables.printStackTrace();
        }
        return effect;
    }

}
