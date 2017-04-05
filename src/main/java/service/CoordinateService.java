package service;

import model.Coordinate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class CoordinateService extends AbstractService<Coordinate> {
    public CoordinateService(EntityManagerFactory emf) {
        super(emf);  // Pass the manager factory up to the abstract service
    }

    @Override
    public Coordinate find(Long id) {
        EntityManager manager = this.managerFactory.createEntityManager();
        return manager.find(Coordinate.class, id);
    }
}
