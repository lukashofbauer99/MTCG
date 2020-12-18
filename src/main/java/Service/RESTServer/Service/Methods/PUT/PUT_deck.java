package Service.RESTServer.Service.Methods.PUT;

import Domain.Cards.Interfaces.IACardRepository;
import Domain.User.Interfaces.IUserRepository;
import Model.Cards.ACard;
import Model.User.Deck;
import Model.User.User;
import Service.RESTServer.Service.Methods.IHTTPMethod;
import Service.RESTServer.Service.Request.IRequestContext;
import Service.RESTServer.Service.Response.IResponseContext;
import Service.RESTServer.Service.Response.ResponseContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PUT_deck implements IHTTPMethod {

    IUserRepository userRepository;
    IACardRepository cardRepository;
    ObjectMapper mapper = new ObjectMapper();

    public PUT_deck(IUserRepository userRepository, IACardRepository cardRepository) {
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }

    @Override
    public Boolean analyse(IRequestContext data) {
        return data.getHttpVerb_Res().startsWith("PUT /deck");
    }

    @Override
    public IResponseContext exec(IRequestContext data) {
        boolean worked;
        ResponseContext responseContext = new ResponseContext();
        List<String> cardIds;
        try {
            cardIds = mapper.readValue(data.getPayload(), new TypeReference<>() {
            });
            User user = userRepository.getUserWithToken(data.getHeaders().get("Authorization"));
            List<ACard> cards = cardIds.stream().map(x -> cardRepository.findEntity(x)).filter(Objects::nonNull).collect(Collectors.toList());
            if (cards.size() == 4 && user != null) {
                worked = user.defineDeck(cards);
                if (worked) {
                    responseContext.setHttpStatusCode("HTTP/1.1 200");
                } else {
                    responseContext.setHttpStatusCode("HTTP/1.1 400");
                    responseContext.setPayload(mapper.writerFor(new TypeReference<Deck>() {
                    })
                            .writeValueAsString(userRepository.getDeckOfUserWithToken(data.getHeaders().get("Authorization"))));
                }
            } else {
                responseContext.setHttpStatusCode("HTTP/1.1 400");
                responseContext.setPayload(
                        mapper.writerFor(new TypeReference<Deck>() {})
                                .with(new DefaultPrettyPrinter())
                                .writeValueAsString(userRepository.getDeckOfUserWithToken(data.getHeaders().get("Authorization"))));
            }
        } catch (JsonProcessingException e) {
            responseContext.setPayload("Invalid form of data");
        }


        responseContext.getHeaders().put("Connection", "close");
        responseContext.getHeaders().put("Content-Length", String.valueOf(responseContext.getPayload().length()));
        responseContext.getHeaders().put("Content-Type", "application/json");
        return responseContext;
    }
}