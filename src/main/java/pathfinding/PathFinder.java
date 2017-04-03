package pathfinding;


import java.util.*;

public class PathFinder {
    public static <T extends Node<T>> List<T> shortestPath(T start, T goal) {
        Set<T> closed = new HashSet<T>();
        Map<T, T> cameFrom = new HashMap<T, T>();

        Map<T, Double> gScore = new HashMap<T, Double>();
        gScore.put(start, 0.0);

        final Map<T, Double> fScore = new HashMap<T, Double>();
        fScore.put(start, start.heuristicCost(goal));

        PriorityQueue<T> fringe = new PriorityQueue<T>(32, (nodeA, nodeB) -> Double.compare(fScore.get(nodeA), fScore.get(nodeB)));
        fringe.add(start);

        while (!fringe.isEmpty()) {
            T currentNode = fringe.poll();

            if (currentNode.equals(goal)) return reconstructPath(currentNode, cameFrom);

            closed.add(currentNode);

            for (T neighbor : currentNode.neighbors()) {
                if (closed.contains(neighbor)) continue;
                double tentativeG = gScore.get(neighbor) + currentNode.traversalCost(neighbor);
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

    private static <T extends Node<T>> List<T> reconstructPath(T end, Map<T, T> cameFrom) {
        LinkedList<T> path = new LinkedList<T>();
        while (end != null) {
            path.addFirst(end);
            end = cameFrom.get(end);
        }
        return path;
    }
}