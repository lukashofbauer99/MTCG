package Domain.Cards.DataBase.Postgres;

import Domain.Cards.Interfaces.ICardPackRepository;
import Model.Cards.ACard;
import Model.Cards.CardPacks.ICardPack;
import Model.Cards.CardPacks.PackType;
import Model.Cards.Effects_Races.Effects.IEffect;
import Model.Cards.Effects_Races.Races.IRace;
import Model.Cards.MonsterCard;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PostgresCardPackRepository implements ICardPackRepository {

    @AllArgsConstructor
    class entry {
        public Long cardPackId;
        public String cardId;
    }

    private Connection _connection = null;

    public PostgresCardPackRepository(Connection connection) {
        this._connection = connection;
    }

    public PostgresCardPackRepository() {
    }

    @Override
    public synchronized Long persistEntity(ICardPack entity) {

        Long id;
        PreparedStatement statement;
        try {
            //region cardPack
            statement = _connection.prepareStatement("""
                    INSERT 
                    INTO
                    cardpacks
                    ( type, costs, cardAmount,packtype)
                    VALUES 
                    (
                    ?,?,?,?
                    )
                    returning id
                    """);

            statement.setString(1, entity.getClass().getTypeName());
            statement.setInt(2, entity.getCosts());
            statement.setInt(3, entity.getCardAmount());
            statement.setString(4, entity.getPackType().toString());

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            id = resultSet.getLong(1);
            entity.setId(id);
            //endregion

            //region cardsInPack
            for (ACard card : entity.getCards()) {
                InsertIntoCardsInPack(entity.getId(), card.getId());
            }
            //endregion


        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }

        return id;
    }

    @Override
    public synchronized boolean updateEntity(ICardPack entity) {
        PreparedStatement statement;
        try {
            //region cardPack
            statement = _connection.prepareStatement("""
                     Update 
                        cardpacks
                        SET
                        costs=?, cardAmount=?,packtype=?
                        WHERE id=?
                        RETURNING id
                    """);

            statement.setInt(1, entity.getCosts());
            statement.setInt(2, entity.getCardAmount());
            statement.setString(3, entity.getPackType().toString());
            statement.setLong(4, entity.getId());

            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next())
                return false;
            //endregion

            //region cardsInPack
            PreparedStatement statementPack = _connection.prepareStatement("""
                    Select 
                    cardpackid,
                    cardid
                    FROM cardsInPack
                    WHERE cardpackid=?
                    """);
            statementPack.setLong(1, entity.getId());
            ResultSet resultSetPack = statementPack.executeQuery();

            List<entry> packEntriesDB = new ArrayList<>();
            while (resultSetPack.next()) {
                packEntriesDB.add(new entry(resultSetPack.getLong(1), resultSetPack.getString(2)));
            }

            List<entry> stackEntriesMemory = entity.getCards().stream().map(x -> new entry(entity.getId(), x.getId())).collect(Collectors.toList());

            List<entry> toDeleteStack = packEntriesDB.stream().filter(x -> !stackEntriesMemory.contains(x)).collect(Collectors.toList());
            for (entry item : toDeleteStack) {
                PreparedStatement statement3 = _connection.prepareStatement("""
                        DELETE
                        FROM CardsInPack
                        WHERE cardpackid=? AND cardid=?
                        """);
                statement3.setLong(1, item.cardPackId);
                statement3.setString(2, item.cardId);
                statement3.execute();
            }

            List<entry> toInsert = stackEntriesMemory.stream().filter(x -> !packEntriesDB.contains(x)).collect(Collectors.toList());
            for (entry item : toInsert) {
                InsertIntoCardsInPack(item.cardPackId, item.cardId);
            }
            //endregion
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public ICardPack findEntity(Long id) {
        ICardPack cardPack = null;
        try {
            PreparedStatement statement = _connection.prepareStatement("""
                    SELECT 
                    id, 
                    type,
                    cardamount,
                    costs,
                    packtype
                    FROM cardpacks 
                    where id=?
                    """);

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Class cardPackClass = Class.forName(resultSet.getString(2));
                cardPack = (ICardPack) ((Class<?>) cardPackClass).getDeclaredConstructor().newInstance();
                cardPack.setId(resultSet.getLong(1));
                cardPack.setCardAmount(resultSet.getInt(3));
                cardPack.setCosts(resultSet.getInt(4));
                cardPack.setPackType(PackType.valueOf(resultSet.getString(5)));

                cardPack.getCards().addAll(findCardsInPack(cardPack.getId()));
            }
        } catch (SQLException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException throwables) {
            throwables.printStackTrace();
        }
        return cardPack;
    }

    @Override
    public synchronized boolean deleteEntity(Long id) {
        PreparedStatement statement;
        try {
            statement = _connection.prepareStatement("""
                    DELETE
                    FROM cardpacks
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
    public List<ICardPack> getAllEntities() {
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
                    """);

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

    private void InsertIntoCardsInPack(Long cardPackId, String cardId) throws SQLException {
        PreparedStatement statement = _connection.prepareStatement("""
                INSERT 
                INTO
                cardsInPack
                ( cardpackid, cardid)
                VALUES 
                (
                ?,?
                )
                """);

        statement.setLong(1, cardPackId);
        statement.setString(2, cardId);
        statement.execute();
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
