package Service.RESTServer.Service;

import Domain.User.InMemoryUserRepository;
import Domain.User.Interfaces.IUserRepository;
import Service.RESTServer.Service.Methods.DELETE.DELETE_messages_Id;
import Service.RESTServer.Service.Methods.Error.NotFound;
import Service.RESTServer.Service.Methods.GET.GET_messages;
import Service.RESTServer.Service.Methods.GET.GET_messages_Id;
import Service.RESTServer.Service.Methods.IHTTPMethod;
import Service.RESTServer.Service.Methods.POST.POST_messages;
import Service.RESTServer.Service.Methods.POST.POST_sessions;
import Service.RESTServer.Service.Methods.POST.POST_users;
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

        //register Methods
        registeredMethods.add(new GET_messages());
        registeredMethods.add(new DELETE_messages_Id());
        registeredMethods.add(new PUT_messages_Id());
        registeredMethods.add(new GET_messages_Id());
        registeredMethods.add(new POST_messages());
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