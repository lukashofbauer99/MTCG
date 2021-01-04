package Model.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Credentials {
    @Getter
    String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password;
}
