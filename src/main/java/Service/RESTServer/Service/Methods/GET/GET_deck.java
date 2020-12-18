package Service.RESTServer.Service.Methods.GET;

import Domain.User.Interfaces.IUserRepository;
import Model.User.Deck;
import Service.RESTServer.Service.Methods.IHTTPMethod;
import Service.RESTServer.Service.Request.IRequestContext;
import Service.RESTServer.Service.Response.IResponseContext;
import Service.RESTServer.Service.Response.ResponseContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GET_deck implements IHTTPMethod {

    ObjectMapper mapper = new ObjectMapper();
    IUserRepository userRepository;

    public GET_deck(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Boolean analyse(IRequestContext data) {
        return data.getHttpVerb_Res().startsWith("GET /deck ");
    }

    @Override
    public IResponseContext exec(IRequestContext data) throws JsonProcessingException {
        ResponseContext responseContext = new ResponseContext();

        if (userRepository.UserLoggedIn(data.getHeaders().get("Authorization"))) {
            responseContext.setPayload(
                    mapper.writerFor(new TypeReference<Deck>() {})
                            .with(new DefaultPrettyPrinter())
                            .writeValueAsString(userRepository.getDeckOfUserWithToken(data.getHeaders().get("Authorization"))));
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
