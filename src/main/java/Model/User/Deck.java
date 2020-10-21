package Model.User;

import Model.Cards.ACard;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Deck {

    List<ACard> cards = new ArrayList<>();
}
