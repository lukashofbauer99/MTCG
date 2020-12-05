package Domain;

import java.util.List;

//TODO: Create other IRepository Interfaces
//TODO: Update datastructure and logic according to curls
//TODO: Implement Rest APIs
public interface IRepository<entityType,idType> {

    int persistEntity(entityType entity);

    int updateEntity(entityType entity);

    entityType findEntity(idType id);

    boolean deleteEntity(idType id);

    List<entityType> getAllEntities();

}
