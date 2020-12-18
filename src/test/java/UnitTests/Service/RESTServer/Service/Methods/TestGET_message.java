package UnitTests.Service.RESTServer.Service.Methods;

import Service.RESTServer.Domain.IRepository;
import Service.RESTServer.Model.Message;
import Service.RESTServer.Service.Methods.GET.GET_messages;
import Service.RESTServer.Service.Request.IRequestContext;
import Service.RESTServer.Service.Request.RequestContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestGET_message {

    @Mock
    IRepository<Message> repository;

    GET_messages get_messages;
    IRequestContext requestContext;

    @Test
    @DisplayName("Test GET_Messages-analyze true")
    void testAnalyzeTrue() {
        // arrange
        get_messages = new GET_messages(repository);
        requestContext = new RequestContext("GET /messages HTTP/1.1", new HashMap<>(), "");
        boolean check;

        // act
        check = get_messages.analyse(requestContext);

        // assert
        assertTrue(check);

    }

    @Test
    @DisplayName("Test GET_Messages-analyze false")
    void testAnalyzeFalse() {
        // arrange
        get_messages = new GET_messages(repository);
        requestContext = new RequestContext("GET /messages/1 HTTP/1.1", new HashMap<>(), "");
        boolean check;

        // act
        check = get_messages.analyse(requestContext);

        // assert
        assertFalse(check);
    }

    @Test
    @DisplayName("Test GET_Messages-exec")
    void testExec() {
        // arrange
        get_messages = new GET_messages(repository);

        when(repository.getAllEntities()).thenReturn(new ArrayList<>());
        // act
        get_messages.exec(requestContext);
        // assert
        verify(repository).getAllEntities();
    }


}
