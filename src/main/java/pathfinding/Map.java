package pathfinding;


import service.AlgorithmSingleton;
import service.NodeService;

import java.util.*;
import java.util.stream.Collectors;

public class Map {
    public enum algorithm { BFS, DFS, ASTAR }
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
    }


    public List<MapNode> shortestPath(MapNode start, MapNode end) {
        System.out.println(AlgorithmSingleton.getInstance().getCurrentAlgorithm());
        switch (AlgorithmSingleton.getInstance().getCurrentAlgorithm()) {
            case BFS: return PathFinder.BFS(start, end);
            case DFS: return PathFinder.DFS(start, end);
            case ASTAR: return PathFinder.shortestPath(start, end);
        }
        return null;
    }

    public MapNode getNode(Long id) {
        return nodeMap.get(id);
    }

    public static Set<Integer> floorsInPath(List<MapNode> path) {
        Set<Integer> allFloors = new HashSet<>();
        allFloors.add(1);
        allFloors.add(2);
        allFloors.add(3);
        allFloors.add(4);
        allFloors.add(5);
        allFloors.add(6);
        allFloors.add(7);
        Set<Integer> floors = new HashSet<>();
        floors.addAll(path.stream().map(n -> n.getLocation().getFloor()).collect(Collectors.toList()));
        allFloors.removeAll(floors);
        return allFloors;
    }
}
