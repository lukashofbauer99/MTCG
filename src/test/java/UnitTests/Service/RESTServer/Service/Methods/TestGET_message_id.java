package UnitTests.Service.RESTServer.Service.Methods;

import Service.RESTServer.Domain.IRepository;
import Service.RESTServer.Model.Message;
import Service.RESTServer.Service.Methods.GET.GET_messages_Id;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestGET_message_id {


    @Mock
    IRepository<Message> repository;

    GET_messages_Id get_message_id;
    IRequestContext requestContext;

    @Test
    @DisplayName("Test GET_Message_id-analyze true")
    void testAnalyzeTrue() {
        // arrange
        get_message_id= new GET_messages_Id(repository);
        requestContext=new RequestContext("GET /messages/1 HTTP/1.1",new HashMap<>(),"");
        boolean check;

        // act
        check= get_message_id.analyse(requestContext);

        // assert
        assertTrue(check);

    }

    @Test
    @DisplayName("Test GET_Message_id-analyze false")
    void testAnalyzeFalse() {
        // arrange
        get_message_id= new GET_messages_Id(repository);
        requestContext=new RequestContext("GET /messages HTTP/1.1",new HashMap<>(),"");
        boolean check;

        // act
        check= get_message_id.analyse(requestContext);

        // assert
        assertFalse(check);
    }

    @Test
    @DisplayName("Test GET_Message_id-exec")
    void testExec() {
        // arrange
        get_message_id = new GET_messages_Id(repository);

        requestContext=new RequestContext("GET /messages/1 HTTP/1.1",new HashMap<>(),"");

        when(repository.findEntity(1)).thenReturn(new Message());
        // act
        get_message_id.exec(requestContext);
        // assert
        verify(repository).findEntity(1);
    }


}
