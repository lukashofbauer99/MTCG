package Model.Cards.Vendor;

import Model.Cards.CardPacks.ICardPack;
import Model.Cards.CardPacks.PackType;
import Model.User.User;

import java.util.List;

public interface IVendor {

    Long getId();

    void setId(Long id);

    List<ICardPack> getAvailibleCardPacks();

    ICardPack buyCards(User user, PackType packType);

    ICardPack buyCards(User user);

    void addICardPack(ICardPack cardPack);
}
