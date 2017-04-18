package service;


import model.Coordinate;
import model.Edge;
import model.Node;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.type.EntityType;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class NodeService extends AbstractService<Node> {
    @Override
    public Node find(Long id) {
        EntityManager manager = this.managerFactory.createEntityManager();
        return manager.find(Node.class, id);
    }

    public Set<Node> neighbors(Long id) {
        EntityManager manager = this.managerFactory.createEntityManager();
        List<Edge> edges = manager.createQuery("SELECT e FROM Edge e WHERE e.start.id = :id OR e.end.id = :id", Edge.class)
                .setParameter("id", id)
                .getResultList();
        Set<Node> startNeighbors = edges.stream().map(Edge::getStart).collect(Collectors.toSet());
        Set<Node> endNeighbors = edges.stream().map(Edge::getEnd).collect(Collectors.toSet());
        startNeighbors.addAll(endNeighbors);
        startNeighbors.remove(find(id));
        startNeighbors.remove(find(id));
        return startNeighbors;
    }

    public Node findNodeByName(String name) {
        EntityManager manager = this.managerFactory.createEntityManager();
        try {
            return manager.createQuery(
                    "SELECT n FROM Node n WHERE n.name LIKE :name", Node.class)
                    .setParameter("name", name)
                    .setMaxResults(1).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Node> findNodeIntersectionByFloor(int floor) {
        EntityManager manager = this.managerFactory.createEntityManager();
        return manager.createQuery(
                "SELECT n FROM Node n WHERE n.name LIKE :name", Node.class)
                .setParameter("name", "intersection" + floor + "%")
                .getResultList();
    }

    public List<Node> getAllNodes() {
        EntityManager manager = this.managerFactory.createEntityManager();
        return manager.createQuery("from Node", Node.class)
                .getResultList();
    }

    public List<Node> getNodesByFloor(int floor) {
        EntityManager manager = this.managerFactory.createEntityManager();

        return manager.createQuery("SELECT n FROM Node n," +
                " Coordinate c WHERE n.location.id = c.id AND " +
                "c.floor = :floor", Node.class)
                .setParameter("floor", floor)
            .getResultList();
    }

    public List<Node> getElevatorNodes() {
        EntityManager manager = this.managerFactory.createEntityManager();
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Node> elevatorCriteria = builder.createQuery(Node.class);
        Root<Node> root = elevatorCriteria.from(Node.class);
        elevatorCriteria.where(
                builder.like(
                        builder.lower(
                                root.get("name")
                        ),
                        "%elevator%"
                )
        );

        return manager.createQuery(elevatorCriteria).getResultList();
    }
}