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
                Edge tempEdge = new Edge(floor.get(j), floor.get(j+1), getEdgeLength(floor.get(j), floor.get(j+1)));
                persist(tempEdge);
            }
        }
    }

    public List<Edge> getAllEdges() {
        EntityManager manager = this.managerFactory.createEntityManager();
        return manager.createQuery("from Edge ", Edge.class)
                .getResultList();
    }

    public double getEdgeLength(Node from, Node end){
        if(from.getLocation().getX() == end.getLocation().getX()){
            return Math.abs(from.getLocation().getY()-end.getLocation().getY());
        }
        else if(from.getLocation().getY() == end.getLocation().getY()){
            return Math.abs(from.getLocation().getX()-end.getLocation().getX());
        }
        else {
            double yLen = Math.abs(from.getLocation().getY()-end.getLocation().getY());
            double xLen = Math.abs(from.getLocation().getX()-end.getLocation().getX());
            return Math.sqrt(yLen * yLen + xLen * xLen);
        }
    }
}
