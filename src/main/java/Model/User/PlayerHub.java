package Model.User;

import Model.Cards.CardPacks.ACardPack;
import jdk.jshell.spi.ExecutionControl;

import java.util.ArrayList;
import java.util.List;

//@Component
public class PlayerHub {

    List<User> BattleSearchingUsers = new ArrayList<>();

    //ENUM CARDPACKS als argument
    public ACardPack buyCards(User user){
        return null;
    }

    public void registerForMatchmaking(User user){
    }

    public void matchPlayers() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("TODO");
    }
}
