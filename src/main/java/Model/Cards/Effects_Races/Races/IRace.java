package Model.Cards.Effects_Races.Races;

import Model.Battle.State;
import Model.Cards.ACard;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

//region JsonConfig
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BaseRace.class, name = "Base"),
        @JsonSubTypes.Type(value = DragonRace.class, name = "Dragon"),
        @JsonSubTypes.Type(value = FireElfRace.class, name = "FireElf"),
        @JsonSubTypes.Type(value = GoblinRace.class, name = "Goblin"),
        @JsonSubTypes.Type(value = KnightRace.class, name = "Knight"),
        @JsonSubTypes.Type(value = KrakenRace.class, name = "Kraken"),
        @JsonSubTypes.Type(value = OrkRace.class, name = "Ork"),
        @JsonSubTypes.Type(value = WizardRace.class, name = "Wizard")
}
)
//endregion
public interface IRace {

    Long getId();

    void setId(Long id);

    String getName();

    double affect(ACard thisCard, ACard opponentCard, State state);

    IRace getBase();
}
