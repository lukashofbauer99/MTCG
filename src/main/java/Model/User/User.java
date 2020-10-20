package Model.User;

import Model.Battle.Battle;
import Model.Cards.ACard;
import jdk.jshell.spi.ExecutionControl;
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

    public User(PlayerHub playerHub) {
        this.playerHub = playerHub;
    }


    Credentials credentials = new Credentials();

    public void buyCardPackage() {

    }

    public void defineDeck(){
    }

    public void tradeCard(User user, ACard cardGiven, ACard cardRecieved) {
    }

    public void searchBattle(){
    }
}
