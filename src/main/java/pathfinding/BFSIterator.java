package pathfinding;

import java.util.*;

public class BFSIterator<T extends Node<T>> implements Iterator<T>  {
    private Set<T> visited = new HashSet<>();
    private Queue<T> queue = new LinkedList<>();

    public BFSIterator(T startNode) {
        this.visited.add(startNode);
        this.queue.add(startNode);
    }

    @Override
    public boolean hasNext() {
        return !this.queue.isEmpty();
    }

    @Override
    public T next() {
        T next = queue.remove();
        for (T neighbor : next.neighbors()) {
            if (!this.visited.contains(neighbor)) {
                this.queue.add(neighbor);
                this.visited.add(neighbor);
            }
        }
        return next;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
