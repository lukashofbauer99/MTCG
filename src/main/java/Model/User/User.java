package Model.User;

import Model.Battle.Battle;
import Model.Cards.ACard;
import Model.Cards.CardPacks.ACardPackFixedSizeAndCost;
import Model.Cards.CardPacks.ICardPack;
import Model.Cards.CardPacks.PackType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


public class User {

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


    Credentials credentials = new Credentials();

    public void buyCardPackage(PackType packType) {
        ICardPack pack =playerHub.buyCards(this,packType);
        if(pack!=null) {
        pack.getCards().forEach(x -> stack.getCards().add(x));
        }
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
