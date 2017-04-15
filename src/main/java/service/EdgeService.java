package service;

import model.Edge;
import model.Node;

import javax.persistence.EntityManager;
import java.util.ArrayList;
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

    public List<Edge> findByNodes(Node start, Node end){
        EntityManager manager = this.managerFactory.createEntityManager();
        List<Edge> temp = new ArrayList<>();
        System.out.println("start: " + start.getId() + " end: " + end.getId());
        temp.add(manager.createQuery(
                "SELECT e FROM Edge e WHERE e.start = :start AND e.end = :end", Edge.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult());
        temp.add(manager.createQuery(
                "SELECT e FROM Edge e WHERE e.start = :start AND e.end = :end", Edge.class)
                .setParameter("start", end)
                .setParameter("end", start)
                .getSingleResult());
        return temp;
    }
}
