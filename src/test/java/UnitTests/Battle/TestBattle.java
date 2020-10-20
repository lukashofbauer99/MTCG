package UnitTests.Battle;

import Model.Battle.Battle;
import Model.Cards.ACard;
import Model.Cards.Effects_Races.Effects.AEffect;
import Model.Cards.Effects_Races.Effects.BaseEffect;
import Model.Cards.Effects_Races.Effects.FireEffect;
import Model.Cards.Effects_Races.Effects.WaterEffect;
import Model.Cards.Effects_Races.Races.ARace;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class TestBattle {

    Battle battle;

    User userA= new User();
    User userB= new User();

    ACard cardA;
    ACard cardB;
    ACard cardC;
    ACard cardD;

    AEffect baseEffect= new BaseEffect();
    ARace baseRace= new BaseRace();


    @Test
    @DisplayName("Test Battle")
    void testBattle() {
        // arrange
        cardA=new MonsterCard("Dragon",10,new FireEffect(baseEffect),new DragonRace(baseRace));
        cardB=new MonsterCard("Water Goblin",100,new WaterEffect(baseEffect),new GoblinRace(baseRace));
        cardC=new SpellCard("Fire Spell",10,new FireEffect(baseEffect));
        cardD=new MonsterCard("Water Monster",7,new WaterEffect(baseEffect),baseRace);

        userA.getDeck().getCards().add(cardA);
        userA.getDeck().getCards().add(cardB);
        userA.getDeck().getCards().add(cardC);
        userA.getDeck().getCards().add(cardD);

        userB.getDeck().getCards().add(cardD);
        userB.getDeck().getCards().add(cardC);
        userB.getDeck().getCards().add(cardB);
        userB.getDeck().getCards().add(cardA);

        battle= new Battle(userA,userB);
        // act

        User winner = battle.Start();

        // assert

        assertNotNull(winner);

    }

}