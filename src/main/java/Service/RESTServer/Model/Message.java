package Service.RESTServer.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    public Message(String content) {
        this.content = content;
    }

    int id;
    String content;
}
