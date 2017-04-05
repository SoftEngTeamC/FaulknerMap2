package pathfinding;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;


public class MapNodeTest {
    private MapNode loneNode;
    private MapNode togetherNode;

    @Before
    public void setUp() throws Exception {
        this.loneNode = new MapNode(new Coordinate(0,0,1));
        this.loneNode = new MapNode(new Coordinate(10,0,1));
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetNeighborsLoneNode() throws Exception {
        Collection<MapNode> neighbors = this.loneNode.neighbors();
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

}