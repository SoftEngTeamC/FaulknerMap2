package service;

import model.Edge;
import model.Node;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import java.util.List;

public class EdgeService extends AbstractService<Edge> {
    @Override
    public Edge find(Long id) {
        EntityManager manager = this.managerFactory.createEntityManager();
        Edge temp = manager.find(Edge.class, id);
        manager.close();
        return temp;
    }

    public List<Edge> getAllEdges() {
        EntityManager manager = this.managerFactory.createEntityManager();
        List<Edge> temp = manager.createQuery("from Edge ", Edge.class)
                .getResultList();
        manager.close();
        return temp;
    }

    public List<Edge> getAllEdgesOnFloor(int floor) {
        EntityManager manager = this.managerFactory.createEntityManager();
        List<Edge> edges = manager.createQuery("SELECT e FROM Edge e WHERE e.start.location.floor = :floor AND e.end.location.floor = :floor", Edge.class)
                .setParameter("floor", floor)
                .getResultList();
        manager.close();
        return edges;
    }

    public Edge findByNodes(Node start, Node end) {
        // TODO: clean this up
        EntityManager manager = this.managerFactory.createEntityManager();
        Edge temp;
        try {
            temp = manager.createQuery("SELECT e FROM Edge e WHERE " +
                    "e.start.id = :start AND e.end.id = :end", Edge.class)
                    .setParameter("start", start.getId())
                    .setParameter("end", end.getId())
                    .getSingleResult();
            manager.close();
            return temp;
        } catch (Exception e) {
            try {
                temp = manager.createQuery("SELECT e FROM Edge e WHERE " +
                        "e.start.id = :start AND e.end.id = :end", Edge.class)
                        .setParameter("start", end.getId())
                        .setParameter("end", start.getId())
                        .getSingleResult();
                manager.close();
                return temp;
            } catch (Exception ex){
                manager.close();
                return null;
            }
        }
    }

}
