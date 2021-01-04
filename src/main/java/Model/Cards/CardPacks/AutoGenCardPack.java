package Model.Cards.CardPacks;

import Model.Cards.Effects_Races.Effects.BaseEffect;
import Model.Cards.Effects_Races.Effects.FireEffect;
import Model.Cards.Effects_Races.Effects.WaterEffect;
import Model.Cards.Effects_Races.Races.BaseRace;
import Model.Cards.Effects_Races.Races.DragonRace;
import Model.Cards.Effects_Races.Races.GoblinRace;
import Model.Cards.MonsterCard;
import Model.Cards.SpellCard;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Random;

@Getter
public class AutoGenCardPack extends ACardPackFixedSizeAndCost {

    Random rand = new Random();

    public AutoGenCardPack() {
        super(5, 4,PackType.AutoGen);
        genereateCards();
    }

    BaseEffect baseEffect = new BaseEffect();
    BaseRace baseRace = new BaseRace();

    void genereateCards() {
        int roll = rand.nextInt(50);
        if (roll <= 2)
            super.cards.add(new MonsterCard("OP Dragon", 1000, new FireEffect(baseEffect), new DragonRace(baseRace)));
        else
            super.cards.add(new MonsterCard("Dragon", 10, new FireEffect(baseEffect), new DragonRace(baseRace)));

        super.cards.add(new MonsterCard("Water Goblin", 100, new WaterEffect(baseEffect), new GoblinRace(baseRace)));
        super.cards.add(new SpellCard("Fire Spell", 10, new FireEffect(baseEffect)));
        super.cards.add(new MonsterCard("Water Monster", 7, new WaterEffect(baseEffect), baseRace));

    }
}
