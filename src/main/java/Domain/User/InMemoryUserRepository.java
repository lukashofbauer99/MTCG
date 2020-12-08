package Domain.User;

import Domain.User.Interfaces.IUserRepository;
import Model.User.Credentials;
import Model.User.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.util.stream.Collectors.toList;

public class InMemoryUserRepository implements IUserRepository {

    ConcurrentMap<Long,User> users= new ConcurrentHashMap<>();
    List<String> credInSession = new ArrayList<>();
    Long currentID=-1l;


    @Override
    public synchronized Long persistEntity(User entity) {
        if(!users.values().stream().map(x->x.getCredentials().getUsername()).collect(toList()).contains(entity.getCredentials().getUsername())) {
            currentID++;
            users.put(currentID, entity);
            entity.setUserID(currentID);
            return currentID;
        }
        else return null;
    }

    @Override
    public synchronized boolean updateEntity(User entity) {
        if(entity.getUserID()!=null) {
            if (users.containsKey(entity.getUserID())) {
                users.put(entity.getUserID(), users.get(entity.getUserID()));
                return true;
            }
        }
        return false;
    }

    @Override
    public User findEntity(Long id) {
        return users.get(id);
    }

    @Override
    public synchronized boolean deleteEntity(Long id) {
        if(users.containsKey(id)) {
            users.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public List<User> getAllEntities() {
        return new ArrayList<User>(users.values());
    }

    @Override
    public String loginUser(Credentials cred) {
        if(users.values().stream().map(x->x.getCredentials()).collect(toList()).contains(cred)) {
            String sessionToken="Basic " + cred.getUsername() + "-mtcgToken";
            if (!credInSession.contains(sessionToken))
            {
                credInSession.add(sessionToken);
            }
            return sessionToken;
        }
        return null;
    }
}
