package Service.RESTServer.Service;

import Domain.Battle.DataBase.Postgres.PostgresBattleRepository;
import Domain.Battle.Interfaces.IBattleRepository;
import Domain.Cards.DataBase.Postgres.*;
import Domain.Cards.InMemory.*;
import Domain.Cards.Interfaces.*;
import Domain.PlayerHub;
import Domain.User.DataBase.Postgres.PostgresITradeRepository;
import Domain.User.DataBase.Postgres.PostgresUserRepository;
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainServer implements Runnable {

    private static ServerSocket _listener = null;

    static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://172.17.0.2:5432/mtcg","postgres", "postgres");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //TODO Test curlscript
    public static void main(String[] args) {
        System.out.println("start server");

        List<IHTTPMethod> registeredMethods = new ArrayList<>();


        //repositories
        IBattleRepository battleRepository = new PostgresBattleRepository(connection);
        IUserRepository userRepository = new PostgresUserRepository(connection);
        ICardPackRepository cardPackRepository = new PostgresCardPackRepository(connection);
        IACardRepository cardRepository = new PostgresACardRepository(connection);
        IEffectRepository effectRepository = new PostgresIEffectRepository(connection,true);
        IRaceRepository raceRepository = new PostgresIRaceRepository(connection,true);
        IVendorRepository vendorRepository = new PostgresIVendorRepository(connection,true);
        ITradeRepository tradeRepository = new PostgresITradeRepository(connection);

        PlayerHub playerHub= new PlayerHub(battleRepository);


        //register Methods
        registeredMethods.add(new GET_messages());
        registeredMethods.add(new DELETE_messages_Id());
        registeredMethods.add(new PUT_messages_Id());
        registeredMethods.add(new GET_messages_Id());
        registeredMethods.add(new POST_messages());



        registeredMethods.add(new POST_battles(userRepository,playerHub));
        registeredMethods.add(new DELETE_tradings_id(userRepository,tradeRepository));
        registeredMethods.add(new POST_tradings_id(userRepository,cardRepository,tradeRepository));
        registeredMethods.add(new POST_tradings_id(userRepository,cardRepository,tradeRepository));
        registeredMethods.add(new POST_tradings(userRepository,cardRepository,tradeRepository));
        registeredMethods.add(new GET_tradings(userRepository,tradeRepository));
        registeredMethods.add(new GET_score(userRepository));
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