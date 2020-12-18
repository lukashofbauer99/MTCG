package Service.RESTServer.Service.Methods.GET;

import Service.RESTServer.Domain.IRepository;
import Service.RESTServer.Domain.MessageRepository;
import Service.RESTServer.Model.Message;
import Service.RESTServer.Service.Methods.IHTTPMethod;
import Service.RESTServer.Service.Request.IRequestContext;
import Service.RESTServer.Service.Response.IResponseContext;
import Service.RESTServer.Service.Response.ResponseContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class GET_messages implements IHTTPMethod {

    ObjectMapper mapper = new ObjectMapper();
    IRepository<Message> repository = new MessageRepository();

    public GET_messages(IRepository<Message> repository) {
        this.repository = repository;
    }

    @Override
    public Boolean analyse(IRequestContext data) {
        return data.getHttpVerb_Res().startsWith("GET /messages ");
    }

    @Override
    public IResponseContext exec(IRequestContext data) {
        ResponseContext responseContext = new ResponseContext();


        String messagesString = "";

        List<Message> messages = new ArrayList<>(repository.getAllEntities());

        try {
            messagesString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(messages);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        responseContext.setPayload(messagesString);
        responseContext.setHttpStatusCode("HTTP/1.1 200");
        responseContext.getHeaders().put("Content-Length", String.valueOf(messagesString.length()));
        responseContext.getHeaders().put("Content-Type", "text/plain");
        responseContext.getHeaders().put("Connection", "close");


        return responseContext;
    }
}
