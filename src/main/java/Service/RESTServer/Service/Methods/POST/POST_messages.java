package Service.RESTServer.Service.Methods.POST;

import Service.RESTServer.Domain.IRepository;
import Service.RESTServer.Domain.MessageRepository;
import Service.RESTServer.Model.Message;
import Service.RESTServer.Service.Methods.IHTTPMethod;
import Service.RESTServer.Service.Request.IRequestContext;
import Service.RESTServer.Service.Response.IResponseContext;
import Service.RESTServer.Service.Response.ResponseContext;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class POST_messages implements IHTTPMethod {

    IRepository<Message> repository = new MessageRepository();


    @Override
    public Boolean analyse(IRequestContext data) {
        return data.getHttpVerb_Res().startsWith("POST /messages ");
    }

    @Override
    public IResponseContext exec(IRequestContext data) {
        ResponseContext responseContext = new ResponseContext();

        int messageID;
        Message message = new Message(data.getPayload());
        messageID=repository.persistEntity(message);
        responseContext.setPayload(String.valueOf(messageID));

        responseContext.setHttpStatusCode("HTTP/1.1 201");
        responseContext.getHeaders().put("Connection", "close");
        responseContext.getHeaders().put("Content-Length", String.valueOf(String.valueOf(messageID).length()));
        responseContext.getHeaders().put("Content-Type", "text/plain");
        return responseContext;
    }
}