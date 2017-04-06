package pathfinding;


import model.*;

import java.util.*;

public class Map {
    private java.util.Map<Long, MapNode> nodeMap;

    public Map(Collection<model.Node> nodes) {
        nodeMap = new HashMap<>();
        for (model.Node n : nodes) {
            nodeMap.put(n.getId(), new MapNode(n));
        }
        for (Long id : nodeMap.keySet()) {
            Set<model.Node> neighbors = new HashSet<>();
            for (model.Node n : neighbors) {
                if (n == null) continue;
                MapNode neighbor = nodeMap.get(n.getId());
                MapNode currentNode = nodeMap.get(id);
                if (neighbor == null || currentNode == null) continue;
                currentNode.addNeighbor(neighbor);
            }
        }
    }

    public MapNode getNode(Long id) {
        return nodeMap.get(id);
    }
}
