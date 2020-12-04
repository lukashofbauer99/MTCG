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
public class GET_deck implements IHTTPMethod {

    ObjectMapper mapper = new ObjectMapper();

    @Override
    public Boolean analyse(IRequestContext data) {
        return data.getHttpVerb_Res().startsWith("GET /deck ");
    }

    @Override
    public IResponseContext exec(IRequestContext data) {
        ResponseContext responseContext = new ResponseContext();


        return responseContext;
    }
}
