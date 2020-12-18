package Service.RESTServer.Domain;


import Service.RESTServer.Model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class MessageRepository implements IRepository<Message> {

    String messagePath = "Messages/";
    String idPath = "id.txt";

    ObjectMapper mapper = new ObjectMapper();
    int currentID = 0;

    public MessageRepository(String messagePath, String idPath) {
        this.messagePath = messagePath;
        this.idPath = idPath;
    }

    public int persistEntity(Message entitiy) {
        try {
            File f = new File(messagePath + entitiy.getId() + ".json");
            if (!f.exists()) {
                entitiy.setId(getNextID());
            }
            //write JSON value to file
            Files.writeString(Path.of(messagePath + entitiy.getId() + ".json"), mapper.writerWithDefaultPrettyPrinter().writeValueAsString(entitiy));
        } catch (IOException e) {
            System.out.println("An error occurred while saving the message");
            e.printStackTrace();
        }
        return entitiy.getId();
    }

    public Message findEntity(int id) {
        try {
            //read JSON value from file
            return mapper.readValue(Files.readString(Path.of(messagePath + id + ".json")), Message.class);
        } catch (NoSuchFileException e) {
            return null;
        } catch (IOException e) {
            System.out.println("An error occurred while reading the message");
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteEntity(int id) {
        try {
            Files.deleteIfExists(Paths.get(messagePath + id + ".json"));
        } catch (NoSuchFileException e) {
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public List<Message> getAllEntities() {
        List<Message> messages = new ArrayList<>();
        File directoryPath = new File(messagePath);

        //List of all files
        String[] contents = directoryPath.list();

        if (contents != null) {
            for (String content : contents) {
                try {
                    messages.add(mapper.readValue(Files.readString(Path.of(messagePath + content)), Message.class));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return messages;
    }

    public synchronized int getNextID() {
        try {
            currentID = Integer.parseInt(Files.readString(Path.of(idPath)));
            currentID++;
            Files.writeString(Path.of(idPath), String.valueOf(currentID));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return currentID;
    }

}
