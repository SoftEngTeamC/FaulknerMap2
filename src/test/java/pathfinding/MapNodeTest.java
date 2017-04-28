package pathfinding;

import model.Coordinate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MapNodeTest {
    private MapNode node1, node2, node3;
    List<MapNode> myPath = new LinkedList<>();

    @Before
    public void setUp() throws Exception {
        // Instantiate nodes
        this.node1 = new MapNode(new Coordinate(0,0,1));
        this.node2 = new MapNode(new Coordinate(10,0,1));
        this.node3 = new MapNode(new Coordinate(10,10,1));

        // Add nodes to list
        myPath.add(node1);
        myPath.add(node2);
        myPath.add(node3);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetNeighborsLoneNode() throws Exception {
        Collection<MapNode> neighbors = this.node1.neighbors();
        Collection<MapNode> emptySet = new HashSet<>();
        assertEquals(neighbors, emptySet);
    }

    @Test
    public void testHeuristicCost() throws Exception {
        MapNode a = new MapNode(new Coordinate(10,0,1));
        MapNode b = new MapNode(new Coordinate(20, 0, 1));
        //assertEquals(b.traversalCost(a), 10);
    }

    @Test
    public void distanceTo() throws Exception {
    }

    @Test
    public void getPixels() throws Exception {
        try{
            MapNode.getPixels(myPath);
        }catch(Exception E){
            Assert.fail("Exception " + E);
            E.printStackTrace();
        }
    }

    @Test
    public void getFeet() throws Exception {
        try{
            MapNode.getFeet(myPath);
        }catch(Exception E){
            Assert.fail("Exception " + E);
            E.printStackTrace();
        }
    }

    @Test
    public void getSeconds() throws Exception {
        try{
            MapNode.getSeconds(myPath);
        }catch(Exception E){
            Assert.fail("Exception " + E);
            E.printStackTrace();
        }
    }

    @Test
    public void getSteps() throws Exception {
        try{
            MapNode.getSteps(myPath);
        }catch(Exception E){
            Assert.fail("Exception " + E);
            E.printStackTrace();
        }
    }

    @Test
    public void heuristicCost() throws Exception {
        try{
            node1.heuristicCost(node2);
        }catch(Exception E){
            Assert.fail("Exception " + E);
            E.printStackTrace();
        }
    }

    @Test
    public void traversalCost() throws Exception {
        try{
            node1.traversalCost(node2);
        }catch(Exception E){
            Assert.fail("Exception " + E);
            E.printStackTrace();
        }
    }

    @Test
    public void getLocation() throws Exception {
        try{
            node3.getLocation();
        }catch(Exception E){
            Assert.fail("Exception " + E);
            E.printStackTrace();
        }
    }

    @Test
    public void addNeighbor() throws Exception {
        try{
            node2.addNeighbor(node3);
        }catch(Exception E){
            Assert.fail("Exception " + E);
            E.printStackTrace();
        }
    }

    @Test
    public void getModelNode() throws Exception {
        try{
            node1.getModelNode();
        }catch(Exception E){
            Assert.fail("Exception " + E);
            E.printStackTrace();
        }
    }
}