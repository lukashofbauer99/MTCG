package Model.User;

import Model.Battle.Battle;
import lombok.Getter;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Future;

import static java.lang.StrictMath.abs;

//@Component
@Getter
public class PlayerHub {

    private List<User> battleSearchingUsers = new ArrayList<>();

    //TODO: match players return same Future<Battle>
    //Future Battle
    public void registerForMatchmaking(User user) {
        battleSearchingUsers.add(user);

    }

    public void activatePlayerhub()
    {

    }

    public void matchPlayers() {
        while(true)
        if (battleSearchingUsers.size() >= 2) {
            List<Pair<User, Integer>> mmrCalclist = new ArrayList<>();
            User matchedUser = battleSearchingUsers.get(0);
            battleSearchingUsers.forEach(x -> mmrCalclist.add(new Pair<>(x, abs(matchedUser.mmr - x.mmr))));

            User matchedUser2 = mmrCalclist.stream().min(Comparator.comparing(Pair::getValue1)).orElse(null).getValue0();

            Battle battle = new Battle(matchedUser, matchedUser2);
            battle.Start();
        } else
            ;//not enough users;

    }
}
