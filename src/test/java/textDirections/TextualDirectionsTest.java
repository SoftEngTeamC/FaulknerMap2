package textDirections;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;


public class TextualDirectionsTest {
    private Point.Double A, B, C, D, E, F;

    @Before
    public void setUp() throws Exception {
        // Create some simple triangles
        A = new Point.Double(0, 0);
        B = new Point.Double(1, 0);
        C = new Point.Double(1, 1);

        // 30, 60, 90
        D = new Point.Double(0, 0);
        E = new Point.Double(Math.sqrt(3), 1);
        F = new Point.Double(Math.sqrt(3), -1);
    }

    @Test
    public void getAngle() throws Exception {
        assertEquals(90, TextualDirections.getAngle(A, B, C), 0.1);
        assertEquals(240, TextualDirections.getAngle(D, E, F), 0.1);
    }

    @Test
    public void angleToDirection() throws Exception {
        assertEquals(TextualDirections.Direction.BACKWARDS, TextualDirections.angleToDirection(1));
        assertEquals(TextualDirections.Direction.LEFT, TextualDirections.angleToDirection(100));
        assertEquals(TextualDirections.Direction.LEFT, TextualDirections.angleToDirection(160));
        assertEquals(TextualDirections.Direction.STRAIGHT, TextualDirections.angleToDirection(179));
        assertEquals(TextualDirections.Direction.STRAIGHT, TextualDirections.angleToDirection(181));
        assertEquals(TextualDirections.Direction.RIGHT, TextualDirections.angleToDirection(270));
        assertEquals(TextualDirections.Direction.BACKWARDS, TextualDirections.angleToDirection(359));
    }

    @Test
    public void angleSharpness() throws Exception {
        assertEquals(TextualDirections.Sharpness.SHARP, TextualDirections.angleSharpness(2));
        assertEquals(TextualDirections.Sharpness.NORMAL, TextualDirections.angleSharpness(90));
        assertEquals(TextualDirections.Sharpness.SLIGHT, TextualDirections.angleSharpness(170));
    }

    @Test
    public void angleToDirectionFromTriangle() throws Exception {
        double ABC = TextualDirections.getAngle(A, B, C);
        assertEquals(TextualDirections.Direction.LEFT, TextualDirections.angleToDirection(ABC));

        double DEF = TextualDirections.getAngle(D, E, F);
        assertEquals(TextualDirections.Direction.RIGHT, TextualDirections.angleToDirection(DEF));
    }
}