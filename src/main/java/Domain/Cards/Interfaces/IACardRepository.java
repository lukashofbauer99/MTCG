package Domain.Cards.Interfaces;

import Domain.IRepository;
import Model.Cards.ACard;

public interface IACardRepository extends IRepository<ACard, String> {

    String persistEntityGenNoId(ACard card);

}
