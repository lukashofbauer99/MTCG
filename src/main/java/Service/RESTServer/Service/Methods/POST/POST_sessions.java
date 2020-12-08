package Service.RESTServer.Service.Methods.POST;

import Domain.User.Interfaces.IUserRepository;
import Model.User.Credentials;
import Service.RESTServer.Service.Methods.IHTTPMethod;
import Service.RESTServer.Service.Request.IRequestContext;
import Service.RESTServer.Service.Response.IResponseContext;
import Service.RESTServer.Service.Response.ResponseContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class POST_sessions implements IHTTPMethod {

    public POST_sessions(IUserRepository userRepository) {
        this.userRepository = userRepository;
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }

    IUserRepository userRepository;
    ObjectMapper mapper = new ObjectMapper();

    @Override
    public Boolean analyse(IRequestContext data) {
        return data.getHttpVerb_Res().startsWith("POST /sessions ");
    }

    @Override
    public IResponseContext exec(IRequestContext data) throws JsonProcessingException {
        ResponseContext responseContext = new ResponseContext();
        Credentials cred = mapper.readValue(data.getPayload(), Credentials.class);
        String sessionToken =userRepository.loginUser(cred);
        if(sessionToken!=null) {
            responseContext.setPayload(sessionToken);
            responseContext.setHttpStatusCode("HTTP/1.1 200");
        }
        else
            responseContext.setHttpStatusCode("HTTP/1.1 401");

        responseContext.getHeaders().put("Connection", "close");
        responseContext.getHeaders().put("Content-Length", String.valueOf(String.valueOf(sessionToken).length()));
        responseContext.getHeaders().put("Content-Type", "text/plain");
        return responseContext;
    }
}