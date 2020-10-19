package Model.User;

import Model.Battle.Battle;
import Model.Cards.ACard;
import jdk.jshell.spi.ExecutionControl;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class User {
    int coins=20;
    int mmr=0;

    Credentials credentials = new Credentials();
    Stack stack = new Stack();
    Deck deck = new Deck();
    List<Battle> battleHistory= new ArrayList<>();

    public void buyCardPackage() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("TODO");
    }

    public void defineDeck() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("TODO");
    }

    public void tradeCard(User user, ACard cardGiven, ACard cardRecieved) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("TODO");
    }

    public void searchBattle() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("TODO");
    }
}
