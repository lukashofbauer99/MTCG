package Model.User;

import Model.Battle.Battle;
import Model.Cards.CardPacks.ICardPack;
import Model.Cards.CardPacks.NormalCardPack;
import Model.Cards.CardPacks.PackType;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.lang.StrictMath.abs;

//@Component
public class PlayerHub {

    List<ICardPack> availibleCardPacks = new ArrayList<>();

    public PlayerHub() {
        availibleCardPacks.add(new NormalCardPack());
    }

    List<User> battleSearchingUsers = new ArrayList<>();

    public ICardPack buyCards(User user, PackType packType)
    {
        ICardPack cardPack=availibleCardPacks.stream().filter(x->x.getPackType()==packType).findFirst().orElse(null);
        if(cardPack!=null) {
            if (user.getCoins() >= cardPack.getCosts()) {
                user.setCoins(user.getCoins() - cardPack.getCosts());
                return cardPack;
            }
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
            battleSearchingUsers.forEach(x-> mmrCalclist.add(new Pair<>(x, abs(matchedUser.mmr-x.mmr))));

            User matchedUser2 = mmrCalclist.stream().min(Comparator.comparing(Pair::getValue1)).orElse(null).getValue0();

            Battle battle = new Battle(matchedUser,matchedUser2);
            battle.Start();
        }
        else
            ;//not enough users;

    }
}
