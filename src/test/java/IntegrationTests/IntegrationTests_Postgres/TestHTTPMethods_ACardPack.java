package IntegrationTests.IntegrationTests_Postgres;

import Domain.Cards.InMemory.*;
import Domain.Cards.Interfaces.*;
import Domain.User.InMemory.InMemoryUserRepository;
import Domain.User.Interfaces.IUserRepository;
import Service.RESTServer.Service.Methods.IHTTPMethod;
import Service.RESTServer.Service.Methods.POST.POST_NormalPackages;
import Service.RESTServer.Service.Socket.MySocket;
import Service.RESTServer.Service.WorkerThread;
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
public class TestHTTPMethods_ACardPack {

    String command = "";

    private static ServerSocket _listener = null;

    static MySocket socket;
    static List<IHTTPMethod> methods= new ArrayList<>();
    static Thread workerThread;
    static volatile boolean ready= false;

    static IUserRepository userRepo=new InMemoryUserRepository();


    @BeforeAll
    static void setUp() {
        ICardPackRepository cardPackRepo=new InMemoryCardPackRepository();
        IACardRepository cardRepo=new InMemoryACardRepository();
        IEffectRepository effectRepository= new InMemoryIEffectRepository();
        IRaceRepository raceRepository= new InMemoryIRaceRepository();
        IVendorRepository vendorRepository= new InMemoryIVendorRepository();

        methods.add(new POST_NormalPackages(cardPackRepo,cardRepo,effectRepository,raceRepository,vendorRepository));

        workerThread = new Thread(()-> {
            try {
                _listener = new ServerSocket(10006, 5);
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
    @DisplayName("Test Create NormalPackage")
    void testCreateNormalPackage() throws IOException {

        //No white spaces in Json Object allowed, if the request is sent from java, because the Content-Type get evaluated automatically
        command= "curl -X POST http://localhost:10006/packages --header \"Content-Type: application/json\" -d [{\"@type\":\"Monster\",\"Id\":\"845f0dc7-37d0-426e-994e-43fc3ac83c08\",\"Name\":\"WaterGoblin\",\"Damage\":10.0},{\"@type\":\"Monster\",\"Id\":\"99f8f8dc-e25e-4a95-aa2c-782823f36e2a\",\"Name\":\"Dragon\",\"Damage\":50.0},{\"@type\":\"Spell\",\"Id\":\"e85e3976-7c86-4d06-9a80-641c2019a79f\",\"Name\":\"WaterSpell\",\"Damage\":20.0},{\"@type\":\"Monster\",\"Id\":\"1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\",\"Name\":\"Ork\",\"Damage\":45.0},{\"@type\":\"Spell\",\"Id\":\"dfdd758f-649c-40f9-ba3a-8657f4b3439f\",\"Name\":\"FireSpell\",\"Damage\":25.0}]";

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

        assertEquals("1",response);
    }
}