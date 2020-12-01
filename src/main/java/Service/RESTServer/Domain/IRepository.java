package Service.RESTServer.Domain;

import java.util.List;

public interface IRepository<entityType> {

    int persistEntity(entityType entity);

    entityType findEntity(int id);

    boolean deleteEntity(int id);

    List<entityType> getAllEntities();

    int getNextID();

}
