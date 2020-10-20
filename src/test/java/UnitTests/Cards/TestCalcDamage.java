package UnitTests.Cards;

import Model.Battle.State;
import Model.Cards.ACard;
import Model.Cards.Effects_Races.Effects.BaseEffect;
import Model.Cards.Effects_Races.Races.BaseRace;
import Model.Cards.Effects_Races.Effects.FireEffect;
import Model.Cards.Effects_Races.Effects.WaterEffect;
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
public class TestCalcDamage {

    ACard cardA;
    ACard cardB;

    BaseEffect baseEffect= new BaseEffect();
    BaseRace baseRace = new BaseRace();


    @Test
    @DisplayName("Normal Spell Card CalcDamage as Attacker")
    void test_NormalSpellCard_CalcDamage() {
        // arrange
        cardA=new SpellCard("Spear Throw",10,baseEffect);
        cardB=new MonsterCard("Human Warrior",7,baseEffect,baseRace);
        int damageCalculated;
        // act
        damageCalculated=cardA.calcDamage(State.ATTACK,cardB);
        // assert
        assertEquals(10,damageCalculated);
    }

    @Test
    @DisplayName("Normal Monster Card CalcDamage")
    void test_NormalMonsterCard_CalcDamage() {
        // arrange
        cardA=new SpellCard("Spear Throw",10,baseEffect);
        cardB=new MonsterCard("Human Warrior",7,baseEffect,baseRace);
        int damageCalculated;
        // act
        damageCalculated=cardB.calcDamage(State.DEFEND,cardA);
        // assert
        assertEquals(7,damageCalculated);
    }

    @Test
    @DisplayName("Fire Monster vs Water Monster")
    void test_FireMVsWaterM_CalcDamage() {
        // arrange
        cardA=new MonsterCard("Fire Monster",10,new FireEffect(baseEffect),baseRace);
        cardB=new MonsterCard("Water Monster",7,new WaterEffect(baseEffect),baseRace);
        int damageCalculated;
        // act
        damageCalculated=cardB.calcDamage(State.ATTACK,cardA);
        // assert
        assertEquals(7,damageCalculated);
    }

    @Test
    @DisplayName("Fire Spell vs Water Monster")
    void test_FireSVsWaterMCalcDamage() {
        // arrange
        cardA=new SpellCard("Fire Spell",10,new FireEffect(baseEffect));
        cardB=new MonsterCard("Water Monster",7,new WaterEffect(baseEffect),baseRace);
        int damageCalculated;
        // act
        damageCalculated=cardB.calcDamage(State.ATTACK,cardA);
        // assert
        assertEquals(14,damageCalculated);
    }


    @Test
    @DisplayName("Fire Dragon vs Water Goblin")
    void test_FireDVsWaterG_CalcDamage() {
        // arrange
        cardA=new MonsterCard("Dragon",10,new FireEffect(baseEffect),new DragonRace(baseRace));
        cardB=new MonsterCard("Water Goblin",100,new WaterEffect(baseEffect),new GoblinRace(baseRace));
        int damageCalculated;
        // act
        damageCalculated=cardB.calcDamage(State.ATTACK,cardA);
        // assert
        assertEquals(0,damageCalculated);
    }


}
