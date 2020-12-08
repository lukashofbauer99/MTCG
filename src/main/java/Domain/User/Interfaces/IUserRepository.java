package Domain.User.Interfaces;

import Domain.IRepository;
import Model.User.Credentials;
import Model.User.User;

public interface IUserRepository extends IRepository<User, Long> {

    String loginUser(Credentials cred);

}
