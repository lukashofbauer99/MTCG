package Model.Cards.Vendor;

import Model.Cards.ACard;
import Model.Cards.CardPacks.ICardPack;
import Model.Cards.CardPacks.PackType;
import Model.User.User;

import java.util.List;

public interface IVendor {

    List<ICardPack> getAvailibleCardPacks();
    ICardPack buyCards(User user, PackType packType);
    ICardPack buyCards(User user);

}
