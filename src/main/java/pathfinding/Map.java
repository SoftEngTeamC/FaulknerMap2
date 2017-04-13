package pathfinding;


import service.NodeService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class Map {
    private java.util.Map<Long, MapNode> nodeMap;
    private NodeService nodeService;

    public Map(Collection<model.Node> nodes) {
        nodeService = new NodeService();
        nodeMap = new HashMap<>();
        for (model.Node n : nodes) {
            nodeMap.put(n.getId(), new MapNode(n));
        }
        for (Long id : nodeMap.keySet()) {
            Set<model.Node> neighbors = nodeService.neighbors(id);
            for (model.Node n : neighbors) {
                if (n == null) continue;
                MapNode neighbor = nodeMap.get(n.getId());
                MapNode currentNode = nodeMap.get(id);
                if (neighbor == null || currentNode == null) continue;
                currentNode.addNeighbor(neighbor);
            }
        }
        // Connect all the elevators vertically

    }

    public MapNode getNode(Long id) {
        return nodeMap.get(id);
    }
}
