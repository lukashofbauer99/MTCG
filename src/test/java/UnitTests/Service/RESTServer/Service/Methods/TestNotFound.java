package UnitTests.Service.RESTServer.Service.Methods;

import Service.RESTServer.Service.Methods.Error.NotFound;
import Service.RESTServer.Service.Request.IRequestContext;
import Service.RESTServer.Service.Request.RequestContext;
import Service.RESTServer.Service.Response.ResponseContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class TestNotFound {

    NotFound notFound;
    IRequestContext requestContext;

    @Test
    @DisplayName("Test NotFound-analyze true")
    void testAnalyzeTrue() {
        // arrange
        notFound = new NotFound();
        requestContext = new RequestContext("DELETE /stsadagtasfas/1 HTTP/1.1", new HashMap<>(), "");
        boolean check;

        // act
        check = notFound.analyse(requestContext);

        // assert
        assertTrue(check);

    }

    @Test
    @DisplayName("Test NotFound-exec")
    void testExec() {
        // arrange
        notFound = new NotFound();
        requestContext = new RequestContext("DELETE /messages/1 HTTP/1.1", new HashMap<>(), "");

        // act
        ResponseContext response = (ResponseContext) notFound.exec(requestContext);
        // assert

        assertEquals("HTTP/1.1 404", response.getHttpStatusCode());

    }


}
