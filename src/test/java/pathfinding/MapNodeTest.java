package pathfinding;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;

import static org.junit.Assert.*;


public class MapNodeTest {
    private MapNode loneNode;

    @Before
    public void setUp() throws Exception {
        this.loneNode = new MapNode(new Coordinate(0,0,1));
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getNeighborsLoneNode() throws Exception {
        Collection<MapNode> neighbors = this.loneNode.neighbors();
        Collection<MapNode> emptySet = new HashSet<>();
        assertEquals(neighbors, emptySet);
    }

    @Test
    public void heuristicDistanceTo() throws Exception {

    }

    @Test
    public void distanceTo() throws Exception {

    }

}