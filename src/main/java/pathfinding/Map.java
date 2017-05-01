package pathfinding;


import model.Edge;
import service.AlgorithmSingleton;
import service.EdgeService;
import service.NodeService;

import java.util.*;
import java.util.stream.Collectors;

public class Map {
    public enum algorithm { BFS, DFS, ASTAR }
    private java.util.Map<Long, MapNode> nodeMap;

    public Map(Collection<model.Node> nodes, boolean disabled) {
        NodeService nodeService = new NodeService();
        EdgeService edgeService = new EdgeService();
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
                if(disabled) {
                    Edge maybeDisabledEdge = edgeService.findByNodes(nodeService.find(id), nodeService.find(n.getId()));
                    if (maybeDisabledEdge == null || maybeDisabledEdge.isDisabled()) continue;
                    if (neighbor == null || currentNode == null) continue;
                }
                currentNode.addNeighbor(neighbor);
            }
        }
    }

    public Path shortestPath(model.Node start, model.Node end) {
        return shortestPath(nodeMap.get(start.getId()), nodeMap.get(end.getId()));
    }

    public Path shortestPath(MapNode start, MapNode end) {
//        System.out.println(AlgorithmSingleton.getInstance().getCurrentAlgorithm());
        switch (AlgorithmSingleton.getInstance().getCurrentAlgorithm()) {
            case BFS: return new Path(PathFinder.BFS(start, end));
            case DFS: return new Path(PathFinder.DFS(start, end));
            case ASTAR: return new Path(PathFinder.shortestPath(start, end));
        }
        return null; // TODO: Probably throw an exception
    }

    public MapNode getNode(Long id) {
        return nodeMap.get(id);
    }

    public Collection<MapNode> nodesOnFloor(int floor) {
        System.out.println("ALl nodes on floor" + floor);
        return nodeMap.values().stream()
                .filter(mapNode -> mapNode.getLocation().getFloor() == floor)
                .collect(Collectors.toList());
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
