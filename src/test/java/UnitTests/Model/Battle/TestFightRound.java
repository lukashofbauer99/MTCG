package UnitTests.Model.Battle;

import Model.Battle.Round;
import Model.Cards.ACard;
import Model.Cards.Effects_Races.Effects.BaseEffect;
import Model.Cards.Effects_Races.Effects.FireEffect;
import Model.Cards.Effects_Races.Effects.WaterEffect;
import Model.Cards.Effects_Races.Races.BaseRace;
import Model.Cards.Effects_Races.Races.DragonRace;
import Model.Cards.Effects_Races.Races.GoblinRace;
import Model.Cards.MonsterCard;
import Model.Cards.SpellCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TestFightRound {

    Round round = new Round();

    ACard attackingCard;
    ACard defendingCard;

    BaseEffect baseEffect = new BaseEffect();
    BaseRace baseRace = new BaseRace();


    @Test
    @DisplayName("Normal SpellCard vs normal MonsterCard Fight Round")
    void test_NormalSpellCard_FightRound() {
        // arrange
        attackingCard = new SpellCard("Spear Throw", 10, baseEffect);
        defendingCard = new MonsterCard("Human Warrior", 7, baseEffect, baseRace);

        // act
        round.fight(attackingCard, defendingCard);

        // assert
        assertEquals(round.getWinner(), attackingCard);
        assertEquals(round.getLooser(), defendingCard);
    }

    @Test
    @DisplayName("Normal Monster Cards Fight Round")
    void test_NormalMonsterCard_FightRound() {
        // arrange
        attackingCard = new SpellCard("Spear Throw", 10, baseEffect);
        defendingCard = new MonsterCard("Human Warrior", 7, baseEffect, baseRace);

        // act
        round.fight(attackingCard, defendingCard);

        // assert
        assertEquals(round.getWinner(), attackingCard);
        assertEquals(round.getLooser(), defendingCard);
    }

    @Test
    @DisplayName("Fire Monster vs Water Monster Fight Round")
    void test_FireMVsWaterM_FightRound() {
        // arrange
        attackingCard = new MonsterCard("Fire Monster", 10, new FireEffect(baseEffect), baseRace);
        defendingCard = new MonsterCard("Water Monster", 7, new WaterEffect(baseEffect), baseRace);

        // act
        round.fight(attackingCard, defendingCard);

        // assert
        assertEquals(round.getWinner(), attackingCard);
        assertEquals(round.getLooser(), defendingCard);
    }

    @Test
    @DisplayName("Fire Spell vs Water Monster Fight Round")
    void test_FireSVsWaterM_FightRound() {
        // arrange
        attackingCard = new SpellCard("Fire Spell", 10, new FireEffect(baseEffect));
        defendingCard = new MonsterCard("Water Monster", 7, new WaterEffect(baseEffect), baseRace);

        // act
        round.fight(attackingCard, defendingCard);

        // assert
        assertEquals(round.getWinner(), defendingCard);
        assertEquals(round.getLooser(), attackingCard);
    }


    @Test
    @DisplayName("Fire Dragon vs Water Goblin Fight Round")
    void test_FireDVsWaterG_FightRound() {
        // arrange
        attackingCard = new MonsterCard("Water Goblin", 100, new WaterEffect(baseEffect), new GoblinRace(baseRace));
        defendingCard = new MonsterCard("Dragon", 10, new FireEffect(baseEffect), new DragonRace(baseRace));
        int damageCalculated;

        // act
        round.fight(attackingCard, defendingCard);

        // assert
        assertEquals(round.getWinner(), defendingCard);
        assertEquals(round.getLooser(), attackingCard);
    }

}