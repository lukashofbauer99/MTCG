package Model.Battle;

import Model.Cards.ACard;
import Model.User.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Getter
@Setter
@NoArgsConstructor
public class Battle {

    Long id;

    User user1;

    public Battle(Long id, User user1, User user2, User winner) {
        this.id = id;
        this.user1 = user1;
        this.user2 = user2;
        this.winner = winner;
    }

    User user2;

    User winner;

    BattleDeck battleDeckUser1;
    BattleDeck battleDeckUser2;

    List<Round> rounds = new ArrayList<>();

    public Battle(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
        battleDeckUser1 = new BattleDeck();
        battleDeckUser2 = new BattleDeck();
        battleDeckUser1.cards.addAll(user1.getDeck().getCards());
        battleDeckUser2.cards.addAll(user2.getDeck().getCards());
    }



    public void start() {
        boolean toggleAttacker = true;
        battleDeckUser1 = new BattleDeck();
        battleDeckUser2 = new BattleDeck();
        battleDeckUser1.cards.addAll(user1.getDeck().getCards());
        battleDeckUser2.cards.addAll(user2.getDeck().getCards());

        while (battleDeckUser1.getCards().size() > 0 && battleDeckUser2.getCards().size() > 0 && rounds.size() < 101) {

            Round currentRound = new Round();
            ACard user1Card = battleDeckUser1.drawCard();
            ACard user2Card = battleDeckUser2.drawCard();

            if (toggleAttacker) {

                currentRound.fight(user1Card, user2Card);
                if (currentRound.roundOutcome == RoundOutcome.AttackerWon) {
                    battleDeckUser1.cards.add(user1Card);
                    battleDeckUser1.cards.add(user2Card);
                } else {
                    if (currentRound.roundOutcome == RoundOutcome.DefenderWon) {
                        battleDeckUser2.cards.add(user1Card);
                        battleDeckUser2.cards.add(user2Card);
                    } else {
                        if (currentRound.roundOutcome == RoundOutcome.Draw) {
                            battleDeckUser1.cards.add(user1Card);
                            battleDeckUser2.cards.add(user2Card);
                        }
                    }
                }
            } else {
                currentRound.fight(user2Card, user1Card);
                if (currentRound.roundOutcome == RoundOutcome.AttackerWon) {
                    battleDeckUser2.cards.add(user1Card);
                    battleDeckUser2.cards.add(user2Card);
                } else {
                    if (currentRound.roundOutcome == RoundOutcome.DefenderWon) {
                        battleDeckUser1.cards.add(user1Card);
                        battleDeckUser1.cards.add(user2Card);
                    } else {
                        if (currentRound.roundOutcome == RoundOutcome.Draw) {
                            battleDeckUser1.cards.add(user1Card);
                            battleDeckUser2.cards.add(user2Card);
                        }
                    }
                }
            }
            toggleAttacker = !toggleAttacker;
            rounds.add(currentRound);
        }
        if (battleDeckUser1.cards.size() < battleDeckUser2.cards.size()) {
            user1.getBattleHistory().add(this);
            user1.setMmr(user1.getMmr() - 5);
            user2.getBattleHistory().add(this);
            user2.setMmr(user2.getMmr() + 3);
            winner = user2;
        } else {
            if (battleDeckUser2.cards.size() < battleDeckUser1.cards.size()) {
                user1.getBattleHistory().add(this);
                user1.setMmr(user1.getMmr() + 3);
                user2.getBattleHistory().add(this);
                user2.setMmr(user2.getMmr() - 5);
                winner = user1;
            } else {
                user1.getBattleHistory().add(this);
                user2.getBattleHistory().add(this);
                winner = null;
            }
        }

    }
}
