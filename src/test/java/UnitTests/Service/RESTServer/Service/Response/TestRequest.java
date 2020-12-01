package UnitTests.Service.RESTServer.Service.Response;

import Service.RESTServer.Service.Response.IResponseContext;
import Service.RESTServer.Service.Response.ResponseContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestRequest {

    IResponseContext responseContext;

    StringWriter sw = new StringWriter();
    BufferedWriter stringWriter = new BufferedWriter(sw);

    @Test
    @DisplayName("Test Send")
    void testSend() throws IOException {
        // arrange
        responseContext = new ResponseContext();
        responseContext.setHttpStatusCode("GET /messages HTTP/1.1");

        responseContext.getHeaders().put("Content-Type","text/plain");
        responseContext.getHeaders().put("Content-Length","7");


        responseContext.setPayload("message");
        // act

        responseContext.sendResponse(stringWriter);

        StringBuffer sb = sw.getBuffer();


        // assert
        assertEquals("GET /messages HTTP/1.1\nContent-Length: 7\nContent-Type: text/plain\n\r\nmessage", sb.toString());
        stringWriter.close();
    }

}

