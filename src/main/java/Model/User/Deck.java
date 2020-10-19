package Model.User;

import Model.Cards.ACard;
import jdk.jshell.spi.ExecutionControl;
import lombok.Data;

import java.lang.reflect.Array;

@Data
public class Deck {

    ACard[] cards = new ACard[4];

    public void drawCard() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("TODO");
    }
}
