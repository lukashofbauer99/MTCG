package Service.RESTServer.Service.Methods.POST;

import Domain.User.Interfaces.IUserRepository;
import Domain.PlayerHub;
import Model.Battle.Battle;
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

public class POST_battles implements IHTTPMethod {

    IUserRepository userRepository;
    PlayerHub playerHub;
    ObjectMapper mapper = new ObjectMapper();

    public POST_battles(IUserRepository userRepository,PlayerHub playerHub) {
        this.playerHub = playerHub;
        this.userRepository = userRepository;
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }

    @Override
    public Boolean analyse(IRequestContext data) {
        return data.getHttpVerb_Res().startsWith("POST /battles ");
    }

    @Override
    public IResponseContext exec(IRequestContext data) throws JsonProcessingException {
        ResponseContext responseContext = new ResponseContext();
        if (userRepository.UserLoggedIn(data.getHeaders().get("Authorization"))) {
            User user = userRepository.getUserWithToken(data.getHeaders().get("Authorization"));
            responseContext.setPayload(mapper.writerFor(new TypeReference<Battle>() {})
                    .with(new DefaultPrettyPrinter())
                    .writeValueAsString(user.searchBattle(playerHub)));
            userRepository.updateEntity(user);
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