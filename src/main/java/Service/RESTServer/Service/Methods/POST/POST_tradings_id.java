package Service.RESTServer.Service.Methods.POST;

import Domain.Cards.Interfaces.IACardRepository;
import Domain.User.Interfaces.ITradeRepository;
import Domain.User.Interfaces.IUserRepository;
import Model.Cards.ACard;
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
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;


//TODO Test with integration and curl
@AllArgsConstructor
public class POST_tradings_id implements IHTTPMethod {

    ObjectMapper mapper = new ObjectMapper();
    IUserRepository userRepository;
    IACardRepository cardRepository;
    ITradeRepository tradeRepository;

    public POST_tradings_id(IUserRepository userRepository, IACardRepository cardPackRepository, ITradeRepository tradeRepository) {
        this.userRepository = userRepository;
        this.cardRepository = cardPackRepository;
        this.tradeRepository=tradeRepository;
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

    }

    @Override
    public Boolean analyse(IRequestContext data) {
        return data.getHttpVerb_Res().startsWith("POST /tradings/");
    }

    @Override
    public IResponseContext exec(IRequestContext data) {
        String tradeId = data.getHttpVerb_Res().substring("POST /tradings/".length(), data.getHttpVerb_Res().indexOf(" HTTP/"));
        ResponseContext responseContext = new ResponseContext();
        if (userRepository.UserLoggedIn(data.getHeaders().get("Authorization"))) {
            User user = userRepository.getUserWithToken(data.getHeaders().get("Authorization"));

            ITrade trade = tradeRepository.findEntity(tradeId);

            ACard card =cardRepository.findEntity(data.getPayload().replace("\"",""));
            if(card!=null) {
                if(user.accectTradeOffer(trade, card)) {
                    userRepository.updateEntity(user);
                    userRepository.updateEntity(trade.getUserOffer());
                    tradeRepository.deleteEntity(trade.getId());
                    responseContext.setHttpStatusCode("HTTP/1.1 200");
                }
                else
                {
                    responseContext.setPayload("Trade failed");
                    responseContext.setHttpStatusCode("HTTP/1.1 400");
                }

            }
            else
            {
                responseContext.setPayload("Not existing CardId");
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