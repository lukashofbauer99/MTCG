package Service.RESTServer.Service.Methods.PUT;

import Service.RESTServer.Domain.IRepository;
import Service.RESTServer.Domain.MessageRepository;
import Service.RESTServer.Model.Message;
import Service.RESTServer.Service.Methods.IHTTPMethod;
import Service.RESTServer.Service.Request.IRequestContext;
import Service.RESTServer.Service.Response.IResponseContext;
import Service.RESTServer.Service.Response.ResponseContext;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import static java.lang.Integer.parseInt;

@AllArgsConstructor
@NoArgsConstructor
public class PUT_messages_Id implements IHTTPMethod {

    IRepository<Message> repository = new MessageRepository();

    @Override
    public Boolean analyse(IRequestContext data) {
        return data.getHttpVerb_Res().startsWith("PUT /messages/");
    }

    @Override
    public IResponseContext exec(IRequestContext data) {
        ResponseContext responseContext = new ResponseContext();

        Message messageToChange = repository.findEntity( parseInt(data.getHttpVerb_Res().substring("PUT /messages/".length(),data.getHttpVerb_Res().indexOf(" HTTP/"))));
        if(messageToChange!= null)
        {
            messageToChange.setContent(data.getPayload());
            repository.persistEntity(messageToChange);
            responseContext.setHttpStatusCode("HTTP/1.1 200");
        }
        else
            responseContext.setHttpStatusCode("HTTP/1.1 400");


        responseContext.getHeaders().put("Connection", "close");
        responseContext.getHeaders().put("Content-Length", "0");
        responseContext.getHeaders().put("Content-Type", "text/plain");
        return responseContext;
    }
}