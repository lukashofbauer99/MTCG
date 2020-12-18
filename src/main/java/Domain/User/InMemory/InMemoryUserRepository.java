package Domain.User.InMemory;

import Domain.User.Interfaces.IUserRepository;
import Model.Cards.ACard;
import Model.User.Credentials;
import Model.User.Deck;
import Model.User.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.util.stream.Collectors.toList;

public class InMemoryUserRepository implements IUserRepository {

    ConcurrentMap<Long,User> users= new ConcurrentHashMap<>();
    Map<String,User> usersInSession = new HashMap<>();
    Long currentID= 1L;


    @Override
    public synchronized Long persistEntity(User entity) {
        if(!users.values().stream().map(x->x.getCredentials().getUsername()).collect(toList()).contains(entity.getCredentials().getUsername())) {
            while (users.containsKey(currentID)) {
                currentID++;
            }
            users.put(currentID, entity);
            entity.setId(currentID);
            return currentID;
        }
        else return null;
    }

    @Override
    public synchronized boolean updateEntity(User entity) {
        if(entity.getId()!=null) {
            if (users.containsKey(entity.getId())) {
                users.put(entity.getId(), entity);
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
        return new ArrayList<>(users.values());
    }

    @Override
    public String loginUser(Credentials cred) {
        if(users.values().stream().map(User::getCredentials).collect(toList()).contains(cred)) {
            String sessionToken="Basic " + cred.getUsername() + "-mtcgToken";
            if (!usersInSession.containsKey(sessionToken))
            {
                User us=users.values().stream().filter(x->x.getCredentials().equals(cred)).findAny().orElse(null);
                usersInSession.put(sessionToken,us);
            }
            return sessionToken;
        }
        return null;
    }

    @Override
    public Boolean UserLoggedIn(String token) {
        return usersInSession.containsKey(token);
    }

    @Override
    public List<ACard> getCardsOfUserWithToken(String token) {
        User user = getUserWithToken(token);
        if(user!=null)
        {
            List<ACard> cardsOfUser= user.getStack().getCards();
            cardsOfUser.addAll(user.getDeck().getCards());
            return cardsOfUser;
        }
        return null;
    }

    @Override
    public Deck getDeckOfUserWithToken(String token) {
        User user = getUserWithToken(token);
        if(user!=null)
        {
            return user.getDeck();
        }
        return null;
    }

    @Override
    public User getUserWithToken(String token) {
        if(usersInSession.containsKey(token))
        {
            return usersInSession.get(token);
        }
        return null;
    }

    @Override
    public User getUserWithUsername(String username) {
        return users.values().stream().filter(x->x.getCredentials().getUsername().equals(username)).findFirst().orElse(null);
    }
}
