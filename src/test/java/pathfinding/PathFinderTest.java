//package pathfinding;
//
//import model.Coordinate;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.LinkedList;
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNull;
//
//public class PathFinderTest {
//    private MapNode A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V;
//
//    @Before
//    public void setUp() throws Exception {
//        // A --- B
//        //  \   /       E (isolated)
//        //   \ /
//        //    C --- D
//        this.A = new MapNode(new Coordinate(0, 5, 1));
//        this.B = new MapNode(new Coordinate(3, 4, 1));
//        this.C = new MapNode(new Coordinate(1, 0, 1));
//        this.D = new MapNode(new Coordinate(6, 0, 1));
//        this.E = new MapNode(new Coordinate(8, 3, 1));
//        this.A.addNeighbor(B);
//        this.A.addNeighbor(C);
//        this.B.addNeighbor(C);
//        this.C.addNeighbor(D);
//
//
//        // The nodes are arranged in a generic star
//        // shape as shown bellow where:
//        // F connects to I, H
//        // G connects to J, I
//        // H connects to J, F, L
//        // I connects to G, F
//        // J connects to G, H
//        // K connects to
//
//        //                 F
//        //                 ^
//        //               |  |
//        //       J------------------G        K
//        //          \   |    |   /
//        //             \      /
//        //             | \  / |
//        //            |        |
//        //            | /    \ |
//        //           |/        \|
//        //          I            H-----------L
//
//        this.F = new MapNode(new Coordinate(4, 8, 1));
//        this.G = new MapNode(new Coordinate(8, 5, 1));
//        this.H = new MapNode(new Coordinate(5, 0, 1));
//        this.I = new MapNode(new Coordinate(3, 0, 1));
//        this.J = new MapNode(new Coordinate(0, 5, 1));
//        this.K = new MapNode(new Coordinate(12, 5, 1));
//        this.L = new MapNode(new Coordinate(12, 0, 1));
//        this.F.addNeighbor(H);
//        this.F.addNeighbor(I);
//        this.G.addNeighbor(J);
//        this.G.addNeighbor(I);
//        this.H.addNeighbor(J);
//        this.H.addNeighbor(L);
//
//
//        //    M --- N ------ V
//        //   /     U  \  <-- MapNode U has no neighbors
//        //  S -- R     \   ... <-Connects to V
//        //  |   /       \ /
//        //  |  Q -- P -- O
//        //  -----------T (MapNode S Connects to MapNode T directly (true path not shown))
//        //MapNodes
//        this.M = new MapNode(new Coordinate(2, 10, 1));
//        this.N = new MapNode(new Coordinate(7, 10, 1));
//        this.O = new MapNode(new Coordinate(10, 2, 1));
//        this.P = new MapNode(new Coordinate(5, 2, 1));
//        this.Q = new MapNode(new Coordinate(2, 2, 1));
//        this.R = new MapNode(new Coordinate(5, 5, 1));
//        this.S = new MapNode(new Coordinate(0, 5, 1));
//        this.T = new MapNode(new Coordinate(8, 0, 1));
//        this.V = new MapNode(new Coordinate(15, 10, 1));
//        //Lone MapNode
//        this.U = new MapNode(new Coordinate(7, 7, 1));
//        //Adding Neighbors
//        this.M.addNeighbor(N);
//        this.N.addNeighbor(O);
//        this.O.addNeighbor(P);
//        this.P.addNeighbor(Q);
//        this.Q.addNeighbor(R);
//        this.R.addNeighbor(S);
//        this.S.addNeighbor(M);
//        this.S.addNeighbor(T);
//        this.N.addNeighbor(V);
//        this.O.addNeighbor(V);
//    }
//
//    @After
//    public void tearDown() throws Exception {
//
//    }
//
//    @Test
//    public void shortestPathIsolated() throws Exception {
//        assertNull(PathFinder.shortestPath(A, E));
//    }
//
//    @Test
//    public void shortestPathAdjacent() throws Exception {
//        List<MapNode> path = new LinkedList<>();
//        path.add(A);
//        path.add(C);
//        assertEquals(PathFinder.shortestPath(A, C), path);
//    }
//
//    @Test
//    public void shortestPath() throws Exception {
//        List<MapNode> path = new LinkedList<>();
//        path.add(A);
//        path.add(C);
//        path.add(D);
//        assertEquals(PathFinder.shortestPath(A, D), path);
//    }
//
//    @Test
//    //test the path when the destination node is an immediate
//    //neighbor of the start node
//    public void immediateNeighbor() throws Exception {
//        List<MapNode> path = new LinkedList<>();
//        path.add(F);
//        path.add(I);
//        assertEquals(path, PathFinder.shortestPath(F, I));
//    }
//
//    @Test
//    //test the path when the destination node is nearby
//    //but not an immediate neighbor
//    public void goOutToComeIn() throws Exception {
//        List<MapNode> path = new LinkedList<>();
//        path.add(I);
//        path.add(F);
//        path.add(H);
//        assertEquals(path, PathFinder.shortestPath(I, H));
//    }
//
//    @Test
//    //test the path when more than one node must be passed through
//    //in order to reach the destination
//    public void goSeveralNodes() throws Exception {
//        List<MapNode> path = new LinkedList<>();
//        path.add(I);
//        path.add(F);
//        path.add(H);
//        assertEquals(path, PathFinder.shortestPath(I, H));
//    }
//
//    @Test
//    //test the path when no path is possible
//    public void goToIsolated() throws Exception {
//        assertNull(PathFinder.shortestPath(H, K));
//    }
//
//    @Test
//    //test the path when it requires leaving the interconnected
//    //structure and shooting off a branch
//    public void goToBranch() throws Exception {
//        List<MapNode> path = new LinkedList<>();
//        path.add(I);
//        path.add(F);
//        path.add(H);
//        path.add(L);
//        assertEquals(path, PathFinder.shortestPath(I, L));
//    }
//
//    @Test
//    //test the path when the start point
//    //is also the destination
//    public void goToSelf() throws Exception {
//        List<MapNode> path = new LinkedList<>();
//        path.add(I);
//        assertEquals(path, PathFinder.shortestPath(I, I));
//    }
//
//    //Checks reverse directionality of addNeighbor()
//    @Test
//    public void testReversePath() throws Exception {
//        List<MapNode> path = new LinkedList<>();
//        path.add(N);
//        path.add(M);
//        assertEquals(PathFinder.shortestPath(N, M), path);
//    }
//
//    //Check node not connected to any other nodes
//    @Test
//    public void testShortestPathIsolated() throws Exception {
//        assertNull(PathFinder.shortestPath(M, U));
//    }
//
//    //Tests to check adjacent nodes choices when determining the path
//    @Test
//    public void testShortJump() throws Exception {
//        List<MapNode> path = new LinkedList<>();
//        path.add(Q);
//        path.add(R);
//        path.add(S);
//        path.add(M);
//        assertEquals(PathFinder.shortestPath(Q, M), path);
//    }
//
//    //Tests to check adjacent nodes choices when determining the path
//    @Test
//    public void testLongJump() throws Exception {
//        List<MapNode> path = new LinkedList<>();
//        path.add(O);
//        path.add(P);
//        path.add(Q);
//        path.add(R);
//        path.add(S);
//        path.add(T);
//        assertEquals(PathFinder.shortestPath(O, T), path);
//    }
//
//    //Tests to check adjacent nodes choices when determining the path
//    @Test
//    public void testHeuristicsPathChoice() throws Exception {
//        List<MapNode> path = new LinkedList<>();
//        path.add(R);
//        path.add(Q);
//        path.add(P);
//        path.add(O);
//        path.add(V);
//        assertEquals(PathFinder.shortestPath(R, V), path);
//    }
//}