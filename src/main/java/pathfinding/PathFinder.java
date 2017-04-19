package pathfinding;


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

            if (currentNode.equals(goal)) {
                System.out.println("Goal Node: " + goal);
                return reconstructPath(currentNode, cameFrom);
            }

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

    public static <T extends Node<T>> List<T> BFS(T start, T goal) {
        BFSIterator<T> iterator = new BFSIterator<>(start);
        java.util.Map<T, T> cameFrom = new HashMap<>();
        T prev = null;
        for (BFSIterator<T> it = iterator; it.hasNext();) {
            T n = it.next();
            cameFrom.put(n, prev);
            if (n.equals(goal)) return reconstructPath(n, cameFrom);
            prev = n;
        }
        return null;
    }

    public static <T extends Node<T>> List<T> DFS(T start, T goal) {
        DFSIterator<T> iterator = new DFSIterator<>(start);
        java.util.Map<T, T> cameFrom = new HashMap<>();
        T prev = null;
        for (DFSIterator<T> it = iterator; it.hasNext();) {
            T n = it.next();
            cameFrom.put(n, prev);
            if (n.equals(goal)) return reconstructPath(n, cameFrom);
            prev = n;
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
}