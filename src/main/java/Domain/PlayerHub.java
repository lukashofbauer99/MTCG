package Domain;

import Domain.Battle.Interfaces.IBattleRepository;
import Model.Battle.Battle;
import Model.User.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

//@Component
@Getter
public class PlayerHub {

    IBattleRepository battleRepository;

    public PlayerHub(IBattleRepository battleRepository) {
        this.battleRepository = battleRepository;
    }

    private List<Battle> currentBattles = Collections.synchronizedList(new ArrayList());
    private Battle finishedBattle = new Battle();

    AtomicInteger playerCounter = new AtomicInteger(2);

    public Battle matchPlayers(User user) {
        if (playerCounter.get() == 2) {
            currentBattles.add(new Battle());
            playerCounter.set(0);
        }
        Battle currentBattle = currentBattles.get(currentBattles.size() - 1);
        if (currentBattle.getUser1() == null) {
            playerCounter.incrementAndGet();
            currentBattle.setUser1(user);
            while (currentBattle.getId() == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            playerCounter.incrementAndGet();
            currentBattle.setUser2(user);
            currentBattle.start();
            currentBattle.setId(battleRepository.persistEntity(currentBattle));
        }
        return currentBattle;
    }
}
