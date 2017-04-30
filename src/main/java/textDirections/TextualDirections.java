package textDirections;

import pathfinding.MapNode;
import pathfinding.Path;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;


public class TextualDirections {
    public static List<Step> pathSteps(Path path) {
        List<Step> steps = new LinkedList<>();
        List<MapNode> nodes = path.nodes();
        for (int i = 1; i < nodes.size() - 1; i++) {
            Point.Double prevNode = nodes.get(i - 1).getModelNode().getPoint();
            Point.Double currNode = nodes.get(i).getModelNode().getPoint();
            Point.Double nextNode = nodes.get(i + 1).getModelNode().getPoint();

            double angle = getAngle(prevNode, currNode, nextNode);

            Direction direction = angleToDirection(angle);
            Sharpness sharpness = angleSharpness(angle);

            double distance = prevNode.distance(currNode) * MapNode.FEET_PER_PIXEL;

            steps.add(new Step(direction, sharpness, distance));
        }
        return steps;
    }

    static double getAngle(Point.Double A, Point.Double B, Point.Double C) {
        double a = B.distance(C);
        double b = A.distance(C);
        double c = A.distance(B);

        double radAngle = Math.acos((a*a + c*c - b*b) / (2*b*c));
        double direction = (B.x - A.x) / (B.y + A.y) + (C.x - B.x) / (B.y + C.y);
        return direction >= 0 ? Math.toDegrees(radAngle) : Math.toDegrees(radAngle) + 180;


    }

    enum Direction { LEFT, RIGHT, STRAIGHT, BACKWARDS }
    static Direction angleToDirection(double angle) {
        final double straightTolerance = 15;
        if (angle > 180 - straightTolerance && angle < 180 + straightTolerance) return Direction.STRAIGHT;
        if (angle < straightTolerance || angle > 360 - straightTolerance) return Direction.BACKWARDS;
        if (angle > 0 && angle < 180) return Direction.LEFT;
        return Direction.RIGHT;
    }

    enum Sharpness { SHARP, NORMAL, SLIGHT }
    static Sharpness angleSharpness(double angle) {
        angle %= 180;
        final double sharp = 45;
        final double normal = 135;
        final double slight = 180;
        if (angle < sharp) return Sharpness.SHARP;
        if (angle < normal) return Sharpness.NORMAL;
        return Sharpness.SLIGHT;
    }
}
