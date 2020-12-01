package Service.RESTServer.Service.Request;

import java.util.Map;

public interface IRequestContext {
    String getHttpVerb_Res();
    Map<String,String> getHeaders();
    String getPayload();
    String formatedString();
}
