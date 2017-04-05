package pathfinding;


import java.util.*;

/**
 * This implementation is based off of Ben Ruijl's gist (https://gist.github.com/benruijl/3385624) and the wikipedia
 * page https://en.wikipedia.org/wiki/A*_search_algorithm
 */
public class PathFinder {
    private static double FEET_PER_PIXEL = 0.2902;
    private static double SECONDS_PER_FOOT = 0.2975;
    private static double STEPS_PER_FOOT = 0.5157;

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

    public static double getPixels(List<MapNode> myPath){
        int totalDistance = 0;
        for(int i = 0; i < myPath.size()-2;i++){ //Go until 1 before the end
            totalDistance += myPath.get(i).distanceTo(myPath.get(i+1));
        }
        return totalDistance;
    }
    public static double getFeet(List<MapNode> myPath){
        return getPixels(myPath)*FEET_PER_PIXEL;
    }
    public static double getSeconds(List<MapNode> myPath){
        return getFeet(myPath) * SECONDS_PER_FOOT;
    }
    public static double getSteps(List<MapNode> myPath){
        return getFeet(myPath) * STEPS_PER_FOOT;
    }
}