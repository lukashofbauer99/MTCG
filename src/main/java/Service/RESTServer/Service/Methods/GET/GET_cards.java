package Service.RESTServer.Service.Methods.GET;

import Domain.User.Interfaces.IUserRepository;
import Model.Cards.ACard;
import Service.RESTServer.Service.Methods.IHTTPMethod;
import Service.RESTServer.Service.Request.IRequestContext;
import Service.RESTServer.Service.Response.IResponseContext;
import Service.RESTServer.Service.Response.ResponseContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class GET_cards implements IHTTPMethod {

    IUserRepository userRepository;

    public GET_cards(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }


    ObjectMapper mapper = new ObjectMapper();

    @Override
    public Boolean analyse(IRequestContext data) {
        return data.getHttpVerb_Res().startsWith("GET /cards ");
    }

    @Override
    public IResponseContext exec(IRequestContext data) throws JsonProcessingException {
        ResponseContext responseContext = new ResponseContext();

        if (userRepository.UserLoggedIn(data.getHeaders().get("Authorization"))) {
            responseContext.setPayload(mapper.writerFor(new TypeReference<List<ACard>>() {
            })
                    .writeValueAsString(userRepository.getCardsOfUserWithToken(data.getHeaders().get("Authorization"))));
        } else {
            responseContext.setHttpStatusCode("HTTP/1.1 401");
            responseContext.setPayload("Not logged In");
        }

        responseContext.setHttpStatusCode("HTTP/1.1 200");
        responseContext.getHeaders().put("Connection", "close");
        responseContext.getHeaders().put("Content-Length", String.valueOf(responseContext.getPayload().length()));
        responseContext.getHeaders().put("Content-Type", "application/json");
        return responseContext;
    }
}
