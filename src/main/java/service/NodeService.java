package service;


import model.HospitalService;
import model.Node;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class NodeService  extends AbstractService<Node> {
    @Override
    public Node find(Long id) {
        EntityManager manager = this.managerFactory.createEntityManager();
        return manager.find(Node.class, id);
    }

    private List<Node> neighbors() {
        EntityManager manager = this.managerFactory.createEntityManager();
        return manager.createQuery("from Node", Node.class).getResultList();
    }

    public Node findNodeByName(String name) {
        EntityManager manager = this.managerFactory.createEntityManager();
        return (Node) manager.createQuery(
                "SELECT n FROM Node n WHERE n.name LIKE :name")
                .setParameter("name", name)
                .setMaxResults(1).getSingleResult();
    }

    public List<Node> getAllNodes() {
        EntityManager manager = this.managerFactory.createEntityManager();
        return manager.createQuery("from Node", Node.class)
                .getResultList();
    }
}
