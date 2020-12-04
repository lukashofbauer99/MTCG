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
public class PUT_deck implements IHTTPMethod {

    @Override
    public Boolean analyse(IRequestContext data) {
        return data.getHttpVerb_Res().startsWith("PUT /deck");
    }

    @Override
    public IResponseContext exec(IRequestContext data) {
        ResponseContext responseContext = new ResponseContext();

        return responseContext;
    }
}