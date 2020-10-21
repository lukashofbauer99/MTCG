package Model.User;

import Model.Battle.Battle;
import Model.Cards.CardPacks.ACardPack;
import Model.Cards.CardPacks.NormalCardPack;
import Model.Cards.CardPacks.PackType;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.javatuples.Tuple;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.lang.StrictMath.abs;

//@Component
public class PlayerHub {

    List<User> battleSearchingUsers = new ArrayList<>();

    public ACardPack buyCards(User user, PackType packType)
    {
        ACardPack cardPack=null;
        if(packType.equals(PackType.Normal)) {
            cardPack = new NormalCardPack();
        }
        if(user.getCoins()>=cardPack.getCosts()) {
            user.setCoins(user.getCoins() - cardPack.getCosts());
            return cardPack;
        }
        return null;
    }

    public void registerForMatchmaking(User user){
        battleSearchingUsers.add(user);
    }

    public void matchPlayers() {


        if (battleSearchingUsers.size()<2) {
            List<Pair<User,Integer>> mmrCalclist= new ArrayList<>();
            User matchedUser = battleSearchingUsers.get(0);
            battleSearchingUsers.forEach(x->{
                mmrCalclist.add(new Pair<>(x, abs(matchedUser.mmr-x.mmr)));
            });

            User matchedUser2 = mmrCalclist.stream().min(Comparator.comparing(Pair::getValue1)).orElse(null).getValue0();

            Battle battle = new Battle(matchedUser,matchedUser2);
            battle.Start();
        }
        else
            ;//not enough users;

    }
}
