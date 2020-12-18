package UnitTests.Service.RESTServer.Service.Methods;

import Service.RESTServer.Domain.IRepository;
import Service.RESTServer.Model.Message;
import Service.RESTServer.Service.Methods.DELETE.DELETE_messages_Id;
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
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TestDELETE_message_id {

    @Mock
    IRepository<Message> repository;

    DELETE_messages_Id delete_message_id;
    IRequestContext requestContext;

    @Test
    @DisplayName("Test DELETE_message_id-analyze true")
    void testAnalyzeTrue() {
        // arrange
        delete_message_id = new DELETE_messages_Id(repository);
        requestContext = new RequestContext("DELETE /messages/1 HTTP/1.1", new HashMap<>(), "");
        boolean check;

        // act
        check = delete_message_id.analyse(requestContext);

        // assert
        assertTrue(check);

    }

    @Test
    @DisplayName("Test DELETE_message_id-analyze false")
    void testAnalyzeFalse() {
        // arrange
        delete_message_id = new DELETE_messages_Id(repository);
        requestContext = new RequestContext("DELETE /messages HTTP/1.1", new HashMap<>(), "");
        boolean check;

        // act
        check = delete_message_id.analyse(requestContext);

        // assert
        assertFalse(check);
    }

    @Test
    @DisplayName("Test DELETE_message_id-exec")
    void testExec() {
        // arrange
        delete_message_id = new DELETE_messages_Id(repository);
        requestContext = new RequestContext("DELETE /messages/1 HTTP/1.1", new HashMap<>(), "");

        // act
        delete_message_id.exec(requestContext);
        // assert
        verify(repository).deleteEntity(1);
    }


}
