package Model.Cards;

import Model.Battle.State;
import Model.Cards.Effects_Races.Effects.IEffect;
import Model.Cards.Effects_Races.Races.BaseRace;
import Model.Cards.Effects_Races.Races.IRace;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
public class MonsterCard extends ACard {

    @Setter
    IRace race = new BaseRace(); //Goblin defend -> dragon -> dmg (* 1)


    public MonsterCard(String name, double damage, IEffect effect, IRace race) {
        super(name, damage, effect,effect);
        this.race = race;
    }

    public MonsterCard(String name, double damage, IEffect attackEffect,IEffect defendEffect, IRace race) {
        super(name, damage, attackEffect,defendEffect);
        this.race = race;
    }



    @Override
    public double calcDamage(State state, ACard oppenentCard) {

        MonsterCard shallowCopy =new MonsterCard(this.name,this.damage,this.attackEffect,this.defendEffect,this.race);
        if (state==State.ATTACK) {
            if(oppenentCard.getClass()==MonsterCard.class)
                return shallowCopy.race.affect(shallowCopy,oppenentCard,State.ATTACK);
            else {
                if (oppenentCard.getClass() == SpellCard.class) {
                    shallowCopy.damage = shallowCopy.race.affect(shallowCopy, oppenentCard, State.ATTACK);
                    return shallowCopy.defendEffect.affect(shallowCopy, oppenentCard);
                }
                else
                    throw new IllegalArgumentException() ; //unkown card type
            }
        }
        else
            if (state==State.DEFEND) {
                if(oppenentCard.getClass()==MonsterCard.class)
                    return shallowCopy.race.affect(shallowCopy,oppenentCard,State.DEFEND);
                else {
                    if (oppenentCard.getClass() == SpellCard.class) {
                        shallowCopy.damage = shallowCopy.race.affect(shallowCopy, oppenentCard, State.DEFEND);
                        return shallowCopy.defendEffect.affect(shallowCopy, oppenentCard);
                    }
                    else
                        throw new IllegalArgumentException() ; //unkown card type
                }
            }
            else
                throw new IllegalArgumentException() ; //no allowed State
    }
}
