package Service.RESTServer.Service;

import Domain.Cards.InMemory.*;
import Domain.Cards.Interfaces.*;
import Domain.User.InMemory.InMemoryITradeRepository;
import Domain.User.InMemory.InMemoryUserRepository;
import Domain.User.Interfaces.ITradeRepository;
import Domain.User.Interfaces.IUserRepository;
import Service.RESTServer.Service.Methods.DELETE.DELETE_messages_Id;
import Service.RESTServer.Service.Methods.DELETE.DELETE_tradings_id;
import Service.RESTServer.Service.Methods.Error.NotFound;
import Service.RESTServer.Service.Methods.GET.*;
import Service.RESTServer.Service.Methods.IHTTPMethod;
import Service.RESTServer.Service.Methods.POST.*;
import Service.RESTServer.Service.Methods.PUT.PUT_deck;
import Service.RESTServer.Service.Methods.PUT.PUT_messages_Id;
import Service.RESTServer.Service.Methods.PUT.PUT_users_name;
import Service.RESTServer.Service.Socket.IMySocket;
import Service.RESTServer.Service.Socket.MySocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class MainServer implements Runnable {

    private static ServerSocket _listener = null;


    public static void main(String[] args) {
        System.out.println("start server");

        List<IHTTPMethod> registeredMethods = new ArrayList<>();

        //repositories
        IUserRepository userRepository = new InMemoryUserRepository();
        ICardPackRepository cardPackRepository = new InMemoryCardPackRepository();
        IACardRepository cardRepository = new InMemoryACardRepository();
        IEffectRepository effectRepository = new InMemoryIEffectRepository();
        IRaceRepository raceRepository = new InMemoryIRaceRepository();
        IVendorRepository vendorRepository = new InMemoryIVendorRepository();
        ITradeRepository tradeRepository = new InMemoryITradeRepository();

        //register Methods
        registeredMethods.add(new GET_messages());
        registeredMethods.add(new DELETE_messages_Id());
        registeredMethods.add(new PUT_messages_Id());
        registeredMethods.add(new GET_messages_Id());
        registeredMethods.add(new POST_messages());


        registeredMethods.add(new DELETE_tradings_id(userRepository,tradeRepository));
        registeredMethods.add(new POST_tradings_id(userRepository,cardRepository,tradeRepository));
        registeredMethods.add(new POST_tradings_id(userRepository,cardRepository,tradeRepository));
        registeredMethods.add(new POST_tradings(userRepository,cardRepository,tradeRepository));
        registeredMethods.add(new GET_tradings(userRepository,tradeRepository));
        registeredMethods.add(new GET_stats(userRepository));
        registeredMethods.add(new PUT_users_name(userRepository));
        registeredMethods.add(new GET_user_name(userRepository));
        registeredMethods.add(new GET_deck_plain(userRepository));
        registeredMethods.add(new PUT_deck(userRepository, cardRepository));
        registeredMethods.add(new GET_deck(userRepository));
        registeredMethods.add(new GET_cards(userRepository));
        registeredMethods.add(new POST_NormalPackages(cardPackRepository, cardRepository, effectRepository, raceRepository, vendorRepository));
        registeredMethods.add(new POST_cards_Effects_RaceFromName(cardRepository, effectRepository, raceRepository));
        registeredMethods.add(new POST_users(userRepository));
        registeredMethods.add(new POST_sessions(userRepository));
        registeredMethods.add(new POST_transaction_packages(userRepository, vendorRepository));
        registeredMethods.add(new NotFound());


        //Create Socket that listens on port 8000
        try {
            _listener = new ServerSocket(10001, 5);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Runtime.getRuntime().addShutdownHook(new Thread(new MainServer()));

        while (true) {
            try {
                IMySocket s = new MySocket(_listener.accept());
                //Start Thread for Connection
                new Thread(new WorkerThread(s, registeredMethods)).start();


            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    public void run() {
        try {
            _listener.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        _listener = null;
        System.out.println("close server");
    }
}