package service;

import Singleton.EMFProvider;
import model.Coordinate;
import model.Edge;
import model.Node;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Sam on 4/27/2017.
 */
public class EdgeServiceTest {
    // Create services
    NodeService nodeService = new NodeService();
    CoordinateService coordinateService = new CoordinateService();
    EdgeService edgeService = new EdgeService();
    double x, y, x2, y2, x3, y3;
    Node testNodeA, testNodeB, testNodeC;
    Edge AtoC;

    @Before
    public void setUp() throws Exception {
        EMFProvider.getInstance().useTest();
        // Diagram
        //    NONE       NodeC(20,20)
        //               /
        //              /
        // Node A(10,10)

        // Create testNodeA
        double x = 10;
        double y = 10;
        int floor = 2;
        Coordinate locationA = new Coordinate(x, y, floor);
        String nameA = "testNodeA";
        testNodeA = new Node(nameA, locationA);

        // Create testNodeC
        double xC = 20;
        double yC = 20;
        int floorC = 2;
        Coordinate locationC = new Coordinate(xC, yC, floorC);
        String nameC = "testNodeC";
        testNodeC = new Node(nameC, locationC);

        // Add coordinates to coordinate service
        coordinateService.persist(locationA);
        coordinateService.persist(locationC);

        // Add nodes to node service
        nodeService.persist(testNodeA);
        nodeService.persist(testNodeC);

        // Add edges to nodes
        AtoC = new Edge(testNodeA, testNodeC, 14);

        // Add edges to edge service;
        edgeService.persist(AtoC);
    }

    @After
    public void tearDown() throws Exception {
    }

    // Tests - - - - - -
    @Test
    public void testFind() throws Exception {
        try{
            System.out.print(testNodeA);
            edgeService.find(testNodeA.getId());
        }catch(Exception E){
            Assert.fail("Exception " + E);
            E.printStackTrace();
        }
    }

    @Test
    public void testGetAllEdges() throws Exception {
        try{
            edgeService.getAllEdges();
        }catch(Exception E){
            Assert.fail("Exception " + E);
            E.printStackTrace();
        }
    }

    @Test
    public void testFindByNodes() throws Exception {
        try{
            edgeService.findByNodes(testNodeA, testNodeC);
        }catch(Exception E){
            Assert.fail("Exception " + E);
            E.printStackTrace();
        }
    }
}