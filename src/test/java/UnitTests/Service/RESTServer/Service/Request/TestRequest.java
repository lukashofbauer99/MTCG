package UnitTests.Service.RESTServer.Service.Request;

import Service.RESTServer.Service.Request.IRequestContext;
import Service.RESTServer.Service.Request.RequestContext;
import Service.RESTServer.Service.Socket.IMySocket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestRequest {

    private IMySocket socket;

    IRequestContext requestContext;

    @BeforeEach
    void setUp() throws IOException {
        socket = mock(IMySocket.class);
        ByteArrayInputStream readStream;
        readStream = new ByteArrayInputStream("""
                GET /messages HTTP/1.1
                Content-Type: text/plain
                Content-Length: 7

                message""".getBytes(StandardCharsets.UTF_8) );
        var writeStream = new ByteArrayOutputStream();

        // mock the TCP client methods
        when(socket.getInputStream()).thenReturn(readStream);
        when(socket.getOutputStream()).thenReturn(writeStream);

    }

    @Test
    @DisplayName("Test httpVerb_Res-input")
    void testHttpVerb_Res() throws IOException {
        // arrange
        // act
        requestContext=new RequestContext(new BufferedReader(new InputStreamReader(socket.getInputStream())));

        // assert
        assertEquals("GET /messages HTTP/1.1",requestContext.getHttpVerb_Res());
    }

    @Test
    @DisplayName("Test headers-input")
    void testHeaders() throws IOException {
        // arrange
        // act
        requestContext=new RequestContext(new BufferedReader(new InputStreamReader(socket.getInputStream())));

        Map<String,String> map = new HashMap<>();
        map.put("Content-Type","text/plain");
        map.put("Content-Length","7");

        // assert
        assertEquals(map,requestContext.getHeaders());
    }

    @Test
    @DisplayName("Test payload-input")
    void testPayload() throws IOException {
        // arrange
        // act
        requestContext=new RequestContext(new BufferedReader(new InputStreamReader(socket.getInputStream())));

        // assert
        assertEquals("message",requestContext.getPayload());
    }
}

