package pathfinding;
import java.util.*;


public class PathFinder {
    /**
     * Calculate the shortest path from the start node to the end node
     * @param start the starting node
     * @param destination the destination node
     * @return the shortest path from start to destination
     */
    public List<MapNode> shortestPath(MapNode start, MapNode destination) throws Exception {
        Set<MapNode> closedSet = new HashSet<>(); // Nodes that have already been looked at
        Set<MapNode> openSet = new HashSet<>(); // Candidate nodes
        openSet.add(start);

        Map<MapNode, Double> nodeCost = new HashMap<>();  // The cost of getting to each node from the start
        nodeCost.put(start, 0.0);

        Map<MapNode, Double> heuristicCost = new HashMap<>();
        heuristicCost.put(start, start.heuristicDistanceTo(destination));

        Map<MapNode, MapNode> cameFrom = new HashMap<>();

        while (!openSet.isEmpty()) {
            // Get the node with the minimum f score
            MapNode currentNode = null;
            for (MapNode n : openSet) {
                if (currentNode == null) {
                    currentNode = n;
                    continue;
                }
                double bestFScore = nodeCost.get(currentNode) + heuristicCost.get(currentNode);
                double fScore = nodeCost.get(n) + heuristicCost.get(n);
                if (fScore < bestFScore) {
                    currentNode = n;
                }
            }
            if (currentNode == destination) return backTrack(cameFrom, currentNode);

            // Move the current node to the closed set
            openSet.remove(currentNode);
            closedSet.add(currentNode);

            for (MapNode n : currentNode.getNeighbors()) {
                if (closedSet.contains(n)) continue;
                double tentativeGScore = heuristicCost.get(currentNode) + currentNode.distanceTo(n);
                if (!openSet.contains(n)) {
                    openSet.add(n);
                } else if (tentativeGScore >= heuristicCost.get(n)) {
                    continue;
                }
                cameFrom.put(n, currentNode);
                heuristicCost.put(n, tentativeGScore);
                nodeCost.put(n, heuristicCost.get(n) + n.distanceTo(destination));
            }
        }
        throw new Exception("Path not found.");
    }

    /**
     * Back track from the destination node to the starting node along the shortest path
     * @param cameFrom a map containing the node prior to each node in the shortest path
     * @param current the node to backtrack from
     * @return the shortest path
     */
    private List<MapNode> backTrack(Map<MapNode, MapNode> cameFrom, MapNode current) {
        List<MapNode> path = new LinkedList<>();
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            path.add(current);
        }
        return path;
    }
}