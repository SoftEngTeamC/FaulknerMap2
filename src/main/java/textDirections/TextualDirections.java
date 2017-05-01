package textDirections;

import pathfinding.MapNode;
import pathfinding.Path;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;


public class TextualDirections {
    public static List<String> pathSteps(Path path, ResourceBundle bundle) {
        List<String> steps = new LinkedList<>();
        List<MapNode> nodes = path.nodes();
        if (nodes.size() == 1) {
            steps.add(bundle.getString("alreadyThere"));
            return steps;
        }
        Point.Double prevNode = nodes.get(0).getModelNode().getPoint();
        Point.Double currNode = nodes.get(1).getModelNode().getPoint();

        double distance = prevNode.distance(currNode) * MapNode.FEET_PER_PIXEL;
        DecimalFormat df = new DecimalFormat("#.#");

        if(nodes.get(0).getModelNode().getName().toLowerCase().contains("elevator")){
            steps.add(bundle.getString("elevator") + nodes.get(1).getModelNode().getLocation().getFloor());
        } else {
            steps.add(bundle.getString("straight") +
                    bundle.getString("for") + " " + df.format(distance) + " " + bundle.getString("feet"));
        }

        for (int i = 2; i < nodes.size() - 1; i++) {
            prevNode = nodes.get(i - 1).getModelNode().getPoint();
            currNode = nodes.get(i).getModelNode().getPoint();
            Point.Double nextNode = nodes.get(i + 1).getModelNode().getPoint();

            double angle = getAngle(prevNode, currNode, nextNode);

            Direction direction = angleToDirection(angle);
            Sharpness sharpness = angleSharpness(angle);

            distance = prevNode.distance(currNode) * MapNode.FEET_PER_PIXEL;

            steps.add(new Step(direction, sharpness, distance, bundle).toString());
            if (nodes.get(i).getModelNode().getName().toLowerCase().contains("elevator")) {
                steps.add(bundle.getString("elevator") + nodes.get(i + 1).getModelNode().getLocation().getFloor());
                i ++;
            }
        }
        steps.add(bundle.getString("done"));
        return steps;
    }

    static double getAngle(Point.Double A, Point.Double B, Point.Double C) {
        double a = B.distance(C);
        double b = A.distance(C);
        double c = A.distance(B);

        double radAngle = Math.acos((a * a + c * c - b * b) / (2 * b * c));
        double direction = (B.x - A.x) / (B.y + A.y) + (C.x - B.x) / (B.y + C.y);
        return direction >= 0 ? Math.toDegrees(radAngle) : Math.toDegrees(radAngle) + 180;


    }

    enum Direction {LEFT, RIGHT, STRAIGHT, BACKWARDS}

    static Direction angleToDirection(double angle) {
        final double straightTolerance = 15;
        if (angle > 180 - straightTolerance && angle < 180 + straightTolerance) return Direction.STRAIGHT;
        if (angle < straightTolerance || angle > 360 - straightTolerance) return Direction.BACKWARDS;
        if (angle > 0 && angle < 180) return Direction.LEFT;
        return Direction.RIGHT;
    }

    enum Sharpness {SHARP, NORMAL, SLIGHT}

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
