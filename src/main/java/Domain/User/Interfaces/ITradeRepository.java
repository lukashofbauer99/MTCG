package Domain.User.Interfaces;

import Domain.IRepository;
import Model.Cards.ACard;
import Model.User.Trade.ITrade;

public interface ITradeRepository extends IRepository<ITrade, String> {
    String persistEntityGenNoId(ITrade card);
}
