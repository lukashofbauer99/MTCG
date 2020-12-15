package Model.Cards;

import Model.Battle.State;
import Model.Cards.Effects_Races.Effects.IEffect;
import Model.Cards.Effects_Races.Races.BaseRace;
import Model.Cards.Effects_Races.Races.IRace;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MonsterCard extends ACard {

    IRace race;


    public MonsterCard(String name, double damage, IEffect effect, IRace race) {
        super(name, damage,effect);
        this.race = race;
    }

    public MonsterCard(String name, double damage, IEffect attackEffect,IEffect defendEffect, IRace race) {
        super(name, damage, attackEffect);
        this.race = race;
    }



    @Override
    public double calcDamage(State state, ACard oppenentCard) {

        MonsterCard shallowCopy =new MonsterCard(this.name,this.damage,this.effect,this.race);
            if(oppenentCard.getClass()==MonsterCard.class)
                return shallowCopy.race.affect(shallowCopy,oppenentCard,state);
            else {
                if (oppenentCard.getClass() == SpellCard.class) {
                    shallowCopy.damage = shallowCopy.race.affect(shallowCopy, oppenentCard, state);
                    return shallowCopy.effect.affect(shallowCopy, oppenentCard);
                }
                else
                    throw new IllegalArgumentException() ; //unkown card type
            }
    }
}
