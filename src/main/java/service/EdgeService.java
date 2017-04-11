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
            for(int j = 0; j < floor.size()-1; j ++){
                Edge tempEdge = new Edge(floor.get(j), floor.get(j+1), getEdgeLength(floor.get(j), floor.get(j+1)));
                this.persist(tempEdge);
            }
        }
    }

    private static double getEdgeLength(Node from, Node end){
        double yLen = from.getLocation().getY() - end.getLocation().getY();
        double xLen = from.getLocation().getX() - end.getLocation().getX();
        return Math.sqrt(yLen * yLen + xLen * xLen);
    }

    public List<Edge> getAllEdges() {
        EntityManager manager = this.managerFactory.createEntityManager();
        return manager.createQuery("from Edge ", Edge.class)
                .getResultList();
    }

}
