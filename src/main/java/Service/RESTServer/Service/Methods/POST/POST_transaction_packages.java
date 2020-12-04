package Service.RESTServer.Service.Methods.POST;

import Service.RESTServer.Service.Methods.IHTTPMethod;
import Service.RESTServer.Service.Request.IRequestContext;
import Service.RESTServer.Service.Response.IResponseContext;
import Service.RESTServer.Service.Response.ResponseContext;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class POST_transaction_packages implements IHTTPMethod {


    @Override
    public Boolean analyse(IRequestContext data) {
        return data.getHttpVerb_Res().startsWith("POST /transactions/packages ");
    }

    @Override
    public IResponseContext exec(IRequestContext data) {
        ResponseContext responseContext = new ResponseContext();

        return responseContext;
    }
}