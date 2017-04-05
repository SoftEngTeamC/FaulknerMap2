package pathfinding;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by sam on 4/4/17.
 */
public class CustomMapBTests {
    MapNode A, B, C, D, E, F, G, H, K, J;

    @Before
    public void setUp() throws Exception {
        //    A --- B ------ J
        //   /     K  \  <-- MapNode K has no neighbors
        //  G -- F     \   ... <-Connects to J
        //  |   /       \ /
        //  |  E -- D -- C
        //  -----------H (MapNode G Connects to MapNode H directly (true path not shown))
        //MapNodes
        this.A = new MapNode(new Coordinate(2, 10, 1));
        this.B = new MapNode(new Coordinate(7, 10, 1));
        this.C = new MapNode(new Coordinate(10, 2, 1));
        this.D = new MapNode(new Coordinate(5, 2, 1));
        this.E = new MapNode(new Coordinate(2, 2, 1));
        this.F = new MapNode(new Coordinate(5, 5, 1));
        this.G = new MapNode(new Coordinate(0, 5, 1));
        this.H = new MapNode(new Coordinate(8, 0, 1));
        this.J = new MapNode(new Coordinate(15, 10, 1));
        //Lone MapNode
        this.K = new MapNode(new Coordinate(7, 7, 1));
        //Adding Neighbors
        this.A.addNeighbor(B);
        this.B.addNeighbor(C);
        this.C.addNeighbor(D);
        this.D.addNeighbor(E);
        this.E.addNeighbor(F);
        this.F.addNeighbor(G);
        this.G.addNeighbor(A);
        this.G.addNeighbor(H);
        this.B.addNeighbor(J);
        this.C.addNeighbor(J);
    }

    @After
    public void tearDown() throws Exception {

    }

    //Checks reverse directionality of addNeighbor()
    @Test
    public void testReversePath() throws Exception {
        List<MapNode> path = new LinkedList<>();
        path.add(B);
        path.add(A);
        assertEquals(PathFinder.shortestPath(B, A), path);
    }

    //Check node not connected to any other nodes
    @Test
    public void testShortestPathIsolated() throws Exception {
        assertNull(PathFinder.shortestPath(A, K));
    }

    //Tests to check adjacent nodes choices when determining the path
    @Test
    public void testShortJump() throws Exception {
        List<MapNode> path = new LinkedList<>();
        path.add(E);
        path.add(F);
        path.add(G);
        path.add(A);
        assertEquals(PathFinder.shortestPath(E, A), path);
    }

    //Tests to check adjacent nodes choices when determining the path
    @Test
    public void testLongJump() throws Exception {
        List<MapNode> path = new LinkedList<>();
        path.add(C);
        path.add(D);
        path.add(E);
        path.add(F);
        path.add(G);
        path.add(H);
        assertEquals(PathFinder.shortestPath(C, H), path);
    }

    //Tests to check adjacent nodes choices when determining the path
    @Test
    public void testHeuristicsPathChoice() throws Exception {
        List<MapNode> path = new LinkedList<>();
        path.add(F);
        path.add(G);
        path.add(A);
        path.add(B);
        path.add(J);
        assertEquals(PathFinder.shortestPath(F, J), path);
    }
}