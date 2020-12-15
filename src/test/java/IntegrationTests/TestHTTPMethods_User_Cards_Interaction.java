package IntegrationTests;

import Domain.Cards.InMemory.*;
import Domain.Cards.Interfaces.*;
import Domain.User.InMemory.InMemoryUserRepository;
import Domain.User.Interfaces.IUserRepository;
import Model.Cards.ACard;
import Model.User.Deck;
import Service.RESTServer.Service.Methods.GET.GET_cards;
import Service.RESTServer.Service.Methods.GET.GET_deck;
import Service.RESTServer.Service.Methods.IHTTPMethod;
import Service.RESTServer.Service.Methods.POST.POST_NormalPackages;
import Service.RESTServer.Service.Methods.POST.POST_sessions;
import Service.RESTServer.Service.Methods.POST.POST_transaction_packages;
import Service.RESTServer.Service.Methods.POST.POST_users;
import Service.RESTServer.Service.Methods.PUT.PUT_deck;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.Long.parseLong;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

//Start whole test class otherwise the Tests will fail
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestHTTPMethods_User_Cards_Interaction {

    String command = "";

    private static ServerSocket _listener = null;

    static ObjectMapper mapper = new ObjectMapper();
    static MySocket socket;
    static List<IHTTPMethod> methods= new ArrayList<>();
    static Thread workerThread;
    static volatile boolean ready= false;

    static ICardPackRepository cardPackRepo=new InMemoryCardPackRepository();
    static IACardRepository cardRepo=new InMemoryACardRepository();
    static IEffectRepository effectRepository= new InMemoryIEffectRepository();
    static IRaceRepository raceRepository= new InMemoryIRaceRepository();
    static IVendorRepository vendorRepository= new InMemoryIVendorRepository();
    static IUserRepository userRepository= new InMemoryUserRepository();


    @BeforeAll
    static void setUp() {
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        cardPackRepo=new InMemoryCardPackRepository();
        cardRepo=new InMemoryACardRepository();
        effectRepository= new InMemoryIEffectRepository();
        raceRepository= new InMemoryIRaceRepository();
        vendorRepository= new InMemoryIVendorRepository();

        methods.add(new GET_cards(userRepository));
        methods.add(new POST_NormalPackages(cardPackRepo,cardRepo,effectRepository,raceRepository,vendorRepository));
        methods.add(new POST_users(userRepository));
        methods.add(new POST_sessions(userRepository));
        methods.add(new POST_transaction_packages(userRepository,vendorRepository));

        workerThread = new Thread(()-> {
            try {
                _listener = new ServerSocket(10003, 5);
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
    void testCreateUser() throws IOException {

        //No white spaces in Json Object allowed, if the request is sent from java, because the Content-Type get evaluated automatically
        command = "curl -X POST http://localhost:10003/users -H \"Content-Type: application/json\" -d {\"Username\":\"kienboec\",\"Password\":\"daniel\"}";
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


    @Test
    @Order(2)
    @DisplayName("Test Login User")
    void testLoginUser() throws IOException{

        command = "curl -X POST http://localhost:10003/sessions --header \"Content-Type: application/json\" -d {\"Username\":\"kienboec\",\"Password\":\"daniel\"}";


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
        assertEquals("Basic kienboec-mtcgToken",response);
    }

    @Test
    @Order(3)
    @DisplayName("Test Create NormalPackage")
    void testCreateNormalPackage() throws IOException {

        //No white spaces in Json Object allowed, if the request is sent from java, because the Content-Type get evaluated automatically
        command= "curl -X POST http://localhost:10003/packages --header \"Content-Type: application/json\" -d [{\"@type\":\"Monster\",\"Id\":\"845f0dc7-37d0-426e-994e-43fc3ac83c08\",\"Name\":\"WaterGoblin\",\"Damage\":10.0},{\"@type\":\"Monster\",\"Id\":\"99f8f8dc-e25e-4a95-aa2c-782823f36e2a\",\"Name\":\"Dragon\",\"Damage\":50.0},{\"@type\":\"Spell\",\"Id\":\"e85e3976-7c86-4d06-9a80-641c2019a79f\",\"Name\":\"WaterSpell\",\"Damage\":20.0},{\"@type\":\"Monster\",\"Id\":\"1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\",\"Name\":\"Ork\",\"Damage\":45.0},{\"@type\":\"Spell\",\"Id\":\"dfdd758f-649c-40f9-ba3a-8657f4b3439f\",\"Name\":\"FireSpell\",\"Damage\":25.0}]";

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

    @Test
    @Order(4)
    @DisplayName("Test Buy NormalPackage")
    void testBuyNormalPackage() throws IOException {

        while (!ready) {
            Thread.onSpinWait();
        }

        //Doesnt work Curls behaves strange when executed from java
        //command = "curl -X POST http://localhost:10003/transactions/packages --header \"Content-Type: application/json\" --header Authorization:Basic kienboec-mtcgToken";
        //Process process= Runtime.getRuntime().exec(command);

        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","Basic kienboec-mtcgToken");
        headers.put("Content-Type","application/json");
        RequestContext requestContext = new RequestContext("POST /transactions/packages HTTP/1.1",headers,"");

        IHTTPMethod method = new POST_transaction_packages(userRepository,vendorRepository);
        if(method.analyse(requestContext))
            method.exec(requestContext);


        assertEquals(5,userRepository.getAllEntities().stream().findFirst().orElse(null).getStack().getCards().size());
    }

    @Test
    @Order(5)
    @DisplayName("Test Buy NormalPackage No Package availible")
    void testBuyNormalPackageFail() throws IOException {

        //Doesnt work Curls behaves strange when executed from java
        //command = "curl -X POST http://localhost:10003/transactions/packages --header \"Content-Type: application/json\" --header Authorization:Basic kienboec-mtcgToken";
        //Process process= Runtime.getRuntime().exec(command);

        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","Basic kienboec-mtcgToken");
        headers.put("Content-Type","application/json");
        RequestContext requestContext = new RequestContext("POST /transactions/packages HTTP/1.1",headers,"");

        IHTTPMethod method = new POST_transaction_packages(userRepository,vendorRepository);
        if(method.analyse(requestContext))
            method.exec(requestContext);


        assertEquals(5,userRepository.getAllEntities().stream().findFirst().orElse(null).getStack().getCards().size());
    }

    @Test
    @Order(6)
    @DisplayName("Test Show Cards Of User")
    void testShowCardsOfUser() throws IOException {

        //command= "curl -X GET http://localhost:10003/cards --header Authorization: Basic kienboec-mtcgToken";
        //Process process = Runtime.getRuntime().exec(command);


        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","Basic kienboec-mtcgToken");
        headers.put("Content-Type","application/json");
        RequestContext requestContext = new RequestContext("GET /cards HTTP/1.1",headers,"");

        IHTTPMethod method = new GET_cards(userRepository);
        List<ACard> cards= new ArrayList<>();
        if(method.analyse(requestContext)) {
            cards = mapper.readValue(method.exec(requestContext).getPayload(), new TypeReference<>() {
            });
        }


        assertEquals(5,cards.size());
    }

    @Test
    @Order(6)
    @DisplayName("Test unconfigured Show Deck Of User")
    void testShowUnconfDeckOfUser() throws IOException {

        //command= "curl -X GET http://localhost:10003/cards --header Authorization: Basic kienboec-mtcgToken";
        //Process process = Runtime.getRuntime().exec(command);


        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","Basic kienboec-mtcgToken");
        headers.put("Content-Type","application/json");
        RequestContext requestContext = new RequestContext("GET /deck HTTP/1.1",headers,"");

        IHTTPMethod method = new GET_deck(userRepository);
        Deck deck = new Deck();
        if(method.analyse(requestContext)) {
            deck = mapper.readValue(method.exec(requestContext).getPayload(), Deck.class);
        }


        assertEquals(0,deck.getCards().size());
    }

    @Test
    @Order(7)
    @DisplayName("Test Define Deck Of User")
    void testDefineDeckOfUser() throws IOException {

        //command= "curl -X GET http://localhost:10003/cards --header Authorization: Basic kienboec-mtcgToken";
        //Process process = Runtime.getRuntime().exec(command);


        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","Basic kienboec-mtcgToken");
        headers.put("Content-Type","application/json");
        RequestContext requestContext = new RequestContext("PUT /deck HTTP/1.1",headers,"[\"845f0dc7-37d0-426e-994e-43fc3ac83c08\", \"99f8f8dc-e25e-4a95-aa2c-782823f36e2a\", \"e85e3976-7c86-4d06-9a80-641c2019a79f\", \"1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\"]");

        IHTTPMethod method = new PUT_deck(userRepository,cardRepo);
        Deck deck = new Deck();
        List<String> SentIds= new ArrayList<>();
        List<String>idsInDeck= new ArrayList<>();
        if(method.analyse(requestContext)) {
            SentIds=mapper.readValue((requestContext.getPayload()), new TypeReference<>(){});
            method.exec(requestContext);
            idsInDeck=userRepository.getUserWithToken("Basic kienboec-mtcgToken").getDeck().getCards().stream().map(ACard::getId).collect(Collectors.toList());

        }


        assertEquals(SentIds,idsInDeck);
    }


    @Test
    @Order(8)
    @DisplayName("Test Show Deck Of User")
    void testShowDeckOfUser() throws IOException {

        //command= "curl -X GET http://localhost:10003/cards --header Authorization: Basic kienboec-mtcgToken";
        //Process process = Runtime.getRuntime().exec(command);


        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","Basic kienboec-mtcgToken");
        headers.put("Content-Type","application/json");
        RequestContext requestContext = new RequestContext("GET /deck HTTP/1.1",headers,"");

        IHTTPMethod method = new GET_deck(userRepository);
        Deck deck = new Deck();
        if(method.analyse(requestContext)) {
            deck = mapper.readValue(method.exec(requestContext).getPayload(), Deck.class);
        }


        assertEquals(4,deck.getCards().size());
    }


    @Test
    @Order(7)
    @DisplayName("Test Define Deck Of User Fail")
    void testDefineDeckOfUserFail() throws IOException {

        //command= "curl -X GET http://localhost:10003/cards --header Authorization: Basic kienboec-mtcgToken";
        //Process process = Runtime.getRuntime().exec(command);


        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","Basic kienboec-mtcgToken");
        headers.put("Content-Type","application/json");
        RequestContext requestContext = new RequestContext("PUT /deck HTTP/1.1",headers,"[\"WrongID\", \"99f8f8dc-e25e-4a95-aa2c-782823f36e2a\", \"e85e3976-7c86-4d06-9a80-641c2019a79f\", \"1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\"]");

        IHTTPMethod method = new PUT_deck(userRepository,cardRepo);
        Deck deck = new Deck();
        List<String> SentIds= new ArrayList<>();
        List<String>idsInDeck= new ArrayList<>();
        if(method.analyse(requestContext)) {
            SentIds=mapper.readValue((requestContext.getPayload()), new TypeReference<>(){});
            method.exec(requestContext);
            idsInDeck=userRepository.getUserWithToken("Basic kienboec-mtcgToken").getDeck().getCards().stream().map(ACard::getId).collect(Collectors.toList());

        }


        assertNotEquals(SentIds,idsInDeck);
    }



}