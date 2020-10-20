package Model.User;

import Model.Cards.ACard;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Stack {

    List<ACard> cards = new ArrayList<>();

}
