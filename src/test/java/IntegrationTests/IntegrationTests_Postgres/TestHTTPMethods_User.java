package IntegrationTests.IntegrationTests_Postgres;

import Domain.User.DataBase.Postgres.PostgresUserRepository;
import Domain.User.InMemory.InMemoryUserRepository;
import Domain.User.Interfaces.IUserRepository;
import Model.User.Statistics.Stats;
import Model.User.User;
import Service.RESTServer.Service.Methods.GET.GET_score;
import Service.RESTServer.Service.Methods.GET.GET_stats;
import Service.RESTServer.Service.Methods.GET.GET_user_name;
import Service.RESTServer.Service.Methods.IHTTPMethod;
import Service.RESTServer.Service.Methods.POST.POST_sessions;
import Service.RESTServer.Service.Methods.POST.POST_users;
import Service.RESTServer.Service.Methods.PUT.PUT_users_name;
import Service.RESTServer.Service.Request.RequestContext;
import Service.RESTServer.Service.Socket.MySocket;
import Service.RESTServer.Service.WorkerThread;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ServerSocket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

//Start whole test class otherwise the Tests will fail
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestHTTPMethods_User {

    String command = "";

    private static ServerSocket _listener = null;

    static MySocket socket;
    static List<IHTTPMethod> methods = new ArrayList<>();
    static Thread workerThread;
    static volatile boolean ready = false;

    static ObjectMapper mapper = new ObjectMapper();

    static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://172.17.0.2:5432/mtcg_testing","postgres", "postgres");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    static IUserRepository userRepo = new PostgresUserRepository(connection);


    @BeforeAll
    static void setUp() {
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

        methods.add(new POST_users(userRepo));
        methods.add(new POST_sessions(userRepo));


        workerThread = new Thread(() -> {
            try {
                _listener = new ServerSocket(10007, 5);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            ready = true;
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

        try {
            connection
                    .createStatement()
                    .execute("truncate table battledecks,battles,cardpacks,cards,cardsinpack,decks,effects,normaltrades,races,rounds,stacks,users,vendors cascade ");

            //region Reset Sequences
            connection
                    .createStatement()
                    .execute("ALTER SEQUENCE battledecks_id_seq RESTART;");

            connection
                    .createStatement()
                    .execute("ALTER SEQUENCE battles_id_seq RESTART;");
            connection
                    .createStatement()
                    .execute("ALTER SEQUENCE cardpacks_id_seq RESTART;");
            connection
                    .createStatement()
                    .execute("ALTER SEQUENCE cardsinpack_id_seq RESTART;");
            connection
                    .createStatement()
                    .execute("ALTER SEQUENCE decks_id_seq RESTART;");
            connection
                    .createStatement()
                    .execute("ALTER SEQUENCE races_id_seq RESTART;");

            connection
                    .createStatement()
                    .execute("ALTER SEQUENCE effects_id_seq RESTART;");

            connection
                    .createStatement()
                    .execute("ALTER SEQUENCE rounds_id_seq RESTART;");
            connection
                    .createStatement()
                    .execute("ALTER SEQUENCE stacks_id_seq RESTART;");
            connection
                    .createStatement()
                    .execute("ALTER SEQUENCE users_id_seq RESTART;");
            connection
                    .createStatement()
                    .execute("ALTER SEQUENCE vendors_id_seq RESTART;");

            connection
                    .createStatement()
                    .execute("UPDATE battledecks SET id = DEFAULT;");

            connection
                    .createStatement()
                    .execute("UPDATE battles SET id = DEFAULT;");
            connection
                    .createStatement()
                    .execute("UPDATE cardpacks SET id = DEFAULT;");
            connection
                    .createStatement()
                    .execute("UPDATE cards SET id = DEFAULT;");
            connection
                    .createStatement()
                    .execute("UPDATE cardsinpack SET id = DEFAULT;");

            connection
                    .createStatement()
                    .execute("UPDATE decks SET id = DEFAULT;");

            connection
                    .createStatement()
                    .execute("UPDATE effects SET id = DEFAULT;");

            connection
                    .createStatement()
                    .execute("UPDATE normaltrades SET id = DEFAULT;");

            connection
                    .createStatement()
                    .execute("UPDATE races SET id = DEFAULT;");

            connection
                    .createStatement()
                    .execute("UPDATE rounds SET id = DEFAULT;");

            connection
                    .createStatement()
                    .execute("UPDATE stacks SET id = DEFAULT;");

            connection
                    .createStatement()
                    .execute("UPDATE users SET id = DEFAULT;");

            connection
                    .createStatement()
                    .execute("UPDATE vendors SET id = DEFAULT;");
            //endregion

            connection.close();
            connection= null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        workerThread.stop();

    }

    @Test
    @Order(1)
    @DisplayName("Test Create User")
    void testCreateUser() throws IOException {

        //No white spaces in Json Object allowed, if the request is sent from java, because the Content-Type get evaluated automatically
        command = "curl -X POST http://localhost:10007/users -H \"Content-Type: application/json\" -d {\"Username\":\"kienboec\",\"Password\":\"daniel\"}";
        while (!ready) {
            Thread.onSpinWait();
        }

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

        assertEquals("1", response);
    }

    @Test
    @Order(2)
    @DisplayName("Test Create User 2nd")
    void testCreateUser2nd() throws IOException {

        //No white spaces in Json Object allowed, if the request is sent from java, because the Content-Type get evaluated automatically
        command = "curl -X POST http://localhost:10007/users -H \"Content-Type: application/json\" -d {\"Username\":\"altenhof\",\"Password\":\"markus\"}";
        while (!ready) {
            Thread.onSpinWait();
        }

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

        assertEquals("2", response);
    }


    @Test
    @Order(3)
    @DisplayName("Test Login User")
    void testLoginUser() throws IOException {

        command = "curl -X POST http://localhost:10007/sessions --header \"Content-Type: application/json\" -d {\"Username\":\"kienboec\",\"Password\":\"daniel\"}";


        while (!ready) {
            Thread.onSpinWait();
        }

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
        assertEquals("Basic kienboec-mtcgToken", response);
    }

    @Test
    @Order(4)
    @DisplayName("Test Login User 2nd")
    void testLoginUser2nd() throws IOException {

        command = "curl -X POST http://localhost:10007/sessions --header \"Content-Type: application/json\" -d {\"Username\":\"altenhof\",\"Password\":\"markus\"}";


        while (!ready) {
            Thread.onSpinWait();
        }

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
        assertEquals("Basic altenhof-mtcgToken", response);
    }

    @Test
    @Order(5)
    @DisplayName("Test Login User Fail")
    void testLoginUserFail() throws IOException {

        command = "curl -X POST http://localhost:10007/sessions --header \"Content-Type: application/json\" -d {\"Username\":\"kienboec\",\"Password\":\"different\"}";


        while (!ready) {
            Thread.onSpinWait();
        }

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
        assertEquals("Invalid Credentials or already logged in", response);
    }

    @Test
    @Order(6)
    @DisplayName("Test Show User")
    void testShowUser() throws IOException {


        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic kienboec-mtcgToken");
        headers.put("Content-Type", "application/json");
        RequestContext requestContext = new RequestContext("GET /users/kienboec HTTP/1.1", headers, "");

        IHTTPMethod method = new GET_user_name(userRepo);
        User user = new User();
        if (method.analyse(requestContext)) {
            user = mapper.readValue(method.exec(requestContext).getPayload(), User.class);
        }


        assertEquals("kienboec", user.getCredentials().getUsername());
    }

    @Test
    @Order(7)
    @DisplayName("Test Show User fail")
    void testShowUserFail() throws IOException {


        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic kienboec-mtcgToken");
        headers.put("Content-Type", "application/json");
        RequestContext requestContext = new RequestContext("GET /users/altenhof HTTP/1.1", headers, "");

        IHTTPMethod method = new GET_user_name(userRepo);
        String response = "";
        if (method.analyse(requestContext)) {
            response = method.exec(requestContext).getPayload();
        }


        assertEquals("Insufficient Permissions", response);
    }

    @Test
    @Order(8)
    @DisplayName("Test Edit User")
    void testEditUser() throws IOException {


        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic kienboec-mtcgToken");
        headers.put("Content-Type", "application/json");
        RequestContext requestContext = new RequestContext("PUT /users/kienboec HTTP/1.1", headers, "{\"Name\": \"Hoax\",  \"Bio\": \"me playin...\", \"Image\": \":-)\"}");

        IHTTPMethod method = new PUT_users_name(userRepo);
        User user = new User();
        if (method.analyse(requestContext)) {
            method.exec(requestContext).getPayload();
            user = userRepo.getUserWithToken("Basic kienboec-mtcgToken");
        }


        assertEquals(":-)", user.getEditableUserData().getImage());
    }

    @Test
    @Order(9)
    @DisplayName("Test Edit User fail")
    void testEditUserFail() throws IOException {


        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic kienboec-mtcgToken");
        headers.put("Content-Type", "application/json");
        RequestContext requestContext = new RequestContext("PUT /users/altenhof HTTP/1.1", headers, "{\"Name\": \"Hoax\",  \"Bio\": \"me playin...\", \"Image\": \":-)\"}");

        IHTTPMethod method = new PUT_users_name(userRepo);
        User user = new User();
        if (method.analyse(requestContext)) {
            method.exec(requestContext).getPayload();
            user = userRepo.getUserWithToken("Basic kienboec-mtcgToken");
        }


        assertEquals(":-)", user.getEditableUserData().getImage());
    }

    @Test
    @Order(10)
    @DisplayName("Test Show Stats")
    void testShowStats() throws IOException {


        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic kienboec-mtcgToken");
        headers.put("Content-Type", "application/json");
        RequestContext requestContext = new RequestContext("GET /stats HTTP/1.1", headers, "");

        IHTTPMethod method = new GET_stats(userRepo);
        Stats stats = new Stats();
        if (method.analyse(requestContext)) {
            stats = mapper.readValue(method.exec(requestContext).getPayload(), Stats.class);
        }


        assertEquals(100, stats.getMmr());
    }

    @Test
    @Order(11)
    @DisplayName("Test Show Scoreboard")
    void testShowScoreboard() throws IOException {


        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic kienboec-mtcgToken");
        headers.put("Content-Type", "application/json");
        RequestContext requestContext = new RequestContext("GET /score HTTP/1.1", headers, "");

        IHTTPMethod method = new GET_score(userRepo);
        List<Stats> score = new ArrayList<>();
        if (method.analyse(requestContext)) {
            score = mapper.readValue(method.exec(requestContext).getPayload(), new TypeReference<>() {
            });
        }


        assertEquals(2, score.size());
    }

}