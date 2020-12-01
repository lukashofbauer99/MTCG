package Service.RESTServer.Service.Response;

import lombok.Getter;
import lombok.Setter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ResponseContext implements IResponseContext {

    String httpStatusCode;

    Map<String,String> headers= new HashMap<>();

    String payload = "";

    //Send Response over the BufferedWriter
    public void sendResponse(BufferedWriter writer) throws IOException {
        System.out.println(httpStatusCode);
        writer.write(httpStatusCode);
        writer.newLine();

        //add Headers line for line
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            System.out.println(entry.getKey()+": "+entry.getValue());
            writer.write(entry.getKey()+": "+entry.getValue());
            writer.newLine();
        }
        writer.write("\r\n");
        System.out.println(payload);
        writer.write(payload);
        writer.flush();
    }
}
