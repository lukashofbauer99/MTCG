package Model.Battle;

import Model.Cards.ACard;
import jdk.jshell.spi.ExecutionControl;
import lombok.Data;
import lombok.Getter;

@Getter
public class Round {
    ACard card1;
    ACard card2;

    public void fight() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("TODO");
    }
}
