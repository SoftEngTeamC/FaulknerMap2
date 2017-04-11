package service;

import model.Edge;

import javax.persistence.EntityManager;
import java.util.List;

public class EdgeService extends AbstractService<Edge> {
    @Override
    public Edge find(Long id) {
        EntityManager manager = this.managerFactory.createEntityManager();
        return manager.find(Edge.class, id);
    }

    public List<Edge> getAllEdges() {
        EntityManager manager = this.managerFactory.createEntityManager();
        return manager.createQuery("from Edge ", Edge.class)
                .getResultList();
    }
}
