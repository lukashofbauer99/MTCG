package Model.Cards;

import Model.Battle.State;
import Model.Cards.Effects_Races.Effects.IEffect;
import Model.Cards.Effects_Races.Races.IRace;
import lombok.Getter;

@Getter
public class MonsterCard extends ACard {
    IRace attackRace; //As example Goblin attack -> cant damage dragon -> dmg*0
    IRace defendRace; //Goblin defend -> dragon -> dmg (* 1)


    public MonsterCard(String name, int damage, IEffect effect, IRace race) {
        super(name, damage, effect,effect);
        this.attackRace = race;
        this.defendRace = race;
    }

    public MonsterCard(String name, int damage, IEffect attackEffect,IEffect defendEffect, IRace race) {
        super(name, damage, attackEffect,defendEffect);
        this.attackRace = race;
        this.defendRace = race;
    }

    public MonsterCard(String name, int damage, IEffect effect, IRace attackRace, IRace defendRace) {
        super(name, damage, effect,effect);
        this.attackRace = attackRace;
        this.defendRace = defendRace;
    }

    public MonsterCard(String name, int damage, IEffect attackEffect, IEffect defendEffect, IRace attackRace, IRace defendRace) {
        super(name, damage, attackEffect,defendEffect);
        this.attackRace = attackRace;
        this.defendRace = defendRace;
    }



    @Override
    public int calcDamage(State state, ACard oppenentCard) {

        MonsterCard shallowCopy =new MonsterCard(this.name,this.damage,this.attackEffect,this.defendEffect,this.attackRace,this.defendRace);

        if (state==State.ATTACK) {
            if(oppenentCard.getClass()==MonsterCard.class)
                return shallowCopy.attackRace.affect(shallowCopy,oppenentCard);
            else {
                if (oppenentCard.getClass() == SpellCard.class) {
                    shallowCopy.damage = shallowCopy.attackRace.affect(shallowCopy, oppenentCard);
                    return shallowCopy.attackEffect.affect(shallowCopy, oppenentCard);
                }
                else
                    throw new IllegalArgumentException() ; //unkown card type
            }
        }
        else
        {
            if (state==State.DEFEND) {
                if(oppenentCard.getClass()==MonsterCard.class)
                    return shallowCopy.defendRace.affect(shallowCopy,oppenentCard);
                else {
                    if (oppenentCard.getClass() == SpellCard.class) {
                        shallowCopy.damage = shallowCopy.defendRace.affect(shallowCopy, oppenentCard);
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
}
