package Domain;

import Domain.Battle.Interfaces.IBattleRepository;
import Model.Battle.Battle;
import Model.User.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static java.lang.StrictMath.abs;

//@Component
@Getter
public class PlayerHub {

    IBattleRepository battleRepository;

    public PlayerHub(IBattleRepository battleRepository) {
        this.battleRepository = battleRepository;
    }

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
            currentBattle.start();
            currentBattle.setId(battleRepository.persistEntity(currentBattle));
        }
        return currentBattle;

    }
}
