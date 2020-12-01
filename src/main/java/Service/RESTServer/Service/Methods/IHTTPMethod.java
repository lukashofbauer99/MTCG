package Service.RESTServer.Service.Methods;


import Service.RESTServer.Service.Request.IRequestContext;
import Service.RESTServer.Service.Response.IResponseContext;

import java.io.IOException;

public interface IHTTPMethod {

    Boolean analyse(IRequestContext data);

    IResponseContext exec(IRequestContext data) throws IOException;
}
