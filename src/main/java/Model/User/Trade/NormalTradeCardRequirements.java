package Model.User.Trade;

import Model.Cards.ACard;
import Model.Cards.Effects_Races.Effects.IEffect;
import Model.Cards.Effects_Races.Races.IRace;
import Model.Cards.MonsterCard;
import Model.Cards.SpellCard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NormalTradeCardRequirements implements ITradeCardRequirements {

    double minimumDamage;

    Class cardType;

    IEffect effect;

    IRace race;


    public boolean evaluateRequirements(ACard card) {
        if (card.getDamage() < minimumDamage)
            return false;
        if (cardType != card.getClass())
            return false;

        List<IEffect> effectsOnCard = getAllEffects(card.getEffect());
        List<IRace> raceOnCard=getAllRaces(((MonsterCard) card).getRace());

        if (card.getClass() == MonsterCard.class&&effect!=null&&race!=null) {

            if(effect!=null&&race==null)
                return raceOnCard.contains(race);
            else if(race!=null&&effect==null)
                return effectsOnCard.contains(effect);
            else
                return effectsOnCard.contains(effect) && raceOnCard.contains(race);
        } else if (card.getClass() == SpellCard.class && effect !=null) {
            getAllEffects(card.getEffect());
            return effectsOnCard.contains(effect);
        } else
            return true;
    }

    List<IEffect> getAllEffects(IEffect curEffect) {
        List<IEffect> effectsOnCard= new ArrayList<>();
        effectsOnCard.add(curEffect);
        if (curEffect.getBase() != null)
            getAllEffects(curEffect.getBase());
        return effectsOnCard;
    }

    List<IRace> getAllRaces(IRace curRace) {
        List<IRace> raceOnCard= new ArrayList<>();
        raceOnCard.add(curRace);
        if (curRace.getBase() != null)
            getAllRaces(curRace.getBase());
        return raceOnCard;
    }


}
