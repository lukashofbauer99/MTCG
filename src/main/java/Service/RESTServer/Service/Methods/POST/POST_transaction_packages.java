package Service.RESTServer.Service.Methods.POST;

import Domain.Cards.Interfaces.IVendorRepository;
import Domain.User.Interfaces.IUserRepository;
import Model.Cards.Vendor.IVendor;
import Model.User.User;
import Service.RESTServer.Service.Methods.IHTTPMethod;
import Service.RESTServer.Service.Request.IRequestContext;
import Service.RESTServer.Service.Response.IResponseContext;
import Service.RESTServer.Service.Response.ResponseContext;

public class POST_transaction_packages implements IHTTPMethod {

    IUserRepository userRepository;
    IVendorRepository vendorRepository;

    public POST_transaction_packages(IUserRepository userRepository, IVendorRepository vendorRepository) {
        this.userRepository = userRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public Boolean analyse(IRequestContext data) {
        return data.getHttpVerb_Res().startsWith("POST /transactions/packages ");
    }

    @Override
    public IResponseContext exec(IRequestContext data) {
        ResponseContext responseContext = new ResponseContext();
        responseContext.setHttpStatusCode("HTTP/1.1 402");

        User user = userRepository.getUserWithToken(data.getHeaders().get("Authorization"));
        if (user != null) {
            IVendor vendor = vendorRepository.getAllEntities().stream().findFirst().orElse(null);
            if (vendor != null) {
                if (user.buyCardPackage(vendor))
                    userRepository.updateEntity(user);
                responseContext.setHttpStatusCode("HTTP/1.1 201");
            }
        } else
            responseContext.setHttpStatusCode("HTTP/1.1 403");


        responseContext.getHeaders().put("Connection", "close");
        responseContext.getHeaders().put("Content-Length", "0");
        responseContext.getHeaders().put("Content-Type", "text/plain");
        return responseContext;
    }
}