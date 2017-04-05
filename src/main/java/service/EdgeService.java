package service;

import model.Edge;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class EdgeService extends AbstractService<Edge> {
    public EdgeService(EntityManagerFactory emf) {
        super(emf);
    }

    @Override
    public Edge find(Long id) {
        EntityManager manager = this.managerFactory.createEntityManager();
        return manager.find(Edge.class, id);
    }
}
