package Model.Battle;

import Model.Cards.ACard;
import Model.User.Deck;
import Model.User.User;
import jdk.jshell.spi.ExecutionControl;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Battle {

    BattleOutcome battleOutcome;

    User user1;
    User user2;

    BattleDeck battleDeckUser1;
    BattleDeck battleDeckUser2;

    List<Round> rounds = new ArrayList<>();

    public Battle(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
        battleDeckUser1= new BattleDeck( user1.getDeck().getCards());
        battleDeckUser2= new BattleDeck( user2.getDeck().getCards());
    }

    Boolean toggleAttacker=true;
    public User Start(){
       while(battleDeckUser1.getCards().size()>0&&battleDeckUser2.getCards().size()>0&&rounds.size()<101)
       {

           Round currentRound = new Round();
           ACard user1Card=battleDeckUser1.drawCard();
           ACard user2Card=battleDeckUser2.drawCard();

           if(toggleAttacker) {

               currentRound.fight(user1Card, user2Card);
               if (currentRound.roundOutcome==RoundOutcome.AttackerWon) {
                   battleDeckUser1.cards.add(user1Card);
                   battleDeckUser1.cards.add(user2Card);
               }
               else {
                   if (currentRound.roundOutcome == RoundOutcome.DefenderWon) {
                       battleDeckUser2.cards.add(user1Card);
                       battleDeckUser2.cards.add(user2Card);
                   }
                   else {
                       if(currentRound.roundOutcome==RoundOutcome.Draw){
                           battleDeckUser1.cards.add(user1Card);
                           battleDeckUser2.cards.add(user2Card);
                       }
                   }
               }
           }
           else {
               currentRound.fight(user2Card, user1Card);
               if (currentRound.roundOutcome==RoundOutcome.AttackerWon) {
                   battleDeckUser2.cards.add(user1Card);
                   battleDeckUser2.cards.add(user2Card);
               }
               else {
                   if (currentRound.roundOutcome == RoundOutcome.DefenderWon) {
                       battleDeckUser1.cards.add(user1Card);
                       battleDeckUser1.cards.add(user2Card);
                   }
                   else {
                       if(currentRound.roundOutcome==RoundOutcome.Draw){
                           battleDeckUser1.cards.add(user1Card);
                           battleDeckUser2.cards.add(user2Card);
                       }
                   }
               }
           }
           rounds.add(currentRound);
       }
       if(battleDeckUser1.cards.size()<battleDeckUser2.cards.size()) {
           battleOutcome = BattleOutcome.User2Won;
           user1.getBattleHistory().add(this);
           user2.getBattleHistory().add(this);
           return user1;
       }
       else {
           if (battleDeckUser2.cards.size() < battleDeckUser1.cards.size()){
               battleOutcome = BattleOutcome.User1Won;
               user1.getBattleHistory().add(this);
               user2.getBattleHistory().add(this);
               return user2;
           }
           else {
               battleOutcome = BattleOutcome.Draw;
               user1.getBattleHistory().add(this);
               user2.getBattleHistory().add(this);
               return null;
           }
       }
    }
}
