package UnitTests.Service.RESTServer.Service.Methods;

import Service.RESTServer.Domain.IRepository;
import Service.RESTServer.Model.Message;
import Service.RESTServer.Service.Methods.PUT.PUT_messages_Id;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestPUT_message_id {

    @Mock
    IRepository<Message> repository;

    PUT_messages_Id put_message_id;
    IRequestContext requestContext;

    @Test
    @DisplayName("Test PUT_message_id-analyze true")
    void testAnalyzeTrue() {
        // arrange
        put_message_id = new PUT_messages_Id(repository);
        requestContext = new RequestContext("PUT /messages/1 HTTP/1.1", new HashMap<>(), "");
        boolean check;

        // act
        check = put_message_id.analyse(requestContext);

        // assert
        assertTrue(check);

    }

    @Test
    @DisplayName("Test PUT_message_id-analyze false")
    void testAnalyzeFalse() {
        // arrange
        put_message_id = new PUT_messages_Id(repository);
        requestContext = new RequestContext("PUT /messages HTTP/1.1", new HashMap<>(), "");
        boolean check;

        // act
        check = put_message_id.analyse(requestContext);

        // assert
        assertFalse(check);
    }

    @Test
    @DisplayName("Test PUT_message_id-exec")
    void testExec() {
        // arrange
        put_message_id = new PUT_messages_Id(repository);

        requestContext = new RequestContext("PUT /messages/1 HTTP/1.1", new HashMap<>(), "");

        when(repository.findEntity(1)).thenReturn(new Message());

        // act
        put_message_id.exec(requestContext);
        // assert
        verify(repository).findEntity(1);
        verify(repository).persistEntity(any(Message.class));
    }


}
