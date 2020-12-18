package IntegrationTests;

import Service.RESTServer.Model.Message;
import Service.RESTServer.Service.Methods.DELETE.DELETE_messages_Id;
import Service.RESTServer.Service.Methods.Error.NotFound;
import Service.RESTServer.Service.Methods.GET.GET_messages;
import Service.RESTServer.Service.Methods.GET.GET_messages_Id;
import Service.RESTServer.Service.Methods.IHTTPMethod;
import Service.RESTServer.Service.Methods.POST.POST_messages;
import Service.RESTServer.Service.Methods.PUT.PUT_messages_Id;
import Service.RESTServer.Service.Socket.MySocket;
import Service.RESTServer.Service.WorkerThread;
import com.fasterxml.jackson.core.type.TypeReference;
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
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//Start whole test class otherwise the Tests will fail
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestHTTPMethods {

    String command = "";
    String command2 = "";
    ObjectMapper mapper = new ObjectMapper();
    static Message testMessage = new Message();

    private static ServerSocket _listener = null;

    static MySocket socket;
    static List<IHTTPMethod> methods = new ArrayList<>();
    static Thread workerThread;
    static boolean ready = false;

    @BeforeAll
    static void setUp() {
        methods.add(new GET_messages());
        methods.add(new POST_messages());
        methods.add(new GET_messages_Id());
        methods.add(new DELETE_messages_Id());
        methods.add(new PUT_messages_Id());
        methods.add(new NotFound());


        workerThread = new Thread(() -> {
            try {
                _listener = new ServerSocket(8000, 5);
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

        workerThread.stop();

    }

    @Test
    @Order(1)
    @DisplayName("Test Create Message")
    void testCreateMessages() throws IOException {

        command = "curl -X POST -H \"Content-type: plain/text\" -d test localhost:8000/messages";
        testMessage = new Message();
        testMessage.setContent("test");
        boolean gotId;

        while (!ready) ;

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
        try {
            testMessage.setId(Integer.parseInt(response));
            gotId = true;
        } catch (Exception e) {
            gotId = false;
        }

        assertTrue(gotId);


    }


    @Test
    @Order(2)
    @DisplayName("Test Get Messages")
    void testGetMessages() throws IOException {

        command = "curl -X GET -H \"Content-type: plain/text\" localhost:8000/messages";

        AtomicBoolean containsMessage = new AtomicBoolean(false);

        while (!ready) ;

        Process process = Runtime.getRuntime().exec(command);

        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (process.getInputStream(), Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }
        List<Message> messages = mapper.readValue(textBuilder.toString(), new TypeReference<>() {
        });
        messages.forEach(x -> {
            if (x.equals(testMessage))
                containsMessage.set(true);
        });
        assertTrue(containsMessage.get());
    }

    @Test
    @Order(3)
    @DisplayName("Test Get Message by Id")
    void testGetMessageById() throws IOException {

        command = "curl -X GET -H \"Content-type: plain/text\" localhost:8000/messages/" + testMessage.getId();


        while (!ready) ;

        Process process = Runtime.getRuntime().exec(command);

        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (process.getInputStream(), Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }
        Message recievedMessage = mapper.readValue(textBuilder.toString(), Message.class);

        assertEquals(testMessage, recievedMessage);
    }

    @Test
    @Order(4)
    @DisplayName("Test Change Message by Id")
    void testChangeMessageById() throws IOException, InterruptedException {

        command = "curl -X PUT -H \"Content-type: plain/text\" -d testChanged localhost:8000/messages/" + testMessage.getId();
        command2 = "curl -X GET -H \"Content-type: plain/text\" localhost:8000/messages/" + testMessage.getId();

        while (!ready) ;

        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();
        Process process2 = Runtime.getRuntime().exec(command2);

        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (process2.getInputStream(), Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }
        Message recievedMessage = mapper.readValue(textBuilder.toString(), Message.class);

        assertEquals("testChanged", recievedMessage.getContent());
    }

    @Test
    @Order(5)
    @DisplayName("Test Delete Message by Id")
    void testDeleteMessageById() throws IOException, InterruptedException {

        command = "curl -X DELETE -H \"Content-type: plain/text\" localhost:8000/messages/" + testMessage.getId();
        command2 = "curl -X GET -H \"Content-type: plain/text\" localhost:8000/messages/" + testMessage.getId();

        while (!ready) ;

        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();
        Process process2 = Runtime.getRuntime().exec(command2);

        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (process2.getInputStream(), Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }

        assertEquals("", textBuilder.toString());
    }


}
