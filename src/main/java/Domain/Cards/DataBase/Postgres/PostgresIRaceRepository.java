package Domain.Cards.DataBase.Postgres;

import Domain.Cards.Interfaces.IRaceRepository;
import Model.Cards.Effects_Races.Effects.*;
import Model.Cards.Effects_Races.Races.*;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostgresIRaceRepository implements IRaceRepository {

    private Connection _connection = null;

    public PostgresIRaceRepository(Connection connection,Boolean initVals) {
        this._connection = connection;
        if(initVals) {
            IRace base = new BaseRace();
            persistEntity(base);
            persistEntity(new GoblinRace(base));
            persistEntity(new DragonRace(base));
            persistEntity(new WizardRace(base));
            persistEntity(new OrkRace(base));
            persistEntity(new KrakenRace(base));
            persistEntity(new FireElfRace(base));
            persistEntity(new KnightRace(base));
        }
    }


    @Override
    public synchronized Long persistEntity(IRace entity) {

        Long id = null;
        PreparedStatement statement;
        try {
            //if baseRace exists
            if (entity.getBase() != null) {
                statement = _connection.prepareStatement("""
                        INSERT 
                        INTO
                        races
                        ( type, name, baseraceid)
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
                        races
                        ( type, name)
                        VALUES 
                        (
                        ?,?
                        )
                        returning id
                        """);
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
    public synchronized boolean updateEntity(IRace entity) {
        PreparedStatement statement;
        try {
            if (entity.getBase() != null) {
                statement = _connection.prepareStatement("""
                        Update 
                        races
                        SET
                        type=?, name=?, baseraceid=?
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
                        races
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
    public IRace findEntity(Long id) {
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
                race.setBase(findEntity(resultSet.getLong(4)));
            }
        } catch (SQLException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException throwables) {
            throwables.printStackTrace();
        }
        return race;
    }

    @Override
    public synchronized boolean deleteEntity(Long id) {
        PreparedStatement statement;
        try {
            statement = _connection.prepareStatement("""
                    DELETE
                    FROM races
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
    public List<IRace> getAllEntities() {
        List<IRace> races = new ArrayList<>();
        try {
            PreparedStatement statement = _connection.prepareStatement("""
                    SELECT 
                    id, 
                    type,
                    name,
                    baseraceid
                    FROM races 
                    """);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Class raceClass = Class.forName(resultSet.getString(2));
                IRace race = (IRace) ((Class<?>) raceClass).getDeclaredConstructor().newInstance();
                race.setId(resultSet.getLong(1));
                race.setName(resultSet.getString(3));
                resultSet.getLong(4);
                race.setBase(findEntity(resultSet.getLong(4)));
                races.add(race);
            }
        } catch (SQLException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException throwables) {
            throwables.printStackTrace();
        }
        return races;
    }

    @Override
    public IRace getIRaceWithName(String Name) {
        IRace race = null;
        try {
            PreparedStatement statement = _connection.prepareStatement("""
                    SELECT 
                    id, 
                    type,
                    name,
                    baseraceid
                    FROM races 
                    where name =?
                    """);

            statement.setString(1, Name);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Class effectClass = Class.forName(resultSet.getString(2));
                race = (IRace) ((Class<?>) effectClass).getDeclaredConstructor().newInstance();
                race.setId(resultSet.getLong(1));
                race.setName(resultSet.getString(3));
                resultSet.getLong(4);
                race.setBase(findEntity(resultSet.getLong(4)));
            }
        } catch (SQLException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException throwables) {
            throwables.printStackTrace();
        }
        return race;
    }

}
