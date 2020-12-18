package Domain;

import java.util.List;

//TODO: Implement Rest APIs
public interface IRepository<entityType, idType> {

    idType persistEntity(entityType entity);

    boolean updateEntity(entityType entity);

    entityType findEntity(idType id);

    boolean deleteEntity(idType id);

    List<entityType> getAllEntities();

}
