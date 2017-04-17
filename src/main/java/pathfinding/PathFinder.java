package pathfinding;


import service.NodeService;

import java.util.*;

/**
 * This implementation is based off of Ben Ruijl's gist (https://gist.github.com/benruijl/3385624) and the wikipedia
 * page https://en.wikipedia.org/wiki/A*_search_algorithm
 */
public class PathFinder {

    public static <T extends Node<T>> List<T> shortestPath(T start, T goal) {
        Set<T> closed = new HashSet<>();
        java.util.Map<T, T> cameFrom = new HashMap<>();

        java.util.Map<T, Double> gScore = new HashMap<>();
        gScore.put(start, 0.0);

        final java.util.Map<T, Double> fScore = new HashMap<>();
        fScore.put(start, start.heuristicCost(goal));

        PriorityQueue<T> fringe = new PriorityQueue<>(32, Comparator.comparingDouble(fScore::get));
        fringe.add(start);

        while (!fringe.isEmpty()) {
            T currentNode = fringe.poll();

            if (currentNode.equals(goal)) return reconstructPath(currentNode, cameFrom);

            closed.add(currentNode);

            for (T neighbor : currentNode.neighbors()) {
                if (closed.contains(neighbor)) continue;

                double tentativeG = gScore.get(currentNode) + currentNode.traversalCost(neighbor);
                boolean onFringe = fringe.contains(neighbor);
                if (!onFringe || tentativeG < gScore.get(neighbor)) {
                    gScore.put(neighbor, tentativeG);
                    fScore.put(neighbor, tentativeG + neighbor.heuristicCost(goal));

                    if (onFringe) fringe.remove(neighbor);

                    fringe.offer(neighbor);
                    cameFrom.put(neighbor, currentNode);
                }
            }

        }
        return null;
    }

    private static <T extends Node<T>> List<T> reconstructPath(T end, java.util.Map<T, T> cameFrom) {
        LinkedList<T> path = new LinkedList<>();
        while (end != null) {
            path.addFirst(end);
            end = cameFrom.get(end);
        }
        return path;
    }

    private LinkedList<MapNode> depthFirstSearch(MapNode start, MapNode end) {
        NodeService NS = new NodeService();
        int numberOfNodes = NS.getAllNodes().size();
        return depthFirstSearchHelper(start, end, numberOfNodes);
    }

    private LinkedList<MapNode> depthFirstSearchHelper(MapNode start, MapNode end, int limit) {
        LinkedList<MapNode> path = new LinkedList<MapNode>();
        if(start.equals(end)) {
            path.add(end);
            return path;
        }
        if(limit == 0)
            return null;
        for(MapNode neighbor : start.neighbors()) {
            path = depthFirstSearchHelper(neighbor, end, limit - 1);
            if(path.getLast().equals(end)) {
                path.addFirst(start);
                return path;
            }
        }
        return null;
    }

    private LinkedList<MapNode> breadthFirstSearch(MapNode start, MapNode end) {
        HashMap<MapNode, LinkedList<MapNode>> paths = new HashMap<MapNode, LinkedList<MapNode>>();
        LinkedList<MapNode> newPath = new LinkedList<MapNode>();
        newPath.add(start);
        paths.put(start, newPath);
        for(MapNode node : paths.keySet()) {
            LinkedList<MapNode> path = paths.get(node);
            if(path.getLast().equals(end)) {
                return path;
            }
            paths.remove(node);
            paths = getNext(node, path, paths);
        }
        return null;
    }

    private HashMap<MapNode, LinkedList<MapNode>>
    getNext(MapNode node, LinkedList<MapNode> pathSoFar, HashMap<MapNode, LinkedList<MapNode>> paths) {
        for(MapNode neighbor : node.neighbors()) {
            pathSoFar.addLast(neighbor);
            paths.put(neighbor, pathSoFar);
            pathSoFar.removeLast();
        }
        return paths;
    }
}