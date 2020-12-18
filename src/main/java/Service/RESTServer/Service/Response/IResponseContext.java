package Service.RESTServer.Service.Response;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

public interface IResponseContext {

    void setHttpStatusCode(String httpStatusCode);

    Map<String, String> getHeaders();

    void setPayload(String payload);

    String getPayload();

    void sendResponse(BufferedWriter writer) throws IOException;

}
