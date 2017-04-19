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

    public List<Edge> findByNodes(Node start, Node end){
        EntityManager manager = this.managerFactory.createEntityManager();
        List<Edge> temp = new ArrayList<>();
        System.out.println("start: " + start.getId() + " end: " + end.getId());
        temp.add(manager.createQuery(
                "SELECT e FROM Edge e WHERE e.start = :start AND e.end = :end", Edge.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult());
        System.out.println("getting a list of edge ");
        temp.add(manager.createQuery(
                "SELECT e FROM Edge e WHERE e.start = :start AND e.end = :end", Edge.class)
                .setParameter("start", end)
                .setParameter("end", start)
                .getSingleResult());
        manager.close();
        return temp;
    }

    public void disableEdge(Edge temp){
        System.out.println("GO into disable function");
        temp.setDisabled(true);

    }

    public void ableEdge(Edge temp){
        System.out.println("GO into disable function");
        temp.setDisabled(false);
    }
}
