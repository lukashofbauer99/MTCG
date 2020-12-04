package Service.RESTServer.Service.Methods.GET;

import Service.RESTServer.Service.Methods.IHTTPMethod;
import Service.RESTServer.Service.Request.IRequestContext;
import Service.RESTServer.Service.Response.IResponseContext;
import Service.RESTServer.Service.Response.ResponseContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GET_stats implements IHTTPMethod {

    ObjectMapper mapper = new ObjectMapper();

    @Override
    public Boolean analyse(IRequestContext data) {
        return data.getHttpVerb_Res().startsWith("GET /stats ");
    }

    @Override
    public IResponseContext exec(IRequestContext data) {
        ResponseContext responseContext = new ResponseContext();


        return responseContext;
    }
}
