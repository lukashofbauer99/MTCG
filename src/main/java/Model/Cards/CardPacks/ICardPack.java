package Model.Cards.CardPacks;

import Model.Cards.ACard;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.Pack;

import java.util.List;

public interface ICardPack {

    Long getId();

    void setId(Long id);

    List<ACard> getCards();

    int getCosts();

    void setCosts(int costs);

    int getCardAmount();

    void setCardAmount(int cardAmount);

    PackType getPackType();

    void setPackType(PackType packType);

}
