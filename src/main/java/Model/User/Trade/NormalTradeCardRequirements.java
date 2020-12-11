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
            getAllEffects(card.getEffect());

            getAllRaces(((MonsterCard)card).getRace());
            return effectsOnCard.contains(effect) && raceOnCard.contains(race);
        }
        else if(card.getClass()== SpellCard.class) {
            getAllEffects(card.getEffect());
            return effectsOnCard.contains(effect);
        }
        else
            return false;
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
