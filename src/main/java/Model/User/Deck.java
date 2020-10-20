package Model.User;

import Model.Cards.ACard;
import jdk.jshell.spi.ExecutionControl;
import lombok.Data;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Data
public class Deck {

    List<ACard> cards = new ArrayList<>();

    public void drawCard() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("TODO");
    }
}
