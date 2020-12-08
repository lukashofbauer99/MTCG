package Model.User;

import Model.Battle.Battle;
import Model.Cards.ACard;
import Model.Cards.CardPacks.ICardPack;
import Model.Cards.Vendor.IVendor;
import Model.Cards.CardPacks.PackType;
import Model.User.Trade.ITrade;
import Model.User.Trade.ITradeCardRequirements;
import Model.User.Trade.Trade1To1;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


public class User {

    @Getter
    Credentials credentials = new Credentials();

    @Setter
    @Getter
    Long userID;

    @Setter
    @Getter
    int coins=20;
    @Setter
    @Getter
    int mmr=0;

    @Getter
    Stack stack = new Stack();
    @Getter
    Deck deck = new Deck();
    @Getter
    List<Battle> battleHistory= new ArrayList<>();



    //@Autowired
    PlayerHub playerHub;


    public User() {
    }

    public User(Credentials credentials) {
        this.credentials=credentials;
    }

    public User(PlayerHub playerHub) {
        this.playerHub = playerHub;
    }

    public boolean buyCardPackage(PackType packType, IVendor vendor) {
        ICardPack pack =vendor.buyCards(this,packType);

        if(pack!=null) {
        pack.getCards().forEach(x -> stack.getCards().add(x));
        return true;
        }
        return false;
    }

    public boolean buyCardPackage(IVendor vendor) {
        ICardPack pack =vendor.buyCards(this);

        if(pack!=null) {
            pack.getCards().forEach(x -> stack.getCards().add(x));
            return true;
        }
        return false;
    }


    private volatile Integer prevIndex= null;
    private volatile Integer counter= 0;
    public void defineDeck(int card1Index,int card2Index,int card3Index,int card4Index){
        List<Integer> indexList= new ArrayList<>();
        indexList.add(card1Index);
        indexList.add(card2Index);
        indexList.add(card3Index);
        indexList.add(card4Index);

        if(indexList.stream().allMatch(x->(x>=0&&x<getStack().getCards().size())))
        {
            indexList.forEach(x-> {
                if(prevIndex!=null)
                    if (prevIndex<x)
                       counter++;
                x=x-counter;
                getDeck().getCards().add(getStack().getCards().get(x));
                getStack().getCards().remove(getStack().getCards().get(x));
                prevIndex=x;
            });
        }
    }

    //@Transaction

    public ITrade createTrade(User user, ACard cardToTrade, ITradeCardRequirements requirements) {
        if(stack.getCards().contains(cardToTrade)) {
            ITrade trade = new Trade1To1(user,cardToTrade,requirements);
            return trade;
        }
        else return null;
    }

    public void tradeCard(User user, ACard cardGiven, ACard cardRecieved) {
        this.getStack().getCards().remove(cardGiven);
        this.getStack().getCards().add(cardRecieved);

        user.getStack().getCards().remove(cardRecieved);
        user.getStack().getCards().add(cardGiven);
    }

    public void searchBattle(){
        playerHub.registerForMatchmaking(this);
    }
}
