package Service.RESTServer.Service.Methods.POST;

import Domain.Cards.Interfaces.IACardRepository;
import Domain.Cards.Interfaces.ICardPackRepository;
import Domain.Cards.Interfaces.IEffectRepository;
import Domain.Cards.Interfaces.IRaceRepository;
import Model.Cards.ACard;
import Model.Cards.CardPacks.NormalCardPack;
import Model.Cards.MonsterCard;
import Service.RESTServer.Service.Methods.IHTTPMethod;
import Service.RESTServer.Service.Request.IRequestContext;
import Service.RESTServer.Service.Response.IResponseContext;
import Service.RESTServer.Service.Response.ResponseContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class POST_NormalPackages implements IHTTPMethod {

    ICardPackRepository cardPackRepository;
    IACardRepository cardRepository;
    IEffectRepository effectRepository;
    IRaceRepository raceRepository;
    ObjectMapper mapper = new ObjectMapper();


    public POST_NormalPackages(ICardPackRepository cardPackRepository, IACardRepository cardRepository,IEffectRepository effectRepository, IRaceRepository raceRepository) {
        this.raceRepository=raceRepository;
        this.effectRepository=effectRepository;
        this.cardPackRepository = cardPackRepository;
        this.cardRepository = cardRepository;
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }

    @Override
    public Boolean analyse(IRequestContext data) {
        return data.getHttpVerb_Res().startsWith("POST /packages ");
    }

    @Override
    public IResponseContext exec(IRequestContext data) throws JsonProcessingException {
        ResponseContext responseContext = new ResponseContext();
        List<ACard> cards = mapper.readValue(data.getPayload(), new TypeReference<List<ACard>>() {});

        cards.forEach(x->addEffectsAndRaceDependingOnName(x));

        Long id =cardPackRepository.persistEntity(new NormalCardPack(cards));
        responseContext.setPayload(String.valueOf(id));

        responseContext.setHttpStatusCode("HTTP/1.1 201");
        responseContext.getHeaders().put("Connection", "close");
        responseContext.getHeaders().put("Content-Length", String.valueOf(String.valueOf(id).length()));
        responseContext.getHeaders().put("Content-Type", "text/plain");
        return responseContext;
    }

    // Just to make the given curls work, better way is to have the effect/race objects in the card object in the curl
    void addEffectsAndRaceDependingOnName(ACard card)
    {
        effectRepository.getAllEntities().stream().filter(x->card.getName().toLowerCase().contains(x.getName())).forEach(
                x->{
                    card.setAttackEffect(x);
                    card.setDefendEffect(x);
                });
        if(card.getClass()== MonsterCard.class)
        {
            raceRepository.getAllEntities().stream().filter(x->card.getName().toLowerCase().contains(x.getName())).forEach(
                    x->{
                        ((MonsterCard) card).setRace(x);
                    });
        }
    }
}