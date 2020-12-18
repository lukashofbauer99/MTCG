package Model.User;

import Model.Battle.Battle;
import Model.Cards.ACard;
import Model.Cards.CardPacks.ICardPack;
import Model.Cards.Vendor.IVendor;
import Model.Cards.CardPacks.PackType;
import Model.User.Trade.ITrade;
import Model.User.Trade.ITradeCardRequirements;
import Model.User.Trade.Trade1To1;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


@Getter
public class User {
    @Setter
    Long id;

    Credentials credentials = new Credentials();

    @Setter
    int coins = 20;
    @Setter
    int mmr = 0;

    @Setter
    EditableUserData editableUserData = new EditableUserData();


    Stack stack = new Stack();
    Deck deck = new Deck();
    List<Battle> battleHistory = new ArrayList<>();


    //@Autowired
    PlayerHub playerHub;


    public User() {
    }

    public User(Credentials credentials) {
        this.credentials = credentials;
    }

    public User(PlayerHub playerHub) {
        this.playerHub = playerHub;
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
       if (cards.size()==4)
       {
           AtomicBoolean cardsOwned= new AtomicBoolean(true);
           cards.forEach(x-> {
               if(!stack.cards.contains(x))
                   cardsOwned.set(false);
           });
           if(cardsOwned.get()) {
               deck.cards = cards;
               return true;
           }
       }
       return false;
    }

    //@Transaction

    public ITrade createTrade(User user, ACard cardToTrade, ITradeCardRequirements requirements) {
        if (stack.getCards().contains(cardToTrade)) {
            return new Trade1To1(user, cardToTrade, requirements);
        } else return null;
    }

    public void tradeCard(User user, ACard cardGiven, ACard cardRecieved) {
        this.getStack().getCards().remove(cardGiven);
        this.getStack().getCards().add(cardRecieved);

        user.getStack().getCards().remove(cardRecieved);
        user.getStack().getCards().add(cardGiven);
    }

    public void searchBattle() {
        playerHub.registerForMatchmaking(this);
    }
}
