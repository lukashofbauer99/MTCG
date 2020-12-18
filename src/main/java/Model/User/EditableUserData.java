package Model.User;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EditableUserData {
    String name="";
    String image="";
    String bio="";
}
