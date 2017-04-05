package pathfinding;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class PathFinderTest {
    MapNode A, B, C, D, E;

    @Before
    public void setUp() throws Exception {
        // A --- B
        //  \   /       E (isolated)
        //   \ /
        //    C --- D
        this.A = new MapNode(new Coordinate(0, 5, 1));
        this.B = new MapNode(new Coordinate(3, 4, 1));
        this.C = new MapNode(new Coordinate(1, 0, 1));
        this.D = new MapNode(new Coordinate(6, 0, 1));
        this.E = new MapNode(new Coordinate(8, 3, 1));
        this.A.addNeighbor(B);
        this.A.addNeighbor(C);
        this.B.addNeighbor(C);
        this.C.addNeighbor(D);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void shortestPathIsolated() throws Exception {
        assertNull(PathFinder.shortestPath(A, E));
    }

    @Test
    public void shortestPathAdjacent() throws Exception {
        List<MapNode> path = new LinkedList<>();
        path.add(A);
        path.add(C);
        assertEquals(PathFinder.shortestPath(A, C), path);
    }

    @Test
    public void shortestPath() throws Exception {
        List<MapNode> path = new LinkedList<>();
        path.add(A);
        path.add(C);
        path.add(D);
        assertEquals(PathFinder.shortestPath(A, D), path);
    }

}