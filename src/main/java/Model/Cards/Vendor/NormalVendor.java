package Model.Cards.Vendor;

import Model.Cards.CardPacks.ICardPack;
import Model.Cards.CardPacks.PackType;
import Model.User.User;
import lombok.Getter;
import lombok.Setter;
import net.bytebuddy.implementation.auxiliary.AuxiliaryType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
public class NormalVendor implements IVendor {

    @Setter
    Long id;
    private List<ICardPack> availibleCardPacks = new ArrayList<>();


    @Override
    public ICardPack buyCards(User user, PackType packType)
    {
        ICardPack cardPack=availibleCardPacks.stream().filter(x->x.getPackType()==packType).findFirst().orElse(null);
        if(cardPack!=null) {
            if (user.getCoins() >= cardPack.getCosts()) {
                user.setCoins(user.getCoins() - cardPack.getCosts());
                availibleCardPacks.remove(cardPack);
                return cardPack;
            }
        }
        return null;
    }

    @Override
    public ICardPack buyCards(User user) {
        Random rand = new Random();
        if(availibleCardPacks.size()!=0) {
            ICardPack cardPack = availibleCardPacks.stream().findFirst().orElse(null);
                if (user.getCoins() >= cardPack.getCosts()) {
                    user.setCoins(user.getCoins() - cardPack.getCosts());
                    availibleCardPacks.remove(cardPack);
                    return cardPack;
                }
        }
        return null;
    }

    @Override
    public void addICardPack(ICardPack cardPack) {
        availibleCardPacks.add(cardPack);
    }

}
