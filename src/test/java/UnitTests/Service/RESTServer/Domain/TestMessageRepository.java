package UnitTests.Service.RESTServer.Domain;

import Service.RESTServer.Domain.IRepository;
import Service.RESTServer.Domain.MessageRepository;
import Service.RESTServer.Model.Message;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestMessageRepository {
    String idPath = "src/test/java/UnitTests/Service/RESTServer/Domain/id.txt";
    String messagePath = "src/test/java/UnitTests/Service/RESTServer/Domain/TestMessages/";
    IRepository<Message> repository = new MessageRepository(messagePath, idPath);

    @AfterEach
    void CleanUp() {
        try {
            Files.writeString(Path.of(idPath), String.valueOf(0));
            for (File file : new File(messagePath).listFiles())
                if (!file.isDirectory())
                    file.delete();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Test persistEntity")
    void testPersistEntity() {
        // arrange
        Message message = new Message("message");
        int messageID;
        // act
        messageID = repository.persistEntity(message);
        File f = new File(messagePath + messageID + ".json");

        // assert
        assertTrue(f.exists());
    }

    @Test
    @DisplayName("Test findEntity")
    void testFindEntity() throws IOException {
        // arrange
        Message message = new Message(1, "message");
        Files.writeString(Path.of(messagePath + "1.json"), "{\"id\":\"1\",\"content\":\"message\"}");
        // act
        Message foundMessage = repository.findEntity(1);
        File f = new File(messagePath + "1.json");

        // assert
        assertEquals(message, foundMessage);
    }

    @Test
    @DisplayName("Test deleteEntity")
    void testDeleteEntity() throws IOException {
        // arrange
        Files.writeString(Path.of(messagePath + "1.json"), "{\"id\":\"1\",\"content\":\"message\"}");
        // act
        repository.deleteEntity(1);
        File f = new File(messagePath + "1.json");

        // assert
        assertFalse(f.exists());
    }

    @Test
    @DisplayName("Test getAllEntity")
    void testGetAllEntity() throws IOException {
        // arrange
        List<Message> messages = new ArrayList<>();

        messages.add(new Message(2, "message2"));
        messages.add(new Message(1, "message"));
        Files.writeString(Path.of(messagePath + "1.json"), "{\"id\":\"1\",\"content\":\"message\"}");
        Files.writeString(Path.of(messagePath + "2.json"), "{\"id\":\"2\",\"content\":\"message2\"}");
        // act
        List<Message> foundMessages = repository.getAllEntities();

        // assert
        assertEquals(messages, foundMessages);
    }

    @Test
    @DisplayName("Test getNextID")
    void testgetNextID() throws IOException {
        // arrange
        int messageID;
        int incremetedMessageID;
        // act
        messageID = Integer.parseInt(Files.readString(Path.of(idPath)));
        incremetedMessageID = repository.getNextID();

        // assert
        assertEquals(messageID + 1, incremetedMessageID);
    }

}
