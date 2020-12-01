package Service.RESTServer.Service.Methods.Error;

import Service.RESTServer.Service.Methods.IHTTPMethod;
import Service.RESTServer.Service.Request.IRequestContext;
import Service.RESTServer.Service.Response.IResponseContext;
import Service.RESTServer.Service.Response.ResponseContext;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NotFound implements IHTTPMethod {

    @Override
    public Boolean analyse(IRequestContext data) {
        return true;
    }

    @Override
    public IResponseContext exec(IRequestContext data) {
        ResponseContext responseContext = new ResponseContext();
        responseContext.setPayload("\r\n");
        responseContext.setHttpStatusCode("HTTP/1.1 404");

        responseContext.getHeaders().put("Connection", "close");
        responseContext.getHeaders().put("Content-Length", "0");
        responseContext.getHeaders().put("Content-Type", "text/plain");


        return responseContext;
    }
}
