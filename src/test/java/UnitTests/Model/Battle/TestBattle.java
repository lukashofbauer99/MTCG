package UnitTests.Model.Battle;

import Model.Battle.Battle;
import Model.Cards.ACard;
import Model.Cards.Effects_Races.Effects.AEffect;
import Model.Cards.Effects_Races.Effects.BaseEffect;
import Model.Cards.Effects_Races.Effects.FireEffect;
import Model.Cards.Effects_Races.Effects.WaterEffect;
import Model.Cards.Effects_Races.Races.ABaseRace;
import Model.Cards.Effects_Races.Races.BaseRace;
import Model.Cards.Effects_Races.Races.DragonRace;
import Model.Cards.Effects_Races.Races.GoblinRace;
import Model.Cards.MonsterCard;
import Model.Cards.SpellCard;
import Model.User.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TestBattle {

    Battle battle;

    User userA = new User();
    User userB = new User();

    ACard cardA;
    ACard cardB;
    ACard cardC;
    ACard cardD;
    ACard cardE;


    AEffect baseEffect = new BaseEffect();
    ABaseRace baseRace = new BaseRace();


    @Test
    @DisplayName("Test Battle User2 Wins")
    void testBattleUser2() {
        // arrange
        cardA = new MonsterCard("Dragon", 10, new FireEffect(baseEffect), new DragonRace(baseRace));
        cardB = new MonsterCard("Water Goblin", 100, new WaterEffect(baseEffect), new GoblinRace(baseRace));
        cardC = new SpellCard("Fire Spell", 10, new FireEffect(baseEffect));
        cardD = new MonsterCard("Water Monster", 7, new WaterEffect(baseEffect), baseRace);

        cardE = new MonsterCard("OP Dragon", 1000, new FireEffect(baseEffect), new DragonRace(baseRace));

        userA.getDeck().getCards().add(cardA);
        userA.getDeck().getCards().add(cardB);
        userA.getDeck().getCards().add(cardC);
        userA.getDeck().getCards().add(cardD);

        userB.getDeck().getCards().add(cardD);
        userB.getDeck().getCards().add(cardC);
        userB.getDeck().getCards().add(cardB);
        userB.getDeck().getCards().add(cardE);

        battle = new Battle(userA, userB);
        // act

        battle.Start();

        // assert

        assertEquals(userB, battle.getWinner());
        assertEquals(userB.getMmr(), 103);


    }

    @Test
    @DisplayName("Test Battle User1 Wins")
    void testBattleUser1() {
        // arrange
        cardA = new MonsterCard("Dragon", 10, new FireEffect(baseEffect), new DragonRace(baseRace));
        cardB = new MonsterCard("Water Goblin", 100, new WaterEffect(baseEffect), new GoblinRace(baseRace));
        cardC = new SpellCard("Fire Spell", 10, new FireEffect(baseEffect));
        cardD = new MonsterCard("Water Monster", 7, new WaterEffect(baseEffect), baseRace);

        cardE = new MonsterCard("OP Dragon", 1000, new FireEffect(baseEffect), new DragonRace(baseRace));

        userA.getDeck().getCards().add(cardA);
        userA.getDeck().getCards().add(cardB);
        userA.getDeck().getCards().add(cardC);
        userA.getDeck().getCards().add(cardD);

        userB.getDeck().getCards().add(cardD);
        userB.getDeck().getCards().add(cardC);
        userB.getDeck().getCards().add(cardB);
        userB.getDeck().getCards().add(cardE);

        battle = new Battle(userB, userA);
        // act

        battle.Start();

        // assert

        assertEquals(userB, battle.getWinner());
        assertEquals(userB.getMmr(), 103);

    }

}