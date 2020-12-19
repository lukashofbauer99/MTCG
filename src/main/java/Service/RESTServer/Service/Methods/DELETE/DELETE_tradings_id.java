package Service.RESTServer.Service.Methods.DELETE;

import Domain.Cards.Interfaces.IACardRepository;
import Domain.User.Interfaces.ITradeRepository;
import Domain.User.Interfaces.IUserRepository;
import Model.Cards.ACard;
import Model.User.Trade.ITrade;
import Model.User.User;
import Service.RESTServer.Service.Methods.IHTTPMethod;
import Service.RESTServer.Service.Request.IRequestContext;
import Service.RESTServer.Service.Response.IResponseContext;
import Service.RESTServer.Service.Response.ResponseContext;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DELETE_tradings_id implements IHTTPMethod {
    ObjectMapper mapper = new ObjectMapper();
    IUserRepository userRepository;
    ITradeRepository tradeRepository;

    public DELETE_tradings_id(IUserRepository userRepository, ITradeRepository tradeRepository) {
        this.userRepository = userRepository;
        this.tradeRepository=tradeRepository;
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

    }
    @Override
    public Boolean analyse(IRequestContext data) {
        return data.getHttpVerb_Res().startsWith("DELETE /tradings/");
    }

    @Override
    public IResponseContext exec(IRequestContext data) {
        String tradeId = data.getHttpVerb_Res().substring("DELETE /tradings/".length(), data.getHttpVerb_Res().indexOf(" HTTP/"));
        ResponseContext responseContext = new ResponseContext();
        if (userRepository.UserLoggedIn(data.getHeaders().get("Authorization"))) {
            User user = userRepository.getUserWithToken(data.getHeaders().get("Authorization"));

            ITrade trade = tradeRepository.findEntity(tradeId);
            if (trade != null) {
                if (trade.getUserOffer() == user) {
                    tradeRepository.deleteEntity(tradeId);
                    responseContext.setHttpStatusCode("HTTP/1.1 204");
                    responseContext.setPayload("Deleted");
                } else {
                    responseContext.setHttpStatusCode("HTTP/1.1 401");
                    responseContext.setPayload("No Permission");
                }
            } else {
                responseContext.setHttpStatusCode("HTTP/1.1 404");
                responseContext.setPayload("No Trade with this ID");
            }
        }
        else {
            responseContext.setHttpStatusCode("HTTP/1.1 401");
            responseContext.setPayload("Not logged In");
        }


        responseContext.getHeaders().put("Connection", "close");
        responseContext.getHeaders().put("Content-Length", String.valueOf(responseContext.getPayload().length()));
        responseContext.getHeaders().put("Content-Type", "application/json");

        return responseContext;
    }
}
