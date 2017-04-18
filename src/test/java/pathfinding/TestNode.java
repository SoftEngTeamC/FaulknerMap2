package pathfinding;


import java.util.HashSet;
import java.util.Set;

public class TestNode implements Node<TestNode> {
    Set<TestNode> neighbors = new HashSet<>();
    String name;

    public TestNode(String name) {
        this.name = name;
    }

    @Override
    public double heuristicCost(TestNode goal) {
        return 0;
    }

    @Override
    public double traversalCost(TestNode neighbor) {
        return 0;
    }

    @Override
    public Set<TestNode> neighbors() {
        return neighbors;
    }

    public void addNeighbor(TestNode neighbor) {
        if (!neighbors.contains(neighbor)) {
            neighbors.add(neighbor);
            return;
        }
        neighbor.addNeighbor(this);
    }

    @Override
    public String toString() {
        return name;
    }
}
