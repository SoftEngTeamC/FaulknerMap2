package service;


import model.Node;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class NodeService  extends AbstractService<Node> {
    public NodeService(EntityManagerFactory emf) {
        super(emf);
    }

    @Override
    public Node find(Long id) {
        EntityManager manager = this.managerFactory.createEntityManager();
        return manager.find(Node.class, id);
    }
}
