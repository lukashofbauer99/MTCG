package Service.RESTServer.Service.Methods.PUT;

import Domain.User.Interfaces.IUserRepository;
import Model.User.EditableUserData;
import Model.User.User;
import Service.RESTServer.Service.Methods.IHTTPMethod;
import Service.RESTServer.Service.Request.IRequestContext;
import Service.RESTServer.Service.Response.IResponseContext;
import Service.RESTServer.Service.Response.ResponseContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PUT_users_name implements IHTTPMethod {

    IUserRepository userRepository;

    public PUT_users_name(IUserRepository userRepository) {
        this.userRepository = userRepository;
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }

    ObjectMapper mapper = new ObjectMapper();

    @Override
    public Boolean analyse(IRequestContext data) {
        return data.getHttpVerb_Res().startsWith("PUT /users/");
    }

    @Override
    public IResponseContext exec(IRequestContext data) throws JsonProcessingException {
        ResponseContext responseContext = new ResponseContext();
        String username = data.getHttpVerb_Res().substring("GET /users/".length(), data.getHttpVerb_Res().indexOf(" HTTP/"));

        if (userRepository.UserLoggedIn(data.getHeaders().get("Authorization"))) {
            User user = userRepository.getUserWithToken(data.getHeaders().get("Authorization"));
            if (user.getCredentials().getUsername().equals(username)) {
                user.setEditableUserData(mapper.readValue(data.getPayload(), EditableUserData.class));
                userRepository.updateEntity(user);
                responseContext.setHttpStatusCode("HTTP/1.1 200");
            } else {
                responseContext.setHttpStatusCode("HTTP/1.1 401");
                responseContext.setPayload("Insufficient Permissions");
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