package UnitTests.Cards;

import Model.Cards.ACard;
import Model.Cards.Effects_Races.Effects.AEffect;
import Model.Cards.Effects_Races.Effects.BaseEffect;
import Model.Cards.Effects_Races.Effects.FireEffect;
import Model.Cards.Effects_Races.Races.ARace;
import Model.Cards.Effects_Races.Races.BaseRace;
import Model.Cards.Effects_Races.Races.DragonRace;
import Model.Cards.MonsterCard;
import Model.Cards.SpellCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
public class TestCreateCard {

    AEffect baseEffect=new BaseEffect();
    ARace baseRace= new BaseRace();

    ACard createdCard;

    @Test
    @DisplayName("Create Simple Normal SpellCard")
    void testCreateSimpleNormalSpellCard() {
        // arrange
        // act
        createdCard=new SpellCard("Spear Throw",10,baseEffect);

        // assert

        assertEquals(createdCard.getAttackEffect(),baseEffect);
        assertEquals(createdCard.getDamage(),10);
        assertEquals(createdCard.getName(),"Spear Throw");
    }

    @Test
    @DisplayName("Create lvl1 Ignite SpellCard")
    void testCreateIngiteSpellCard() {
        // arrange
        // act

        createdCard=new SpellCard("Ignite",5,new FireEffect(baseEffect));

        // assert

        assertEquals(createdCard.getAttackEffect().getBase(),baseEffect);
        assertEquals(createdCard.getDamage(),5);
        assertEquals(createdCard.getName(),"Ignite");
    }

    @Test
    @DisplayName("Create lvl4 Meteor SpellCard")
    void testCreateSpellCard() {
        // arrange
        // act

        createdCard=new SpellCard("Meteor",30,
                new FireEffect(new FireEffect(new FireEffect(new FireEffect(baseEffect)))));

        // assert

        assertEquals(createdCard.getAttackEffect().getBase().getBase().getBase().getBase(),baseEffect);
        assertEquals(createdCard.getDamage(),30);
        assertEquals(createdCard.getName(),"Meteor");
    }

    @Test
    @DisplayName("Create Simple Normal MonsterCard")
    void testCreateSimpleNormalMonsterCard() {
        // arrange

        // act
        createdCard=new MonsterCard("Human Warrior",7,baseEffect,baseRace);



        // assert

        assertEquals(createdCard.getAttackEffect(),baseEffect);
        assertEquals(((MonsterCard)createdCard).getAttackRace(),baseRace);
        assertEquals(createdCard.getDamage(),7);
        assertEquals(createdCard.getName(),"Human Warrior");
    }

    @Test
    @DisplayName("Create Fire Dragon MonsterCard")
    void testCreateOrcFireMageMonsterCard() {
        // arrange

        // act
        createdCard=new MonsterCard("Orc Fire Mage",15,new FireEffect(baseEffect),new DragonRace(baseRace));



        // assert

        assertEquals(createdCard.getAttackEffect().getBase(),baseEffect);
        assertEquals(((MonsterCard)createdCard).getAttackRace().getBase(),baseRace);
        assertEquals(createdCard.getDamage(),15);
        assertEquals(createdCard.getName(),"Orc Fire Mage");
    }
}
