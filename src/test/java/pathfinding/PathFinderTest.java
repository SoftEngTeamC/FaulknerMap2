package pathfinding;

import model.Coordinate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PathFinderTest {
    MapNode A, B, C, D, E, F, G, H, I, J, K, L;

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

        // The nodes are arranged in a generic star
        // shape as shown bellow where:
        // F connects to I, H
        // G connects to J, I
        // H connects to J, F, L
        // I connects to G, F
        // J connects to G, H
        // K connects to

        //                 F
        //                 ^
        //               |  |
        //       J------------------G        K
        //          \   |    |   /
        //             \      /
        //             | \  / |
        //            |        |
        //            | /    \ |
        //           |/        \|
        //          I            H-----------L

        this.F = new MapNode(new Coordinate(4, 8, 1));
        this.G = new MapNode(new Coordinate(8, 5, 1));
        this.H = new MapNode(new Coordinate(5, 0, 1));
        this.I = new MapNode(new Coordinate(3, 0, 1));
        this.J = new MapNode(new Coordinate(0, 5, 1));
        this.K = new MapNode(new Coordinate(12, 5, 1));
        this.L = new MapNode(new Coordinate(12, 0, 1));
        this.F.addNeighbor(H);
        this.F.addNeighbor(I);
        this.G.addNeighbor(J);
        this.G.addNeighbor(I);
        this.H.addNeighbor(J);
        this.H.addNeighbor(L);
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

    @Test
    //test the path when the destination node is an immediate
    //neighbor of the start node
    public void immediateNeighbor() throws Exception {
        List<MapNode> path = new LinkedList<>();
        path.add(F);
        path.add(I);
        assertEquals(path, PathFinder.shortestPath(F, I));
    }

    @Test
    //test the path when the destination node is nearby
    //but not an immediate neighbor
    public void goOutToComeIn() throws Exception {
        List<MapNode> path = new LinkedList<>();
        path.add(I);
        path.add(F);
        path.add(H);
        assertEquals(path, PathFinder.shortestPath(I, H));
    }

    @Test
    //test the path when more than one node must be passed through
    //in order to reach the destination
    public void goSeveralNodes() throws Exception {
        List<MapNode> path = new LinkedList<>();
        path.add(I);
        path.add(F);
        path.add(H);
        assertEquals(path, PathFinder.shortestPath(I, H));
    }

    @Test
    //test the path when no path is possible
    public void goToIsolated() throws Exception {
        assertNull(PathFinder.shortestPath(H, K));
    }

    @Test
    //test the path when it requires leaving the interconnected
    //structure and shooting off a branch
    public void goToBranch() throws Exception {
        List<MapNode> path = new LinkedList<>();
        path.add(I);
        path.add(F);
        path.add(H);
        path.add(L);
        assertEquals(path, PathFinder.shortestPath(I, L));
    }

    @Test
    //test the path when the start point
    //is also the destination
    public void goToSelf() throws Exception {
        List<MapNode> path = new LinkedList<>();
        path.add(I);
        assertEquals(path, PathFinder.shortestPath(I, I));
    }

}