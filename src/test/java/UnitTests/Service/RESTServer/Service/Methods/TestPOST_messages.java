package UnitTests.Service.RESTServer.Service.Methods;

import Service.RESTServer.Domain.IRepository;
import Service.RESTServer.Model.Message;
import Service.RESTServer.Service.Methods.POST.POST_messages;
import Service.RESTServer.Service.Request.IRequestContext;
import Service.RESTServer.Service.Request.RequestContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TestPOST_messages {


    @Mock
    IRepository<Message> repository;

    POST_messages post_messages;
    IRequestContext requestContext;

    @Test
    @DisplayName("Test POST_Messages-analyze true")
    void testAnalyzeTrue() {
        // arrange
        post_messages = new POST_messages(repository);
        requestContext = new RequestContext("POST /messages HTTP/1.1", new HashMap<>(), "");
        boolean check;

        // act
        check = post_messages.analyse(requestContext);

        // assert
        assertTrue(check);

    }

    @Test
    @DisplayName("Test POST_Messages-analyze false")
    void testAnalyzeFalse() {
        // arrange
        post_messages = new POST_messages(repository);
        requestContext = new RequestContext("POST /messages/1 HTTP/1.1", new HashMap<>(), "");
        boolean check;

        // act
        check = post_messages.analyse(requestContext);

        // assert
        assertFalse(check);
    }

    @Test
    @DisplayName("Test POST_Messages-exec")
    void testExec() {
        // arrange
        post_messages = new POST_messages(repository);

        requestContext = new RequestContext("POST /messages/1 HTTP/1.1", new HashMap<>(), "message");
        // act
        post_messages.exec(requestContext);
        // assert
        verify(repository).persistEntity(any(Message.class));
    }


}
