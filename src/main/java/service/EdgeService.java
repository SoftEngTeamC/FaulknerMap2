package service;

import model.Edge;
import model.Node;

import javax.persistence.EntityManager;
import java.util.List;

public class EdgeService extends AbstractService<Edge> {
    @Override
    public Edge find(Long id) {
        EntityManager manager = this.managerFactory.createEntityManager();
        return manager.find(Edge.class, id);
    }

    public void addEdgeIntersections(){
        NodeService ns = new NodeService();
        for(int i = 1; i < 8; i ++){
            List<Node> floor = ns.findNodeIntersectionByFloor(i);
            for(int j = 0; j < floor.size(); j ++){
                Edge tempEdge = new Edge(floor.get(i), floor.get(i+1), 0);
                persist(tempEdge);
            }
        }
    }

    public List<Edge> getAllEdges() {
        EntityManager manager = this.managerFactory.createEntityManager();
        return manager.createQuery("from Edge ", Edge.class)
                .getResultList();
    }
}
