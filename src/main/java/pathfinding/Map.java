package pathfinding;

import db.dbClasses.Node;
import db.dbHelpers.EdgesHelper;
import db.dbHelpers.NodesHelper;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class Map {
    private java.util.Map<UUID, MapNode> nodeMap;

    public Map(Collection<Node> dbNodes) {
        nodeMap = new HashMap<>();
        for (Node n : dbNodes) {
            nodeMap.put(n.getId(), new MapNode(n));
        }
        for (UUID id : nodeMap.keySet()) {
        //    System.out.println(id);
            for (Node n : EdgesHelper.getNeighbors(NodesHelper.getNodeByID(id))) {
                if (n == null) continue;
                MapNode neighbor = nodeMap.get(n.getId());
                MapNode currentNode = nodeMap.get(id);
                if (neighbor == null || currentNode == null) continue;
                currentNode.addNeighbor(neighbor);
            }
        }
    }

    public MapNode getNode(UUID id) {
        return nodeMap.get(id);
    }
}
