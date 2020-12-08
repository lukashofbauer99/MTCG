package IntegrationTests;

import Domain.User.InMemoryUserRepository;
import Domain.User.Interfaces.IUserRepository;
import Model.User.Credentials;
import Model.User.User;
import Service.RESTServer.Service.Methods.DELETE.DELETE_messages_Id;
import Service.RESTServer.Service.Methods.Error.NotFound;
import Service.RESTServer.Service.Methods.GET.GET_messages;
import Service.RESTServer.Service.Methods.GET.GET_messages_Id;
import Service.RESTServer.Service.Methods.IHTTPMethod;
import Service.RESTServer.Service.Methods.POST.POST_messages;
import Service.RESTServer.Service.Methods.POST.POST_sessions;
import Service.RESTServer.Service.Methods.POST.POST_users;
import Service.RESTServer.Service.Methods.PUT.PUT_messages_Id;
import Service.RESTServer.Service.Socket.MySocket;
import Service.RESTServer.Service.WorkerThread;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ServerSocket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

//Start whole test class otherwise the Tests will fail
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestHTTPMethods_MTCG {

    String command = "";
    String command2= "";
    ObjectMapper mapper = new ObjectMapper();
    static User testUser= new User(new Credentials("name","pw"));

    private static ServerSocket _listener = null;

    static MySocket socket;
    static List<IHTTPMethod> methods= new ArrayList<>();
    static Thread workerThread;
    static boolean ready= false;

    static IUserRepository userRepo=new InMemoryUserRepository();


    @BeforeAll
    static void setUp() {
        methods.add(new GET_messages());
        methods.add(new POST_messages());
        methods.add(new GET_messages_Id());
        methods.add(new DELETE_messages_Id());
        methods.add(new PUT_messages_Id());
        methods.add(new POST_users(userRepo));
        methods.add(new POST_sessions(userRepo));
        methods.add(new NotFound());


        workerThread = new Thread(()-> {
            try {
                _listener = new ServerSocket(10001, 5);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            ready=true;
            while (true) {
                try {
                    socket = new MySocket(_listener.accept());
                    new WorkerThread(socket, methods).run();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        workerThread.start();


    }

    @AfterAll
    static void cleanUp() {

        workerThread.stop();

    }

    @Test
    @Order(1)
    @DisplayName("Test Create User")
    void testCreateUser() throws IOException { //TODO: LOOK Content-Type gets not set right

        command = "curl -X POST -d '{\"Username\":\"kienboec\", \"Password\":\"daniel\"}' -H \"Content-Type: application/json\" http://localhost:10001/users";
        while (!ready);

        Process process = Runtime.getRuntime().exec(command);

        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (process.getInputStream(), Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String response = textBuilder.toString();

        assertEquals(0l,response);



    }


    @Test
    @Order(2)
    @DisplayName("Test Login User")
    void testLoginUser() throws IOException{

        command = "curl -X POST http://localhost:10001/sessions --header \"Content-Type: application/json\" -d \"{\"Username\":\"kienboec\", \"Password\":\"daniel\"}\"";


        while (!ready);

        Process process = Runtime.getRuntime().exec(command);

        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (process.getInputStream(), Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String response = textBuilder.toString();
        assertEquals("Basic name-mtcgToken",response);
    }
}