package Service.RESTServer.Service;

import Domain.Cards.InMemory.InMemoryACardRepository;
import Domain.Cards.InMemory.InMemoryCardPackRepository;
import Domain.Cards.InMemory.InMemoryIEffectRepository;
import Domain.Cards.InMemory.InMemoryIRaceRepository;
import Domain.Cards.Interfaces.IACardRepository;
import Domain.Cards.Interfaces.ICardPackRepository;
import Domain.Cards.Interfaces.IEffectRepository;
import Domain.Cards.Interfaces.IRaceRepository;
import Domain.User.InMemory.InMemoryUserRepository;
import Domain.User.Interfaces.IUserRepository;
import Service.RESTServer.Service.Methods.DELETE.DELETE_messages_Id;
import Service.RESTServer.Service.Methods.Error.NotFound;
import Service.RESTServer.Service.Methods.GET.GET_messages;
import Service.RESTServer.Service.Methods.GET.GET_messages_Id;
import Service.RESTServer.Service.Methods.IHTTPMethod;
import Service.RESTServer.Service.Methods.POST.*;
import Service.RESTServer.Service.Methods.PUT.PUT_messages_Id;
import Service.RESTServer.Service.Socket.IMySocket;
import Service.RESTServer.Service.Socket.MySocket;

import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class MainServer implements Runnable {

    private static ServerSocket _listener = null;


    public static void main(String[] args) {
        System.out.println("start server");

        List<IHTTPMethod> registeredMethods = new ArrayList<>();

        //repositories
        IUserRepository userRepo=new InMemoryUserRepository();
        ICardPackRepository cardPackRepo=new InMemoryCardPackRepository();
        IACardRepository cardRepo=new InMemoryACardRepository();
        IEffectRepository effectRepository= new InMemoryIEffectRepository();
        IRaceRepository raceRepository= new InMemoryIRaceRepository();

        //register Methods
        registeredMethods.add(new GET_messages());
        registeredMethods.add(new DELETE_messages_Id());
        registeredMethods.add(new PUT_messages_Id());
        registeredMethods.add(new GET_messages_Id());
        registeredMethods.add(new POST_messages());

        registeredMethods.add(new POST_NormalPackages(cardPackRepo,cardRepo,effectRepository,raceRepository));
        registeredMethods.add(new POST_cards_Effects_RaceFromName(cardRepo,effectRepository,raceRepository));
        registeredMethods.add(new POST_users(userRepo));
        registeredMethods.add(new POST_sessions(userRepo));
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
                new Thread(new WorkerThread(s,registeredMethods)).start();


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