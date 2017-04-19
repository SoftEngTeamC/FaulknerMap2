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
                "SELECT e FROM Edge e WHERE e.start.id = :start AND e.end.id = :end", Edge.class)
                .setParameter("start", start.getId())
                .setParameter("end", end.getId())
                .getSingleResult());
        System.out.println("getting a list of edge ");
        temp.add(manager.createQuery(
                "SELECT e FROM Edge e WHERE e.start.id = :start AND e.end.id = :end", Edge.class)
                .setParameter("start", end.getId())
                .setParameter("end", start.getId())
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
