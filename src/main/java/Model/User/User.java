package Model.User;

import Domain.PlayerHub;
import Model.Battle.Battle;
import Model.Cards.ACard;
import Model.Cards.CardPacks.ICardPack;
import Model.Cards.CardPacks.PackType;
import Model.Cards.Vendor.IVendor;
import Model.User.Statistics.Stats;
import Model.User.Trade.ITrade;
import Model.User.Trade.ITradeCardRequirements;
import Model.User.Trade.Trade1To1;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.stream.Collectors.toList;


@Getter
public class User {
    @Setter
    Long id;

    Credentials credentials = new Credentials();

    @Setter
    int coins = 20;
    @Setter
    int mmr = 100;

    public User(Long id, Credentials credentials, int coins, int mmr, EditableUserData editableUserData) {
        this.id = id;
        this.credentials = credentials;
        this.coins = coins;
        this.mmr = mmr;
        this.editableUserData = editableUserData;
    }

    @Setter
    EditableUserData editableUserData = new EditableUserData();


    @JsonIgnore
    Stack stack = new Stack();
    @JsonIgnore
    Deck deck = new Deck();
    @JsonIgnore
    List<Battle> battleHistory = new ArrayList<>();


    public User() {
    }

    public User(Credentials credentials) {
        this.credentials = credentials;
    }


    public boolean buyCardPackage(PackType packType, IVendor vendor) {
        ICardPack pack = vendor.buyCards(this, packType);

        if (pack != null) {
            pack.getCards().forEach(x -> stack.getCards().add(x));
            return true;
        }
        return false;
    }

    public boolean buyCardPackage(IVendor vendor) {
        ICardPack pack = vendor.buyCards(this);

        if (pack != null) {
            pack.getCards().forEach(x -> stack.getCards().add(x));
            return true;
        }
        return false;
    }


    public boolean defineDeck(List<ACard> cards) {
        if (cards.size() == 4) {
            AtomicBoolean cardsOwned = new AtomicBoolean(true);
            cards.forEach(x -> {
                if (!stack.cards.stream().map(y->y.getId()).collect(toList()).contains(x.getId()))
                    cardsOwned.set(false);
            });
            if (cardsOwned.get()) {
                deck.cards = cards;
                stack.cards.removeIf(x->cards.stream().map(y->y.getId()).collect(toList()).contains(x.getId()));
                return true;
            }
        }
        return false;
    }

    //@Transaction

    public ITrade createTrade(String id, ACard cardToTrade, ITradeCardRequirements requirements) {
        if (stack.getCards().stream().map(x->x.getId()).collect(toList()).contains(cardToTrade.getId())) {
            return new Trade1To1(id,this, cardToTrade, requirements);
        } else return null;
    }

    public void tradeCard(User user, ACard cardGiven, ACard cardRecieved) {
        this.getStack().getCards().remove(cardGiven);
        this.getStack().getCards().add(cardRecieved);

        user.getStack().getCards().remove(cardRecieved);
        user.getStack().getCards().add(cardGiven);
    }

    public boolean accectTradeOffer(ITrade trade, ACard card) {
        if (stack.getCards().stream().map(x->x.getId()).collect(toList()).contains(card.getId())) {
            return trade.trade(this, card);
        }
        else
            return false;
    }

    public Battle searchBattle(PlayerHub playerHub) {
        return playerHub.matchPlayers(this);
    }

    public Stats showStats() {
        double battleCount = battleHistory.size();
        double battlesWon = (int) battleHistory.stream().filter(x -> x.getWinner().equals(this)).count();
        double winrate = battlesWon / battleCount;
        return new Stats(this.credentials.username, mmr, (int) battleCount, winrate);
    }
}
