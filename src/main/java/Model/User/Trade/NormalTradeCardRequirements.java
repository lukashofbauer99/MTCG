package Model.User.Trade;

import Model.Cards.ACard;
import Model.Cards.Effects_Races.Effects.IEffect;
import Model.Cards.Effects_Races.Races.IRace;
import Model.Cards.MonsterCard;
import Model.Cards.SpellCard;

import java.util.List;

public class NormalTradeCardRequirements implements ITradeCardRequirements {

    int minimumDamage;

    Class cardType;

    IEffect effect;

    IRace race;

    List<IEffect> effectsOnCard;

    List<IRace> raceOnCard;

    public boolean evaluateRequirements(ACard card) {
        if(card.getDamage()<minimumDamage)
            return false;
        if (cardType!=card.getClass())
            return false;
        if(card.getClass()== MonsterCard.class)
        {
            getAllEffects(card.getAttackEffect());
            getAllEffects(card.getDefendEffect());

            getAllRaces(((MonsterCard)card).getAttackRace());
            getAllRaces(((MonsterCard)card).getDefendRace());
            if(!effectsOnCard.contains(effect)||!raceOnCard.contains(race))
            {
                return false;
            }
        }
        else if(card.getClass()== SpellCard.class) {
            getAllEffects(card.getAttackEffect());
            getAllEffects(card.getDefendEffect());
            if(!effectsOnCard.contains(effect))
            {
                return false;
            }
        }
        else
            return false;
        return true;
    }

    void getAllEffects(IEffect curEffect)
    {
        effectsOnCard.add(curEffect);
        if(curEffect.getBase()!=null)
            getAllEffects(curEffect.getBase());
    }

    void getAllRaces(IRace curRace)
    {
        raceOnCard.add(curRace);
        if(curRace.getBase()!=null)
            getAllRaces(curRace.getBase());
    }


}
