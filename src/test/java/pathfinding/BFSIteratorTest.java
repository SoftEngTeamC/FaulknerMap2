package pathfinding;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class BFSIteratorTest {
    private TestNode A, B, C, D, E, F, G, Z;
    @Before
    public void setUp() throws Exception {
        Z = new TestNode("Z");  // Isolated node
        A = new TestNode("A");
        B = new TestNode("B");
        C = new TestNode("C");
        D = new TestNode("D");
        E = new TestNode("E");
        F = new TestNode("F");
        G = new TestNode("G");

        A.addNeighbor(B);
        A.addNeighbor(C);
        A.addNeighbor(D);

        C.addNeighbor(E);
        C.addNeighbor(F);

        F.addNeighbor(G);
    }

    private void expectIteration(List<TestNode> expected, Iterator<TestNode> it) {
        List<TestNode> actual = new LinkedList<>();
        while (it.hasNext()) actual.add(it.next());
        assertEquals(expected, actual);
    }

    @Test
    public void BFSOfIsolatedNode() {
        List<TestNode> expectedIteration = new LinkedList<>();
        expectedIteration.add(Z);
        BFSIterator<TestNode> iterator = new BFSIterator<>(Z);
        expectIteration(expectedIteration, iterator);
    }

    @Test
    public void BFSOfFarthestNode() {
        BFSIterator<TestNode> iterator = new BFSIterator<>(A);
        TestNode last = null;
        while (iterator.hasNext()) last = iterator.next();
        assertEquals(G, last);
    }
}