package pathfinding;

import java.util.Set;


public interface Node<T> {
    double heuristicCost(T goal);
    double traversalCost(T neighbor);
    Set<T> neighbors();
}
