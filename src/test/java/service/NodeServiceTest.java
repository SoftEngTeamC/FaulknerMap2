package service;


import model.Coordinate;
import model.Edge;
import model.Node;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NodeServiceTest {

    // Create services
    NodeService nodeService = new NodeService();
    CoordinateService coordinateService = new CoordinateService();
    EdgeService edgeService = new EdgeService();
    double x, y, x2, y2, x3, y3;
    Node testNodeA, testNodeB, testNodeC;

    // Set Up and Tear Down
    @Before
    public void setUp() throws Exception {
        EMFProvider.getInstance().useTest();
        // Diagram
        // NodeB(10,20)---NodeC(20,20)
        //     |         /
        //     |        /
        // Node A(10,10)

        // Create testNodeA
        double x = 10;
        double y = 10;
        int floor = 2;
        Coordinate locationA = new Coordinate(x, y, floor);
        String nameA = "testNodeA";
        Node testNodeA = new Node(nameA, locationA);

        // Create testNodeB
        double xB = 10;
        double yB = 20;
        int floorB = 2;
        Coordinate locationB = new Coordinate(xB, yB, floorB);
        String nameB = "testNodeB";
        Node testNodeB = new Node(nameB, locationB);

        // Create testNodeC
        double xC = 20;
        double yC = 20;
        int floorC = 2;
        Coordinate locationC = new Coordinate(xC, yC, floorC);
        String nameC = "testNodeC";
        Node testNodeC = new Node(nameC, locationC);

        // Add coordinates to coordinate service
        coordinateService.persist(locationA);
        coordinateService.persist(locationB);
        coordinateService.persist(locationC);

        // Add nodes to node service
        nodeService.persist(testNodeA);
        nodeService.persist(testNodeB);
        nodeService.persist(testNodeC);

        // Add edges to nodes
        Edge AtoB = new Edge(testNodeA, testNodeB, 10);
        Edge BtoC = new Edge(testNodeB, testNodeC, 10);
        Edge AtoC = new Edge(testNodeA, testNodeC, 14);

        // Add edges to edge service
        edgeService.persist(AtoB);
        edgeService.persist(BtoC);
        edgeService.persist(AtoC);
    }

    @After
    public void tearDown() throws Exception {
    }

    // Tests
    @Test
    public void addNodeToService() throws Exception {
        double x = 10;
        double y = 10;
        int floor = 4;
        Coordinate locationD = new Coordinate(x, y, floor);
        String nameD = "testNodeD";
        Node testNodeD = new Node(nameD, locationD);
        coordinateService.persist(locationD);
        try{
            nodeService.persist(testNodeD);
        }catch(Exception E){
            Assert.fail("Exception " + E);
            E.printStackTrace();
        }
    }

    @Test
    public void removeNodeFromService() throws Exception {
        double x = 10;
        double y = 10;
        int floor = 3;
        Coordinate locationE = new Coordinate(x, y, floor);
        String nameE = "testNodeE";
        Node testNodeE = new Node(nameE, locationE);
        coordinateService.persist(locationE);
        nodeService.persist(testNodeE);
        try{
            nodeService.remove(testNodeE);
        }catch(Exception E){
            Assert.fail("Exception " + E);
            E.printStackTrace();
        }
    }

    @Test
    public void find() throws Exception {
        double x = 10;
        double y = 10;
        int floor = 3;
        Coordinate locationF = new Coordinate(x, y, floor);
        String nameF = "testNodeF";
        Node testNodeF = new Node(nameF, locationF);
        coordinateService.persist(locationF);
        nodeService.persist(testNodeF);

        try{
            nodeService.find(testNodeF.getId());
        }catch(Exception E){
            Assert.fail("Exception " + E);
            E.printStackTrace();
        }
    }


    @Test
    public void testNeighbors() throws Exception {
        try{
            nodeService.neighbors(testNodeB.getId());
        }catch(Exception E){
            Assert.fail("Exception " + E);
            E.printStackTrace();
        }
    }

    @Test
    public void testFindByName() throws Exception{
        try{
            Assert.assertEquals(testNodeC, nodeService.findNodeByName("testNodeC"));
        }catch(Exception E){
            Assert.fail("Exception " + E);
            E.printStackTrace();
        }
    }

    @Test
    public void findNodeByNameAndFloor() throws Exception {
    }

    @Test
    public void findNodeIntersectionByFloor() throws Exception {
    }

    @Test
    public void getAllNodes() throws Exception {
    }

    @Test
    public void getNodesByFloor() throws Exception {
    }

    @Test
    public void getElevatorNodes() throws Exception {
    }
}
