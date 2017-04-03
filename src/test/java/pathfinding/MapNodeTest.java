package pathfinding;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;


public class MapNodeTest {
    private MapNode loneNode, A, B, C, D;

    @Before
    public void setUp() throws Exception {
        this.loneNode = new MapNode(new Point2D.Double(0, 0), 1);

        // A --- B
        //  \   /
        //   \ /
        //    C --- D
        this.A = new MapNode(new Point2D.Double(0, 5), 1);
        this.B = new MapNode(new Point2D.Double(3, 4), 1);
        this.C = new MapNode(new Point2D.Double(1, 0), 1);
        this.D = new MapNode(new Point2D.Double(6, 0), 1);
        this.A.addNeighbor(B);
        this.A.addNeighbor(C);
        this.B.addNeighbor(C);
        this.C.addNeighbor(D);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getNeighborsLoneNode() throws Exception {
        Collection<MapNode> neighbors = this.loneNode.getNeighbors();
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