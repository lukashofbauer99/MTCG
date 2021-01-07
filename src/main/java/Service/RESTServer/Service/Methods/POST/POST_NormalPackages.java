package Service.RESTServer.Service.Methods.POST;

import Domain.Cards.Interfaces.*;
import Model.Cards.ACard;
import Model.Cards.CardPacks.ICardPack;
import Model.Cards.CardPacks.NormalCardPack;
import Model.Cards.MonsterCard;
import Model.Cards.Vendor.IVendor;
import Service.RESTServer.Service.Methods.IHTTPMethod;
import Service.RESTServer.Service.Request.IRequestContext;
import Service.RESTServer.Service.Response.IResponseContext;
import Service.RESTServer.Service.Response.ResponseContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

//TODO: create POST_NormalPackages_new with Authorisation
public class POST_NormalPackages implements IHTTPMethod {

    ICardPackRepository cardPackRepository;
    IACardRepository cardRepository;
    IEffectRepository effectRepository;
    IRaceRepository raceRepository;
    IVendorRepository vendorRepository;
    ObjectMapper mapper = new ObjectMapper();


    public POST_NormalPackages(ICardPackRepository cardPackRepository, IACardRepository cardRepository, IEffectRepository effectRepository, IRaceRepository raceRepository, IVendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
        this.raceRepository = raceRepository;
        this.effectRepository = effectRepository;
        this.cardPackRepository = cardPackRepository;
        this.cardRepository = cardRepository;
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }

    @Override
    public Boolean analyse(IRequestContext data) {
        return data.getHttpVerb_Res().startsWith("POST /packages ");
    }

    @Override
    public IResponseContext exec(IRequestContext data) {
        ResponseContext responseContext = new ResponseContext();
        List<ACard> cards = new ArrayList<>();
        try {
            cards = mapper.readValue(data.getPayload(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            responseContext.setPayload("Invalid form of Data");
            responseContext.setHttpStatusCode("HTTP/1.1 400");
        }

        cards.forEach(this::addEffectsAndRaceDependingOnName);
        cards.forEach(x -> cardRepository.persistEntityGenNoId(x));
        ICardPack cardPack = new NormalCardPack(cards);
        Long id = cardPackRepository.persistEntity(cardPack);

        IVendor vendor= vendorRepository.getAllEntities().stream().findFirst().orElse(null);
        if(vendor!=null) {
            vendor.addICardPack(cardPack);
            vendorRepository.updateEntity(vendor);
        }


        responseContext.setPayload(String.valueOf(id));

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
                .orElse(effectRepository.getIEffectWithName("regular")));

        if (card.getClass() == MonsterCard.class) {
            ((MonsterCard) card).setRace(raceRepository.getAllEntities().stream().filter(x -> card.getName().toLowerCase().contains(x.getName())).findFirst().orElse(raceRepository.getIRaceWithName("base")));
        }
    }
}