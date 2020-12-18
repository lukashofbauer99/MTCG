package Model.User;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EditableUserData {
    String name = "";
    String image = "";
    String bio = "";
}
