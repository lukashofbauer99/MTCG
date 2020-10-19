package Model.Battle;

import Model.User.User;
import jdk.jshell.spi.ExecutionControl;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Battle {

    User user1;
    User user2;

    List<Round> rounds = new ArrayList<>();

    public void Start() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("TODO");
    }

}
