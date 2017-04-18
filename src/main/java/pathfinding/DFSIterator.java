package pathfinding;

import java.util.*;

public class DFSIterator<T extends Node<T>> implements Iterator<T>  {
    private Set<T> visited = new HashSet<>();
    private Deque<T> stack = new LinkedList<>();

    public DFSIterator(T startNode) {
        this.visited.add(startNode);
        this.stack.add(startNode);
    }

    @Override
    public boolean hasNext() {
        return !this.stack.isEmpty();
    }

    @Override
    public T next() {
        T next = stack.remove();
        for (T neighbor : next.neighbors()) {
            if (!this.visited.contains(neighbor)) {
                this.stack.add(neighbor);
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
