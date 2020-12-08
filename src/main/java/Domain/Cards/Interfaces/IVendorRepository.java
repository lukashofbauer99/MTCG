package Domain.Cards.Interfaces;

import Domain.IRepository;
import Model.Cards.ACard;
import Model.Cards.Vendor.IVendor;

import java.util.List;

public interface IVendorRepository extends IRepository<IVendor, Long> {

    List<ACard> findCardsOfUser(String Username);
}
