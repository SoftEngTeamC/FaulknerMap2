package service;

import model.Coordinate;

import javax.persistence.EntityManager;
import java.util.List;

public class CoordinateService extends AbstractService<Coordinate> {
    @Override
    public Coordinate find(Long id) {
        EntityManager manager = this.managerFactory.createEntityManager();
        return manager.find(Coordinate.class, id);
    }

    public List<Coordinate> getCoordinatesByFloor(int floor){
        EntityManager manager = this.managerFactory.createEntityManager();
        return manager.createQuery(
                "SELECT c FROM Coordinate c WHERE c.floor = :floor", Coordinate.class)
                .setParameter("floor", floor)
                .getResultList();
    }
}
