package Model.Battle;

import Model.Cards.ACard;
import lombok.Getter;

@Getter
public class Round {

    Long id;

    RoundOutcome roundOutcome;

    double winnerDmg;
    double looserDmg;

    ACard winner;
    ACard looser;

    public void fight(ACard attackingCard, ACard defendingCard) {
        double attackingCardDmg;
        double defendingCardDmg;

        attackingCardDmg = attackingCard.calcDamage(State.ATTACK, defendingCard);
        defendingCardDmg = defendingCard.calcDamage(State.DEFEND, attackingCard);

        if (attackingCardDmg > defendingCardDmg) {
            winner = attackingCard;
            looser = defendingCard;

            winnerDmg = attackingCardDmg;
            looserDmg = defendingCardDmg;

            roundOutcome = RoundOutcome.AttackerWon;

        } else {
            if (defendingCardDmg > attackingCardDmg) {
                winner = defendingCard;
                looser = attackingCard;

                winnerDmg = defendingCardDmg;
                looserDmg = attackingCardDmg;

                roundOutcome = RoundOutcome.DefenderWon;
            } else {
                winner = defendingCard;
                looser = attackingCard;

                winnerDmg = defendingCardDmg;
                looserDmg = attackingCardDmg;

                roundOutcome = RoundOutcome.Draw;
            }

        }


    }
}
