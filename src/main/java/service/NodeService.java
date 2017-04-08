package service;


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
        String sql = "SELECT n FROM Node n where n.name = :name";
        TypedQuery<Node> query = (TypedQuery<Node>) manager.createQuery(sql);
        query.setParameter("name", name);
        return query.getSingleResult();
    }
}
