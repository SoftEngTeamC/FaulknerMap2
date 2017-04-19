package service;

import model.Coordinate;

import javax.persistence.EntityManager;
import java.util.List;

public class CoordinateService extends AbstractService<Coordinate> {
    @Override
    public Coordinate find(Long id) {
        EntityManager manager = this.managerFactory.createEntityManager();
        Coordinate temp =  manager.find(Coordinate.class, id);
        manager.close();
        return temp;
    }

    public List<Coordinate> getCoordinatesByFloor(int floor){
        EntityManager manager = this.managerFactory.createEntityManager();
        List<Coordinate> temp = manager.createQuery(
                "SELECT c FROM Coordinate c WHERE c.floor = :floor", Coordinate.class)
                .setParameter("floor", floor)
                .getResultList();
        manager.close();
        return temp;
    }
}
