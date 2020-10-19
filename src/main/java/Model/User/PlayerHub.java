package Model.User;

import Model.Cards.CardPacks.ICardPack;
import jdk.jshell.spi.ExecutionControl;

import java.util.ArrayList;
import java.util.List;

public class PlayerHub {

    List<User> BattleSearchingUsers = new ArrayList<>();

    public ICardPack buyCards(User user) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("TODO");
    }

    public void registerForMatchmaking(User user)throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("TODO");
    }

    public void matchPlayers() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("TODO");
    }
}
