package Model.Battle;

import Model.User.Deck;
import Model.User.User;
import jdk.jshell.spi.ExecutionControl;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Battle {

    User user1;
    User user2;

    Deck battleDeckUser1= new Deck();
    Deck battleDeckUser2= new Deck();

    List<Round> rounds = new ArrayList<>();

    public Battle(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
        battleDeckUser1.setCards( user1.getDeck().getCards());
        battleDeckUser2.setCards( user2.getDeck().getCards());
    }

    public User Start(){
        return null;
    }

}
