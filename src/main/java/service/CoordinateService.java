package service;

import model.Coordinate;

import javax.persistence.EntityManager;

public class CoordinateService extends AbstractService<Coordinate> {
    @Override
    public Coordinate find(Long id) {
        EntityManager manager = this.managerFactory.createEntityManager();
        return manager.find(Coordinate.class, id);
    }
}
