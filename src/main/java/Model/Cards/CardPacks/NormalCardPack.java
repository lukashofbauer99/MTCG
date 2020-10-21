package Model.Cards.CardPacks;

import Model.Cards.ACard;
import Model.Cards.Effects_Races.Effects.BaseEffect;
import Model.Cards.Effects_Races.Effects.FireEffect;
import Model.Cards.Effects_Races.Effects.WaterEffect;
import Model.Cards.Effects_Races.Races.BaseRace;
import Model.Cards.Effects_Races.Races.DragonRace;
import Model.Cards.Effects_Races.Races.GoblinRace;
import Model.Cards.MonsterCard;
import Model.Cards.SpellCard;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
public class NormalCardPack extends ACardPack {

    //ENUM CARDPACKS
    Random rand = new Random();

    public NormalCardPack() {
        super(5, 5);
        genereateCards();
    }

    BaseEffect baseEffect= new BaseEffect();
    BaseRace baseRace = new BaseRace();
    @Override
    public void genereateCards() {
        int roll=rand.nextInt(50);
        if(roll<=2)
            super.Cards.add(new MonsterCard("OP Dragon",1000,new FireEffect(baseEffect),new DragonRace(baseRace)));
        else
            super.Cards.add(new MonsterCard("Dragon",10,new FireEffect(baseEffect),new DragonRace(baseRace)));

         super.Cards.add(new MonsterCard("Water Goblin",100,new WaterEffect(baseEffect),new GoblinRace(baseRace)));
         super.Cards.add(new SpellCard("Fire Spell",10,new FireEffect(baseEffect)));
         super.Cards.add(new MonsterCard("Water Monster",7,new WaterEffect(baseEffect),baseRace));

    }
}
