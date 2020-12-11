package UnitTests.Model.Cards;

import Model.Battle.State;
import Model.Cards.ACard;
import Model.Cards.Effects_Races.Effects.BaseEffect;
import Model.Cards.Effects_Races.Effects.NormalEffect;
import Model.Cards.Effects_Races.Races.*;
import Model.Cards.Effects_Races.Effects.FireEffect;
import Model.Cards.Effects_Races.Effects.WaterEffect;
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
    @DisplayName("Normal Spell Card CalcDamage as Attack")
    void test_NormalSpellCard_Attack_CalcDamage() {
        // arrange
        cardA=new SpellCard("Spear Throw",10,new NormalEffect(baseEffect));
        cardB=new MonsterCard("Human Warrior",7,new NormalEffect(baseEffect),baseRace);
        double damageCalculated;
        // act
        damageCalculated=cardA.calcDamage(State.ATTACK,cardB);
        // assert
        assertEquals(10,damageCalculated);
    }

    @Test
    @DisplayName("Normal Monster Card CalcDamage Defend")
    void test_NormalMonsterCard_Defend_CalcDamage() {
        // arrange
        cardA=new SpellCard("Spear Throw",10,baseEffect);
        cardB=new MonsterCard("Human Warrior",7,baseEffect,baseRace);
        double damageCalculated;
        // act
        damageCalculated=cardB.calcDamage(State.DEFEND,cardA);
        // assert
        assertEquals(7,damageCalculated);
    }

    @Test
    @DisplayName("Fire Monster vs Water Monster")
    void test_FireMVsWaterM_Attack_CalcDamage() {
        // arrange
        cardA=new MonsterCard("Fire Monster",10,new FireEffect(baseEffect),baseRace);
        cardB=new MonsterCard("Water Monster",7,new WaterEffect(baseEffect),baseRace);
        double damageCalculated;
        // act
        damageCalculated=cardB.calcDamage(State.ATTACK,cardA);
        // assert
        assertEquals(7,damageCalculated);
    }

    @Test
    @DisplayName("Fire Spell vs Water Monster")
    void test_FireSVsWaterM_Attack_CalcDamage() {
        // arrange
        cardA=new SpellCard("Fire Spell",10,new FireEffect(baseEffect));
        cardB=new MonsterCard("Water Monster",7,new WaterEffect(baseEffect),baseRace);
        double damageCalculated;
        // act
        damageCalculated=cardB.calcDamage(State.ATTACK,cardA);
        // assert
        assertEquals(14,damageCalculated);
    }


    @Test
    @DisplayName("Fire Dragon vs Water Goblin Attack")
    void test_FireDVsWaterG_Attack_CalcDamage() {
        // arrange
        cardA=new MonsterCard("Dragon",10,new FireEffect(baseEffect),new DragonRace(baseRace));
        cardB=new MonsterCard("Water Goblin",100,new WaterEffect(baseEffect),new GoblinRace(baseRace));
        double damageCalculated;
        // act
        damageCalculated=cardB.calcDamage(State.ATTACK,cardA);
        // assert
        assertEquals(0,damageCalculated);
    }

    @Test
    @DisplayName("Fire Dragon vs Water Goblin Defend")
    void test_FireDrVsWaterG_Defend_CalcDamage() {
        // arrange
        cardA=new MonsterCard("Dragon",10,new FireEffect(baseEffect),new DragonRace(baseRace));
        cardB=new MonsterCard("Water Goblin",100,new WaterEffect(baseEffect),new GoblinRace(baseRace));
        double damageCalculated;
        // act
        damageCalculated=cardB.calcDamage(State.DEFEND,cardA);
        // assert
        assertEquals(100,damageCalculated);
    }

    @Test
    @DisplayName("Water Spell vs Normal Monster Defend")
    void test_WaterSpellVsNormalMonster_Defend_CalcDamage() {
        // arrange
        cardA=new SpellCard("WaterBall",10,new WaterEffect(baseEffect));
        cardB=new MonsterCard("Normal Goblin",100,new NormalEffect(baseEffect),new GoblinRace(baseRace));
        double damageCalculated;
        // act
        damageCalculated=cardB.calcDamage(State.DEFEND,cardA);
        // assert
        assertEquals(200,damageCalculated);
    }

    @Test
    @DisplayName("Fire Spell vs Normal Spell Attack")
    void test_FireBallVsRockThrow_Attack_CalcDamage() {
        // arrange
        cardA=new SpellCard("Fire Ball",10,new FireEffect(baseEffect));
        cardB=new SpellCard("Rock Throw",20,new NormalEffect(baseEffect));
        double damageCalculated;
        // act
        damageCalculated=cardB.calcDamage(State.ATTACK,cardA);
        // assert
        assertEquals(10,damageCalculated);
    }


    @Test
    @DisplayName("Water Spell vs Normal Knight Attack")
    void test_WaterSpellVsKnight_Attack_CalcDamage() {
        // arrange
        cardA=new SpellCard("Water Ball",10,new WaterEffect(baseEffect));
        cardB=new MonsterCard("Knight",20,new NormalEffect(baseEffect), new KnightRace(baseRace));
        double damageCalculated;
        // act
        damageCalculated=cardB.calcDamage(State.ATTACK,cardA);
        // assert
        assertEquals(0,damageCalculated);
    }

    @Test
    @DisplayName("Dragon vs FireElf Attack")
    void test_DragonVsFireElf_Attack_CalcDamage() {
        // arrange
        cardA=new MonsterCard("Fireelf",20,new FireEffect(baseEffect), new FireElfRace(baseRace));
        cardB=new MonsterCard("Dragon",100,new NormalEffect(baseEffect),new DragonRace(baseRace));
        double damageCalculated;
        // act
        damageCalculated=cardB.calcDamage(State.ATTACK,cardA);
        // assert
        assertEquals(0,damageCalculated);
    }

    @Test
    @DisplayName("Dragon vs FireElf Attack")
    void test_DragonVsFireElf_Defend_CalcDamage() {
        // arrange
        cardA=new MonsterCard("Fireelf",20,new FireEffect(baseEffect), new FireElfRace(baseRace));
        cardB=new MonsterCard("Dragon",100,new NormalEffect(baseEffect),new DragonRace(baseRace));
        double damageCalculated;
        // act
        damageCalculated=cardB.calcDamage(State.DEFEND,cardA);
        // assert
        assertEquals(100,damageCalculated);
    }


    @Test
    @DisplayName("Normal Spell vs Kraken Attack")
    void test_SpellVsKraken_Attack_CalcDamage() {
        // arrange
        cardA=new MonsterCard("Kraken",200,new WaterEffect(baseEffect), new KrakenRace(baseRace));
        cardB=new SpellCard("Hulk Smash",1000,new NormalEffect(baseEffect));
        double damageCalculated;
        // act
        damageCalculated=cardB.calcDamage(State.ATTACK,cardA);
        // assert
        assertEquals(0,damageCalculated);
    }

    @Test
    @DisplayName("Water Wizard vs Ork Attack")
    void test_WaterWVsOrk_Attack_CalcDamage() {
        // arrange
        cardA=new MonsterCard("Water Wizard",20,new WaterEffect(baseEffect), new WizardRace(baseRace));
        cardB=new MonsterCard("Ork",25,new NormalEffect(baseEffect),new OrkRace(baseRace));
        double damageCalculated;
        // act
        damageCalculated=cardB.calcDamage(State.ATTACK,cardA);
        // assert
        assertEquals(0,damageCalculated);
    }

    @Test
    @DisplayName("Water Wizard vs Ork Attack")
    void test_WaterWVsOrk_Defend_CalcDamage() {
        // arrange
        cardA=new MonsterCard("Water Wizard",20,new WaterEffect(baseEffect), new WizardRace(baseRace));
        cardB=new MonsterCard("Ork",25,new NormalEffect(baseEffect),new OrkRace(baseRace));
        double damageCalculated;
        // act
        damageCalculated=cardB.calcDamage(State.DEFEND,cardA);
        // assert
        assertEquals(25,damageCalculated);
    }




}
