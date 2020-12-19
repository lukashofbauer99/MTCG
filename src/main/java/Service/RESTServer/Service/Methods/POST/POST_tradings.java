package Service.RESTServer.Service.Methods.POST;

import Domain.Cards.Interfaces.IACardRepository;
import Domain.Cards.Interfaces.ICardPackRepository;
import Domain.User.Interfaces.ITradeRepository;
import Domain.User.Interfaces.IUserRepository;
import Model.User.Trade.ITrade;
import Model.User.Trade.ITradeCardRequirements;
import Model.User.Trade.NormalTradeCardRequirements;
import Model.User.User;
import Service.RESTServer.Model.TradeCurlAdapter;
import Service.RESTServer.Service.Methods.IHTTPMethod;
import Service.RESTServer.Service.Request.IRequestContext;
import Service.RESTServer.Service.Response.IResponseContext;
import Service.RESTServer.Service.Response.ResponseContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;


//TODO Test with integration and curl
@AllArgsConstructor
public class POST_tradings implements IHTTPMethod {

    ObjectMapper mapper = new ObjectMapper();
    IUserRepository userRepository;
    IACardRepository cardRepository;
    ITradeRepository tradeRepository;

    public POST_tradings(IUserRepository userRepository,IACardRepository cardPackRepository,ITradeRepository tradeRepository) {
        this.userRepository = userRepository;
        this.cardRepository = cardPackRepository;
        this.tradeRepository=tradeRepository;
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

    }

    @Override
    public Boolean analyse(IRequestContext data) {
        return data.getHttpVerb_Res().startsWith("POST /tradings ");
    }

    @Override
    public IResponseContext exec(IRequestContext data) {
        ResponseContext responseContext = new ResponseContext();
        if (userRepository.UserLoggedIn(data.getHeaders().get("Authorization"))) {
            User user = userRepository.getUserWithToken(data.getHeaders().get("Authorization"));


            try {
                TradeCurlAdapter tradeCurlAdapter = mapper.readValue(data.getPayload(),TradeCurlAdapter.class);
                ITradeCardRequirements tradeCardRequirements=
                        new NormalTradeCardRequirements
                                (tradeCurlAdapter.getMinimumDamage(),
                                        Class.forName("Model.Cards."+(tradeCurlAdapter.getType().substring(0,1).toUpperCase() + tradeCurlAdapter.getType().substring(1).toLowerCase())+"Card")
                                        ,null,null);

                ITrade trade= user.createTrade(tradeCurlAdapter.getId(), cardRepository.findEntity(tradeCurlAdapter.getCardToTrade()),tradeCardRequirements);

                tradeRepository.persistEntityGenNoId(trade);
                responseContext.setHttpStatusCode("HTTP/1.1 200");

            } catch (ClassNotFoundException e) {
                responseContext.setPayload("Invalid form of Class Data");
                responseContext.setHttpStatusCode("HTTP/1.1 400");
            } catch (JsonProcessingException e) {
                responseContext.setPayload("Invalid form of Data");
                responseContext.setHttpStatusCode("HTTP/1.1 400");
            }

        } else {
            responseContext.setHttpStatusCode("HTTP/1.1 401");
            responseContext.setPayload("Not logged In");
        }

        responseContext.getHeaders().put("Connection", "close");
        responseContext.getHeaders().put("Content-Length", String.valueOf(responseContext.getPayload().length()));
        responseContext.getHeaders().put("Content-Type", "application/json");

        return responseContext;
    }
}