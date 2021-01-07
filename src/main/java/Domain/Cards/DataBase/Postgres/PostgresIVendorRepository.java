package Domain.Cards.DataBase.Postgres;

import Domain.Cards.Interfaces.IVendorRepository;
import Model.Cards.ACard;
import Model.Cards.CardPacks.ICardPack;
import Model.Cards.CardPacks.PackType;
import Model.Cards.Effects_Races.Effects.IEffect;
import Model.Cards.Effects_Races.Races.IRace;
import Model.Cards.MonsterCard;
import Model.Cards.Vendor.IVendor;
import Model.Cards.Vendor.NormalVendor;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PostgresIVendorRepository implements IVendorRepository {

    @AllArgsConstructor
    class entry {
        public Long vendorId;
        public Long cardPackId;
    }

    private Connection _connection;

    public PostgresIVendorRepository(Connection connection,Boolean initVals) {
        this._connection = connection;
        if(initVals)
            persistEntity(new NormalVendor());
    }


    @Override
    public synchronized Long persistEntity(IVendor entity) {

        Long id;
        PreparedStatement statement;
        try {
            //region cardPack
            statement = _connection.prepareStatement("""
                    INSERT 
                    INTO
                    vendors
                    (type)
                    VALUES 
                    (
                    ?
                    )
                    returning id
                    """);

            statement.setString(1, entity.getClass().getTypeName());

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            id = resultSet.getLong(1);
            entity.setId(id);
            //endregion

            //region cardsInPack
            for (ICardPack cardPack : entity.getAvailibleCardPacks()) {
                InsertVendorIdIntoCardPack(entity.getId(), cardPack.getId());
            }
            //endregion


        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }

        return id;
    }

    @Override
    public synchronized boolean updateEntity(IVendor entity) {
        PreparedStatement statement;
        try {
            //region cardPack
            statement = _connection.prepareStatement("""
                     Update 
                        vendors
                        SET
                        type=?
                        WHERE id=?
                        RETURNING id
                    """);
            statement.setString(1, entity.getClass().getTypeName());
            statement.setLong(2, entity.getId());

            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next())
                return false;
            //endregion

            //region cardsPacks
            PreparedStatement statementPack = _connection.prepareStatement("""
                    Select 
                    id,
                    vendorid
                    FROM cardpacks
                    WHERE vendorid=?
                    """);
            statementPack.setLong(1, entity.getId());
            ResultSet resultSetPack = statementPack.executeQuery();

            List<entry> packEntriesDB = new ArrayList<>();
            while (resultSetPack.next()) {
                packEntriesDB.add(new entry(resultSetPack.getLong(2), resultSetPack.getLong(1)));
            }

            List<entry> stackEntriesMemory = entity.getAvailibleCardPacks().stream().map(x -> new entry(entity.getId(), x.getId())).collect(Collectors.toList());

            List<entry> toDeletePack = packEntriesDB.stream().filter(x -> !stackEntriesMemory.contains(x)).collect(Collectors.toList());
            for (entry item : toDeletePack) {
                PreparedStatement statement3 = _connection.prepareStatement("""
                        UPDATE 
                        cardpacks
                        set vendorid=?
                        where id=?
                        """);
                statement3.setNull(1, Types.INTEGER);
                statement3.setLong(2, item.cardPackId);
                statement3.execute();
            }

            List<entry> toInsert = stackEntriesMemory.stream().filter(x -> !packEntriesDB.contains(x)).collect(Collectors.toList());
            for (entry item : toInsert) {
                InsertVendorIdIntoCardPack(item.vendorId, item.cardPackId);
            }
            //endregion
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public IVendor findEntity(Long id) {
        IVendor vendor = null;
        try {
            PreparedStatement statement = _connection.prepareStatement("""
                    SELECT 
                    id, 
                    type
                    FROM vendors 
                    where id=?
                    """);

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Class cardPackClass = Class.forName(resultSet.getString(2));
                vendor = (IVendor) ((Class<?>) cardPackClass).getDeclaredConstructor().newInstance();
                vendor.setId(resultSet.getLong(1));

                vendor.getAvailibleCardPacks().addAll(findCardPacksfromVendor(vendor.getId()));
            }
        } catch (SQLException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException throwables) {
            throwables.printStackTrace();
        }
        return vendor;
    }

    @Override
    public synchronized boolean deleteEntity(Long id) {
        PreparedStatement statement;
        try {
            //region cardpacks
            PreparedStatement statementPack = _connection.prepareStatement("""
                    Select 
                    id,
                    vendorid
                    FROM cardpacks
                    WHERE vendorid=?
                    """);
            statementPack.setLong(1, id);
            ResultSet resultSetPack = statementPack.executeQuery();

            List<entry> packEntriesDB = new ArrayList<>();
            while (resultSetPack.next()) {
                packEntriesDB.add(new entry(resultSetPack.getLong(1), resultSetPack.getLong(2)));
            }
            for (entry item : packEntriesDB) {
                PreparedStatement statement3 = _connection.prepareStatement("""
                        UPDATE 
                        cardpacks
                        set vendorid=?
                        where id=?
                        """);
                statement3.setNull(1, Types.INTEGER);
                statement3.setLong(2, item.cardPackId);
                statement3.execute();
            }
            //endregion
            //region vendor
            statement = _connection.prepareStatement("""
                    DELETE
                    FROM vendors
                    WHERE id=?
                    returning id
                    """);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next())
                return false;
            //endregion

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<IVendor> getAllEntities() {
        List<IVendor> vendors = new ArrayList<>();
        try {
            PreparedStatement statement = _connection.prepareStatement("""
                    SELECT 
                    id, 
                    type
                    FROM vendors 
                    """);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Class cardPackClass = Class.forName(resultSet.getString(2));
                IVendor vendor = (IVendor) ((Class<?>) cardPackClass).getDeclaredConstructor().newInstance();
                vendor.setId(resultSet.getLong(1));

                vendor.getAvailibleCardPacks().addAll(findCardPacksfromVendor(vendor.getId()));
                vendors.add(vendor);
            }
        } catch (SQLException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException throwables) {
            throwables.printStackTrace();
        }
        return vendors;
    }

    private void InsertVendorIdIntoCardPack(Long vendorId, Long cardPackId) throws SQLException {
        PreparedStatement statement = _connection.prepareStatement("""
                UPDATE 
                cardpacks
                set vendorid=?
                where id=?
                """);

        statement.setLong(1, vendorId);
        statement.setLong(2, cardPackId);
        statement.execute();
    }

    public List<ICardPack> findCardPacksfromVendor(Long vendorid) {
        List<ICardPack> cardPacks = new ArrayList<>();
        try {
            PreparedStatement statement = _connection.prepareStatement("""
                    SELECT 
                    id, 
                    type,
                    cardamount,
                    costs,
                    packtype
                    FROM cardpacks 
                    where vendorid=?
                    """);

            statement.setLong(1, vendorid);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Class cardPackClass = Class.forName(resultSet.getString(2));
                ICardPack cardPack = (ICardPack) ((Class<?>) cardPackClass).getDeclaredConstructor().newInstance();
                cardPack.setId(resultSet.getLong(1));
                cardPack.setCardAmount(resultSet.getInt(3));
                cardPack.setCosts(resultSet.getInt(4));
                cardPack.setPackType(PackType.valueOf(resultSet.getString(5)));

                cardPack.getCards().addAll(findCardsInPack(cardPack.getId()));
                cardPacks.add(cardPack);
            }
        } catch (SQLException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException throwables) {
            throwables.printStackTrace();
        }
        return cardPacks;
    }

    private List<ACard> findCardsInPack(Long cardPackId) {
        List<ACard> cards = new ArrayList<>();
        try {
            PreparedStatement statement = _connection.prepareStatement("""
                    SELECT 
                    c.type,
                    c.id, 
                    c.name,
                    c.damage,
                    c.effectid,
                    c.raceid
                    FROM cards c
                    JOIN cardsInPack cp ON c.id=cp.cardid
                    where cp.cardpackid=?
                    """);

            statement.setLong(1, cardPackId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Class cardClass = Class.forName(resultSet.getString(1));
                ACard card = (ACard) ((Class<?>) cardClass).getDeclaredConstructor().newInstance();
                card.setId(resultSet.getString(2));
                card.setName(resultSet.getString(3));
                card.setDamage(resultSet.getDouble(4));
                card.setEffect(findEffect(resultSet.getLong(5)));

                if (card.getClass() == MonsterCard.class) {
                    ((MonsterCard) card).setRace(findRace(resultSet.getLong(6)));

                }
                cards.add(card);
            }
        } catch (SQLException | ClassNotFoundException | NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException throwables) {
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
        } catch (SQLException | ClassNotFoundException | NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException throwables) {
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
