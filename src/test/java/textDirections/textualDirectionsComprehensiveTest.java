package textDirections;

import model.Coordinate;
import model.Edge;
import model.Node;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pathfinding.MapNode;
import pathfinding.Path;
import service.CoordinateService;
import service.EMFProvider;
import service.EdgeService;
import service.NodeService;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by sam on 4/30/17.
 */
public class textualDirectionsComprehensiveTest {
    // Create services
    NodeService nodeService = new NodeService();
    CoordinateService coordinateService = new CoordinateService();
    EdgeService edgeService = new EdgeService();
    double x, y, x2, y2, x3, y3;
    Node testNodeA, testNodeB, testNodeC;
    MapNode A, B, C;
    private static boolean setUpIsDone = false;
    Edge AtoC, AtoB, BtoC;
    List<MapNode> path = new LinkedList<MapNode>();
    List<Step> steps = new LinkedList<Step>();
    Path testPath;

    @Before
    public void setUp() throws Exception {
        EMFProvider.getInstance().useTest();
        // Diagram
        // NodeB(10,20)--
        //     |        -NodeC(20,17)
        //     |         /
        //     |        /
        // Node A(10,10)

        // Create testNodeA
        double x = 10;
        double y = 10;
        int floor = 2;
        Coordinate locationA = new Coordinate(x, y, floor);
        String nameA = "testNodeA";
        testNodeA = new Node(nameA, locationA);

        // Create testNodeB
        double xB = 10;
        double yB = 20;
        int floorB = 2;
        Coordinate locationB = new Coordinate(xB, yB, floorB);
        String nameB = "testNodeB";
        testNodeB = new Node(nameB, locationB);

        // Create testNodeC
        double xC = 20;
        double yC = 17;
        int floorC = 2;
        Coordinate locationC = new Coordinate(xC, yC, floorC);
        String nameC = "testNodeC";
        testNodeC = new Node(nameC, locationC);

        // Add coordinates to coordinate service
        coordinateService.persist(locationA);
        coordinateService.persist(locationB);
        coordinateService.persist(locationC);

        // Add nodes to node service
        nodeService.persist(testNodeA);
        nodeService.persist(testNodeB);
        nodeService.persist(testNodeC);

        // Add edges to nodes
        AtoB = new Edge(testNodeA, testNodeB, 10);
        BtoC = new Edge(testNodeB, testNodeC, 10);
        AtoC = new Edge(testNodeA, testNodeC, 14);

        // Add edges to edge service
        edgeService.persist(AtoB);
        edgeService.persist(BtoC);
        edgeService.persist(AtoC);

        //Create MapNodes
        A = new MapNode(testNodeA);
        B = new MapNode(testNodeB);
        C = new MapNode(testNodeC);
    }

    @Test
    public void createPathFromService() throws Exception {
        // Add nodes to the path
        path.add(A);
        path.add(B);
        path.add(C);
        try{
            testPath = new Path(path);
            //System.out.print(testPath);
        }catch(Exception E){
            Assert.fail("Exception " + E);
            E.printStackTrace();
        }
    }

    @Test
    public void createDirectionsFromServicePath() throws Exception {
        // Add nodes to the path
        path.add(A);
        path.add(B);
        path.add(C);
        testPath = new Path(path);
        try{
            //TODO come back and fix this later
//            System.out.print(testPath);
//            steps = pathSteps(testPath);
        }catch(Exception E){
//            Assert.fail("Exception " + E);
            E.printStackTrace();
        }
    }
}
