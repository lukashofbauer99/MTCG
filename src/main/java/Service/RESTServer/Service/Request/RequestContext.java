package Service.RESTServer.Service.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public class RequestContext implements IRequestContext {

    String httpVerb_Res;

    Map<String,String> headers= new HashMap<>();

    String payload="";


    public RequestContext(BufferedReader reader) {
        int contentLength;
        try {
            contentLength = readHttpHeader( reader );
        payload = readHttpBody( reader, contentLength );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private int readHttpHeader(BufferedReader reader) throws IOException {
        String line;
        int contentLength = 0;

        if((line = reader.readLine())!=null)
        {
            httpVerb_Res=line;
        }

        //read line after line and fill into Map
        while ( (line=reader.readLine())!=null ) {
            if (line.isBlank() )
                break;
            headers.put(line.substring(0, line.indexOf(":")), line.substring(line.indexOf(":") + 2).trim());
            if (line.toLowerCase().startsWith("content-length:") ) {
                contentLength = Integer.parseInt(line.substring(15).trim());
            }
        }
        return contentLength;
    }

    private String readHttpBody(BufferedReader reader, int contentLength) throws IOException {
        if(contentLength==0)
        {
            return "";
        }
        StringBuilder sb = new StringBuilder(10000);
        char[] buf = new char[1024];
        int totalLen = 0;
        int len;
        while ((len = reader.read(buf)) != -1) {
            sb.append(buf, 0, len);
            totalLen += len;
            if( totalLen >= contentLength )
                break;
        }

        return sb.toString();
    }

    public String formatedString()
    {
        StringBuilder returnString= new StringBuilder("REQUEST:\n");
        returnString.append(httpVerb_Res).append("\n\n");
        returnString.append("HEADERS:\n");
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            returnString.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        returnString.append("\n");
        returnString.append("PAYLOAD:\n");
        returnString.append(payload);
        return returnString.toString();
    }




}
