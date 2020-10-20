package Model.Battle;

import Model.Cards.ACard;
import jdk.jshell.spi.ExecutionControl;
import lombok.Data;
import lombok.Getter;

@Getter
public class Round {

    int WinnerDmg;
    int LooserDmg;

    ACard Winner;
    ACard Looser;

    public void fight(ACard attackingCard,ACard defendingCard)
    {
    }
}
