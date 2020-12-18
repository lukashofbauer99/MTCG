package Service.RESTServer.Service.Methods.POST;

import Domain.User.Interfaces.IUserRepository;
import Model.User.Credentials;
import Model.User.User;
import Service.RESTServer.Service.Methods.IHTTPMethod;
import Service.RESTServer.Service.Request.IRequestContext;
import Service.RESTServer.Service.Response.IResponseContext;
import Service.RESTServer.Service.Response.ResponseContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class POST_users implements IHTTPMethod {

    IUserRepository userRepository;
    ObjectMapper mapper = new ObjectMapper();

    public POST_users(IUserRepository userRepository) {
        this.userRepository = userRepository;
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }

    @Override
    public Boolean analyse(IRequestContext data) {
        return data.getHttpVerb_Res().startsWith("POST /users ");
    }

    @Override
    public IResponseContext exec(IRequestContext data) {
        ResponseContext responseContext = new ResponseContext();
        Credentials cred = null;
        try {
            cred = mapper.readValue(data.getPayload(), Credentials.class);
        } catch (JsonProcessingException e) {
            responseContext.setPayload("Invalid form of Data");
        }
        User user = new User(cred);
        user.getEditableUserData().setName(cred.getUsername());
        Long id =userRepository.persistEntity(new User(cred));
        if (id!=null) {
            responseContext.setPayload(String.valueOf(id));
            responseContext.setHttpStatusCode("HTTP/1.1 201");
        }
        else {
            responseContext.setPayload("User already Exists");
            responseContext.setHttpStatusCode("HTTP/1.1 409");
        }


        responseContext.getHeaders().put("Connection", "close");
        responseContext.getHeaders().put("Content-Length", String.valueOf(responseContext.getPayload().length()));
        responseContext.getHeaders().put("Content-Type", "text/plain");
        return responseContext;
    }
}