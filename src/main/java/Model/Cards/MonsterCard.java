package Model.Cards;

import Model.Battle.State;
import Model.Cards.Effects_Races.Effects.AEffect;
import Model.Cards.Effects_Races.Races.ARace;
import lombok.Getter;

@Getter
public class MonsterCard extends ACard {
    ARace attackRace; //As example Goblin attack -> cant damage dragon -> dmg*0
    ARace defendRace; //Goblin defend -> dragon -> dmg (* 1)


    public MonsterCard(String name, int damage, AEffect effect, ARace race) {
        super(name, damage, effect,effect);
        this.attackRace = race;
        this.defendRace = race;
    }

    public MonsterCard(String name, int damage, AEffect attackEffect,AEffect defendEffect, ARace race) {
        super(name, damage, attackEffect,defendEffect);
        this.attackRace = race;
        this.defendRace = race;
    }

    public MonsterCard(String name, int damage, AEffect effect, ARace attackRace, ARace defendRace) {
        super(name, damage, effect,effect);
        this.attackRace = attackRace;
        this.defendRace = defendRace;
    }

    public MonsterCard(String name, int damage, AEffect attackEffect,AEffect defendEffect, ARace attackRace, ARace defendRace) {
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
