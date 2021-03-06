package Service.RESTServer.Service.Methods.POST;

import Domain.Cards.Interfaces.IACardRepository;
import Domain.Cards.Interfaces.IEffectRepository;
import Domain.Cards.Interfaces.IRaceRepository;
import Model.Cards.ACard;
import Model.Cards.MonsterCard;
import Service.RESTServer.Service.Methods.IHTTPMethod;
import Service.RESTServer.Service.Request.IRequestContext;
import Service.RESTServer.Service.Response.IResponseContext;
import Service.RESTServer.Service.Response.ResponseContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class POST_cards_Effects_RaceFromName implements IHTTPMethod {

    IACardRepository cardRepository;
    IEffectRepository effectRepository;
    IRaceRepository raceRepository;
    ObjectMapper mapper = new ObjectMapper();


    public POST_cards_Effects_RaceFromName(IACardRepository cardRepository, IEffectRepository effectRepository, IRaceRepository raceRepository) {
        this.raceRepository = raceRepository;
        this.effectRepository = effectRepository;
        this.cardRepository = cardRepository;
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }

    @Override
    public Boolean analyse(IRequestContext data) {
        return data.getHttpVerb_Res().startsWith("POST /cards ");
    }

    @Override
    public IResponseContext exec(IRequestContext data) {
        ResponseContext responseContext = new ResponseContext();
        ACard card = null;
        try {
            card = mapper.readValue(data.getPayload(), ACard.class);
        } catch (JsonProcessingException e) {
            responseContext.setPayload("Invalid form of data");
            responseContext.setHttpStatusCode("HTTP/1.1 400");
        }

        addEffectsAndRaceDependingOnName(card);

        String id = cardRepository.persistEntity(card);
        responseContext.setPayload(id);

        responseContext.setHttpStatusCode("HTTP/1.1 201");
        responseContext.getHeaders().put("Connection", "close");
        responseContext.getHeaders().put("Content-Length", String.valueOf(responseContext.getPayload().length()));
        responseContext.getHeaders().put("Content-Type", "text/plain");
        return responseContext;
    }

    // Just to make the given curls work, better way is to have the effect/race objects in the card object in the curl
    void addEffectsAndRaceDependingOnName(ACard card) {
        card.setEffect(effectRepository.getAllEntities()
                .stream()
                .filter(x -> card.getName().toLowerCase().contains(x.getName()))
                .findFirst()
                .orElse(effectRepository.getIEffectWithName("normal")));

        if (card.getClass() == MonsterCard.class) {
            ((MonsterCard) card).setRace(raceRepository.getAllEntities().stream().filter(x -> card.getName().toLowerCase().contains(x.getName())).findFirst().orElse(raceRepository.getIRaceWithName("base")));
        }
    }


}