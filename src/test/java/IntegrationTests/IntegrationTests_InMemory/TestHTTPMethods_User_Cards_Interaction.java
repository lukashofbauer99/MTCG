package IntegrationTests.IntegrationTests_InMemory;

import Domain.Cards.InMemory.*;
import Domain.Cards.Interfaces.*;
import Domain.PlayerHub;
import Domain.User.InMemory.InMemoryITradeRepository;
import Domain.User.InMemory.InMemoryUserRepository;
import Domain.User.Interfaces.ITradeRepository;
import Domain.User.Interfaces.IUserRepository;
import Model.Battle.Battle;
import Model.Cards.ACard;
import Model.User.Deck;
import Model.User.Trade.ITrade;
import Model.User.User;
import Service.RESTServer.Service.Methods.DELETE.DELETE_tradings_id;
import Service.RESTServer.Service.Methods.GET.GET_cards;
import Service.RESTServer.Service.Methods.GET.GET_deck;
import Service.RESTServer.Service.Methods.GET.GET_deck_plain;
import Service.RESTServer.Service.Methods.GET.GET_tradings;
import Service.RESTServer.Service.Methods.IHTTPMethod;
import Service.RESTServer.Service.Methods.POST.*;
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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

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

    static PlayerHub playerHub = new PlayerHub();

    static ICardPackRepository cardPackRepo=new InMemoryCardPackRepository();
    static IACardRepository cardRepo=new InMemoryACardRepository();
    static IEffectRepository effectRepository= new InMemoryIEffectRepository();
    static IRaceRepository raceRepository= new InMemoryIRaceRepository();
    static IVendorRepository vendorRepository= new InMemoryIVendorRepository();
    static IUserRepository userRepository= new InMemoryUserRepository();
    static ITradeRepository tradeRepository= new InMemoryITradeRepository();


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

    //region Create user1
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
    //endregion

    @Test
    @Order(5)
    @DisplayName("Test Buy NormalPackage No Package availible")
    void testBuyNormalPackageFail() throws IOException {

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
    @Order(7)
    @DisplayName("Test (no) show trades")
    void testNpShowTrades() throws IOException {

        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","Basic kienboec-mtcgToken");
        headers.put("Content-Type","application/json");
        RequestContext requestContext = new RequestContext("GET /tradings HTTP/1.1",headers,"");

        IHTTPMethod method = new GET_tradings(userRepository,tradeRepository);
        List<ITrade> trades = new ArrayList<>();
        if(method.analyse(requestContext)) {
            trades = mapper.readValue(method.exec(requestContext).getPayload(),new TypeReference<>() {});
        }


        assertEquals(0,trades.size());
    }

    @Test
    @Order(8)
    @DisplayName("Test create trade")
    void testCreateTrade() throws IOException {

        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","Basic kienboec-mtcgToken");
        headers.put("Content-Type","application/json");
        RequestContext requestContext = new RequestContext("POST /tradings HTTP/1.1",headers,"{\"Id\": \"6cd85277-4590-49d4-b0cf-ba0a921faad0\", \"CardToTrade\": \"1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\", \"Type\": \"monster\", \"MinimumDamage\": 15}");

        IHTTPMethod method = new POST_tradings(userRepository,cardRepo,tradeRepository);
        if(method.analyse(requestContext)) {
            method.exec(requestContext);
        }


        assertEquals(1,tradeRepository.getAllEntities().size());
    }

    @Test
    @Order(9)
    @DisplayName("Test show trades")
    void testShowTrades() throws IOException {

        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","Basic kienboec-mtcgToken");
        headers.put("Content-Type","application/json");
        RequestContext requestContext = new RequestContext("GET /tradings HTTP/1.1",headers,"");

        IHTTPMethod method = new GET_tradings(userRepository,tradeRepository);
        List<ITrade> trades = new ArrayList<>();
        if(method.analyse(requestContext)) {
            trades = mapper.readValue(method.exec(requestContext).getPayload(),new TypeReference<>() {});
        }


        assertEquals(1,trades.size());
    }

    //region create user2
    @Test
    @Order(10)
    @DisplayName("Test Create User2")
    void testCreateUser2() throws IOException {

        //No white spaces in Json Object allowed, if the request is sent from java, because the Content-Type get evaluated automatically
        command = "curl -X POST http://localhost:10003/users -H \"Content-Type: application/json\" -d {\"Username\":\"altenhof\",\"Password\":\"markus\"}";
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

        assertEquals("2",response);
    }


    @Test
    @Order(11)
    @DisplayName("Test Login User2")
    void testLoginUser2() throws IOException{

        command = "curl -X POST http://localhost:10003/sessions --header \"Content-Type: application/json\" -d {\"Username\":\"altenhof\",\"Password\":\"markus\"}";


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
        assertEquals("Basic altenhof-mtcgToken",response);
    }

    @Test
    @Order(12)
    @DisplayName("Test Create NormalPackage2")
    void testCreateNormalPackage2() throws IOException {

        //No white spaces in Json Object allowed, if the request is sent from java, because the Content-Type get evaluated automatically
        command= "curl -X POST http://localhost:10003/packages --header \"Content-Type: application/json\" -d [{\"@type\":\"Monster\",\"Id\":\"644808c2-f87a-4600-b313-122b02322fd5\",\"Name\":\"WaterGoblin\",\"Damage\":9.0},{\"@type\":\"Monster\",\"Id\":\"4a2757d6-b1c3-47ac-b9a3-91deab093531\",\"Name\":\"Dragon\",\"Damage\":55.0},{\"@type\":\"Spell\",\"Id\":\"91a6471b-1426-43f6-ad65-6fc473e16f9f\",\"Name\":\"WaterSpell\",\"Damage\":21.0},{\"@type\":\"Monster\",\"Id\":\"4ec8b269-0dfa-4f97-809a-2c63fe2a0025\",\"Name\":\"Ork\",\"Damage\":55.0},{\"@type\":\"Spell\",\"Id\":\"f8043c23-1534-4487-b66b-238e0c3c39b5\",\"Name\":\"WaterSpell\",\"Damage\":23.0}]";

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

        assertEquals("2",response);
    }

    @Test
    @Order(13)
    @DisplayName("Test Buy NormalPackage2")
    void testBuyNormalPackage2() throws IOException {

        while (!ready) {
            Thread.onSpinWait();
        }

        //Doesnt work Curls behaves strange when executed from java
        //command = "curl -X POST http://localhost:10003/transactions/packages --header \"Content-Type: application/json\" --header Authorization:Basic kienboec-mtcgToken";
        //Process process= Runtime.getRuntime().exec(command);

        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","Basic altenhof-mtcgToken");
        headers.put("Content-Type","application/json");
        RequestContext requestContext = new RequestContext("POST /transactions/packages HTTP/1.1",headers,"");

        IHTTPMethod method = new POST_transaction_packages(userRepository,vendorRepository);
        if(method.analyse(requestContext))
            method.exec(requestContext);


        assertEquals(5,userRepository.getUserWithToken("Basic altenhof-mtcgToken").getStack().getCards().size());
    }

    //endregion

    @Test
    @Order(14)
    @DisplayName("Test accept Trade card not own")
    void testAcceptTradeCardNotOwned() throws IOException {

        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","Basic altenhof-mtcgToken");
        headers.put("Content-Type","application/json");
        RequestContext requestContext = new RequestContext("POST /tradings/6cd85277-4590-49d4-b0cf-ba0a921faad0 HTTP/1.1",headers,"99f8f8dc-e25e-4a95-aa2c-782823f36e2a");

        User user = userRepository.getUserWithToken("Basic altenhof-mtcgToken");
        IHTTPMethod method = new POST_tradings_id(userRepository,cardRepo,tradeRepository);
        if(method.analyse(requestContext)) {
            method.exec(requestContext);
        }


        assertFalse(user.getStack().getCards().contains(cardRepo.findEntity("1cb6ab86-bdb2-47e5-b6e4-68c5ab389334")));
    }

    @Test
    @Order(15)
    @DisplayName("Test accept Trade with self")
    void testAcceptTradeSelf() throws IOException {

        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","Basic kienboec-mtcgToken");
        headers.put("Content-Type","application/json");
        RequestContext requestContext = new RequestContext("POST /tradings/6cd85277-4590-49d4-b0cf-ba0a921faad0 HTTP/1.1",headers,"99f8f8dc-e25e-4a95-aa2c-782823f36e2a");

        User user = userRepository.getUserWithToken("Basic altenhof-mtcgToken");
        IHTTPMethod method = new POST_tradings_id(userRepository,cardRepo,tradeRepository);
        if(method.analyse(requestContext)) {
            method.exec(requestContext);
        }


        assertFalse(user.getStack().getCards().contains(cardRepo.findEntity("1cb6ab86-bdb2-47e5-b6e4-68c5ab389334")));
    }

    @Test
    @Order(16)
    @DisplayName("Test accept Trade")
    void testAcceptTrade() throws IOException {

        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","Basic altenhof-mtcgToken");
        headers.put("Content-Type","application/json");
        RequestContext requestContext = new RequestContext("POST /tradings/6cd85277-4590-49d4-b0cf-ba0a921faad0 HTTP/1.1",headers,"4a2757d6-b1c3-47ac-b9a3-91deab093531");

        User user = userRepository.getUserWithToken("Basic altenhof-mtcgToken");
        IHTTPMethod method = new POST_tradings_id(userRepository,cardRepo,tradeRepository);
        if(method.analyse(requestContext)) {
            method.exec(requestContext);
        }


        assertTrue(user.getStack().getCards().contains(cardRepo.findEntity("1cb6ab86-bdb2-47e5-b6e4-68c5ab389334")));
    }

    @Test
    @Order(17)
    @DisplayName("Test create trade2")
    void testCreateTrade2() throws IOException {

        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","Basic altenhof-mtcgToken");
        headers.put("Content-Type","application/json");
        RequestContext requestContext = new RequestContext("POST /tradings HTTP/1.1",headers,"{\"Id\": \"7cd85277-4590-49d4-b0cf-ba0a921faad0\", \"CardToTrade\": \"1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\", \"Type\": \"monster\", \"MinimumDamage\": 15}");

        IHTTPMethod method = new POST_tradings(userRepository,cardRepo,tradeRepository);
        if(method.analyse(requestContext)) {
            method.exec(requestContext);
        }


        assertEquals(1,tradeRepository.getAllEntities().size());
    }

    @Test
    @Order(18)
    @DisplayName("Test accept Trade2")
    void testAcceptTrade2() throws IOException {

        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","Basic kienboec-mtcgToken");
        headers.put("Content-Type","application/json");
        RequestContext requestContext = new RequestContext("POST /tradings/7cd85277-4590-49d4-b0cf-ba0a921faad0 HTTP/1.1",headers,"4a2757d6-b1c3-47ac-b9a3-91deab093531");

        User user = userRepository.getUserWithToken("Basic kienboec-mtcgToken");
        IHTTPMethod method = new POST_tradings_id(userRepository,cardRepo,tradeRepository);
        if(method.analyse(requestContext)) {
            method.exec(requestContext);
        }


        assertTrue(user.getStack().getCards().contains(cardRepo.findEntity("1cb6ab86-bdb2-47e5-b6e4-68c5ab389334")));
    }

    @Test
    @Order(19)
    @DisplayName("Test create trade3")
    void testCreateTrade3() throws IOException {

        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","Basic kienboec-mtcgToken");
        headers.put("Content-Type","application/json");
        RequestContext requestContext = new RequestContext("POST /tradings HTTP/1.1",headers,"{\"Id\": \"8cd85277-4590-49d4-b0cf-ba0a921faad0\", \"CardToTrade\": \"1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\", \"Type\": \"monster\", \"MinimumDamage\": 15}");

        IHTTPMethod method = new POST_tradings(userRepository,cardRepo,tradeRepository);
        if(method.analyse(requestContext)) {
            method.exec(requestContext);
        }


        assertEquals(1,tradeRepository.getAllEntities().size());
    }

    @Test
    @Order(20)
    @DisplayName("Test delete trade3")
    void testDeleteTrade3() throws IOException {

        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","Basic kienboec-mtcgToken");
        headers.put("Content-Type","application/json");
        RequestContext requestContext = new RequestContext("DELETE /tradings/8cd85277-4590-49d4-b0cf-ba0a921faad0 HTTP/1.1",headers,"");

        IHTTPMethod method = new DELETE_tradings_id(userRepository,tradeRepository);
        if(method.analyse(requestContext)) {
            method.exec(requestContext);
        }


        assertEquals(0,tradeRepository.getAllEntities().size());
    }

    @Test
    @Order(20)
    @DisplayName("Test unconfigured Show Deck Of User")
    void testShowUnconfDeckOfUser() throws IOException {

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
    @Order(21)
    @DisplayName("Test Define Deck Of User")
    void testDefineDeckOfUser() throws IOException {

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
    @Order(22)
    @DisplayName("Test Show Deck Of User")
    void testShowDeckOfUser() throws IOException {

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
    @Order(23)
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


    @Test
    @Order(24)
    @DisplayName("Test Show Deck Of User other Format")
    void testShowDeckOfUserOtherFormat() throws IOException {

        //command= "curl -X GET http://localhost:10003/cards --header Authorization: Basic kienboec-mtcgToken";
        //Process process = Runtime.getRuntime().exec(command);


        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","Basic kienboec-mtcgToken");
        headers.put("Content-Type","application/json");
        RequestContext requestContext = new RequestContext("GET /deck?format=plain HTTP/1.1",headers,"");

        IHTTPMethod method = new GET_deck_plain(userRepository);
        String deck="";
        if(method.analyse(requestContext)) {
            deck = method.exec(requestContext).getPayload();
        }


        assertEquals("""

                          cards = [\s
                            @type = Monster,
                            id = 845f0dc7-37d0-426e-994e-43fc3ac83c08,
                            name = WaterGoblin,
                            damage = 10.0,
                            effect =\s
                              @type = Water,
                              id = 3,
                              name = water,
                              base =\s
                                @type = Base,
                                id = 1,
                                name = base,
                                base = null
                             \s
                            ,
                            race =\s
                              @type = Goblin,
                              id = 2,
                              name = goblin,
                              base =\s
                                @type = Base,
                                id = 1,
                                name = baseRace,
                                base = null
                             \s
                           \s
                          ,\s
                            @type = Monster,
                            id = 99f8f8dc-e25e-4a95-aa2c-782823f36e2a,
                            name = Dragon,
                            damage = 50.0,
                            effect =\s
                              @type = Normal,
                              id = 4,
                              name = normal,
                              base =\s
                                @type = Base,
                                id = 1,
                                name = base,
                                base = null
                             \s
                            ,
                            race =\s
                              @type = Dragon,
                              id = 3,
                              name = dragon,
                              base =\s
                                @type = Base,
                                id = 1,
                                name = baseRace,
                                base = null
                             \s
                           \s
                          ,\s
                            @type = Spell,
                            id = e85e3976-7c86-4d06-9a80-641c2019a79f,
                            name = WaterSpell,
                            damage = 20.0,
                            effect =\s
                              @type = Water,
                              id = 3,
                              name = water,
                              base =\s
                                @type = Base,
                                id = 1,
                                name = base,
                                base = null
                             \s
                           \s
                          ,\s
                            @type = Monster,
                            id = 1cb6ab86-bdb2-47e5-b6e4-68c5ab389334,
                            name = Ork,
                            damage = 45.0,
                            effect =\s
                              @type = Normal,
                              id = 4,
                              name = normal,
                              base =\s
                                @type = Base,
                                id = 1,
                                name = base,
                                base = null
                             \s
                            ,
                            race =\s
                              @type = Ork,
                              id = 5,
                              name = ork,
                              base =\s
                                @type = Base,
                                id = 1,
                                name = baseRace,
                                base = null
                             \s
                           \s
                           ]
                        """
                ,deck);
    }



    class MatchPlayersCallable implements Callable<Battle>
    {

        String token;

        public MatchPlayersCallable(String token) {
            this.token=token;
        }

        public Battle call() throws Exception
        {
            Map<String,String> headers = new HashMap<>();
            headers.put("Authorization",token);
            headers.put("Content-Type","application/json");
            RequestContext requestContext = new RequestContext("POST /battles HTTP/1.1",headers,"");

            IHTTPMethod method = new POST_battles(userRepository,playerHub);
            Battle battle=null;
            if(method.analyse(requestContext)) {
                battle=mapper.readValue((method.exec(requestContext).getPayload()), new TypeReference<>(){});
            }
            System.out.println(battle.getWinner().getId());

            return battle;
        }

    }


    @Test
    @Order(25)
    @DisplayName("Test Matchmaking")
    public void testMatchmaking() throws ExecutionException, InterruptedException {

        //Thread 1
        FutureTask<Battle> task = new FutureTask<>(new MatchPlayersCallable("Basic kienboec-mtcgToken"));

        Thread thread1=new Thread(task);
        thread1.start();

        Thread.sleep(100);
        //Thread2
        FutureTask<Battle> task2 = new FutureTask<>(new MatchPlayersCallable("Basic altenhof-mtcgToken"));
        Thread thread2=new Thread(task2);
        thread2.start();



        assertNotNull(task2.get());
        assertNotNull(task.get());

    }




}