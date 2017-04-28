package service;

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
public class CoordinateServiceTest {
    // Create services
    NodeService nodeService = new NodeService();
    CoordinateService coordinateService = new CoordinateService();
    EdgeService edgeService = new EdgeService();

    // Instantiate variable names
    double x, y, xB, yB; // x3, y3;
    Node testNodeA, testNodeB; // testNodeC;

    @Before
    public void setUp() throws Exception {
        EMFProvider.getInstance().useTest();
        // Diagram
        // NodeB(10,20)
        //     |
        //     |
        // NodeA(10,10)

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

        // Add coordinates to coordinate service
        coordinateService.persist(locationA);
        coordinateService.persist(locationB);

        // Add nodes to node service
        nodeService.persist(testNodeA);
        nodeService.persist(testNodeB);

        // Add edges to nodes
        Edge AtoB = new Edge(testNodeA, testNodeB, 10);

        // Add edges to edge service
        edgeService.persist(AtoB);;
    }

    @After
    public void tearDown() throws Exception {
    }

    // Tests - - - - -
    @Test
    public void find() throws Exception {
        try{
            coordinateService.find(testNodeA.getId());
        }catch(Exception E){
            Assert.fail("Exception " + E);
            E.printStackTrace();
        }
    }

    @Test
    public void getCoordinatesByFloor() throws Exception {
        try{
            coordinateService.getCoordinatesByFloor(2);
        }catch(Exception E){
            Assert.fail("Exception " + E);
            E.printStackTrace();
        }
    }

}