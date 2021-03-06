package Service.RESTServer.Service.Methods.GET;

import Domain.User.Interfaces.ITradeRepository;
import Domain.User.Interfaces.IUserRepository;
import Model.Cards.ACard;
import Model.User.Trade.ITrade;
import Service.RESTServer.Service.Methods.IHTTPMethod;
import Service.RESTServer.Service.Request.IRequestContext;
import Service.RESTServer.Service.Response.IResponseContext;
import Service.RESTServer.Service.Response.ResponseContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;

import java.util.List;

//TODO Test curl
@NoArgsConstructor
public class GET_tradings implements IHTTPMethod {

    ObjectMapper mapper = new ObjectMapper();
    IUserRepository userRepository;
    ITradeRepository tradeRepository;

    public GET_tradings(IUserRepository userRepository,ITradeRepository tradeRepository) {
        this.userRepository = userRepository;
        this.tradeRepository=tradeRepository;
    }

    @Override
    public Boolean analyse(IRequestContext data) {
        return data.getHttpVerb_Res().startsWith("GET /tradings ");
    }

    @Override
    public IResponseContext exec(IRequestContext data) throws JsonProcessingException {
        ResponseContext responseContext = new ResponseContext();
        if (userRepository.UserLoggedIn(data.getHeaders().get("Authorization"))) {
            responseContext.setPayload(
                    mapper.writerFor(new TypeReference<List<ITrade>>() {})
                            .with(new DefaultPrettyPrinter())
                            .writeValueAsString(tradeRepository.getAllEntities()));
            responseContext.setHttpStatusCode("HTTP/1.1 200");
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
