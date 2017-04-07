package service;

import model.Edge;

import javax.persistence.EntityManager;

public class EdgeService extends AbstractService<Edge> {
    @Override
    public Edge find(Long id) {
        EntityManager manager = this.managerFactory.createEntityManager();
        return manager.find(Edge.class, id);
    }
}
