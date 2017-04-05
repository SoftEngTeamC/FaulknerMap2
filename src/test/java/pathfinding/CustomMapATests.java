package pathfinding;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by sam on 4/4/17.
 */
public class CustomMapATests {
    MapNode A, B, C, D, E, F, G;

    @Before
    public void setUp() throws Exception {
        // The nodes are arranged in a generic star
        // shape as shown bellow where:
        // A connects to D, C
        // B connects to E, D
        // C connects to E, A, G
        // D connects to B, A
        // E connects to B, C
        // F connects to

        //                 A
        //                 ^
        //               |  |
        //       E------------------B        F
        //          \   |    |   /
        //             \      /
        //             | \  / |
        //            |        |
        //            | /    \ |
        //           |/        \|
        //          D            C-----------G

        this.A = new MapNode(new Coordinate(4, 8, 1));
        this.B = new MapNode(new Coordinate(8, 5, 1));
        this.C = new MapNode(new Coordinate(5, 0, 1));
        this.D = new MapNode(new Coordinate(3, 0, 1));
        this.E = new MapNode(new Coordinate(0, 5, 1));
        this.F = new MapNode(new Coordinate(12, 5, 1));
        this.G = new MapNode(new Coordinate(12, 0, 1));
        this.A.addNeighbor(C);
        this.A.addNeighbor(D);
        this.B.addNeighbor(E);
        this.B.addNeighbor(D);
        this.C.addNeighbor(E);
        this.C.addNeighbor(G);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    //test the path when the destination node is an immediate
    //neighbor of the start node
    public void immediateNeighbor() throws Exception {
        List<MapNode> path = new LinkedList<>();
        path.add(A);
        path.add(D);
        assertEquals(path, PathFinder.shortestPath(A, D));
    }

    @Test
    //test the path when the destination node is nearby
    //but not an immediate neighbor
    public void goOutToComeIn() throws Exception {
        List<MapNode> path = new LinkedList<>();
        path.add(D);
        path.add(A);
        path.add(C);
        assertEquals(path, PathFinder.shortestPath(D, C));
    }

    @Test
    //test the path when more than one node must be passed through
    //in order to reach the destination
    public void goSeveralNodes() throws Exception {
        List<MapNode> path = new LinkedList<>();
        path.add(D);
        path.add(A);
        path.add(C);
        assertEquals(path, PathFinder.shortestPath(D, C));
    }

    @Test
    //test the path when no path is possible
    public void goToIsolated() throws Exception {
        List<MapNode> path = new LinkedList<>();
        path.add(C);
        path.add(A);
        path.add(D);
        path.add(B);
        assertNull(PathFinder.shortestPath(C, B));
    }

    @Test
    //test the path when it requires leaving the interconnected
    //structure and shooting off a branch
    public void goToBranch() throws Exception {
        List<MapNode> path = new LinkedList<>();
        path.add(D);
        path.add(A);
        path.add(C);
        path.add(G);
        assertEquals(path, PathFinder.shortestPath(D, G));
    }

    @Test
    //test the path when the start point
    //is also the destination
    public void goToSelf() throws Exception {
        List<MapNode> path = new LinkedList<>();
        path.add(D);
        path.add(D);
        assertEquals(path, PathFinder.shortestPath(D, D));
    }
}