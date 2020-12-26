package Domain;

import Model.Battle.Battle;
import Model.User.User;
import lombok.Getter;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;

import static java.lang.StrictMath.abs;

//@Component
@Getter
public class PlayerHub {

    private List<User> battleSearchingUsers = new ArrayList<>();
    private List<Battle> currentBattles = new ArrayList<>();
    private Battle currentBattle= new Battle();

    public Battle matchPlayers(User user) {
        if(currentBattle.getUser1()==null) {
            currentBattle.setUser1(user);
            while(currentBattle.getWinner()==null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            currentBattle.setUser2(user);
            currentBattle.Start();
        }
        return currentBattle;

    }
}
