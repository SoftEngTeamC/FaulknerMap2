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

    public Edge findByNodes(Node start, Node end){
        EntityManager manager = this.managerFactory.createEntityManager();
        Edge temp =  manager.createQuery("SELECT e FROM Edge e WHERE " +
                "e.start.id = :start AND e.end.id = :end", Edge.class)
                .setParameter("start", start.getId())
                .setParameter("end", end.getId())
                .getSingleResult();
        if(temp == null){
            temp =  manager.createQuery("SELECT e FROM Edge e WHERE " +
                    "e.start.id = :start AND e.end.id = :end", Edge.class)
                    .setParameter("start", end.getId())
                    .setParameter("end", start.getId())
                    .getSingleResult();
        }
        System.out.println("start: " + start.getId() + " end: " + end.getId());
        manager.close();
        return temp;
    }

}
