package textDirections;

import org.junit.Test;
import pathfinding.MapNode;
import pathfinding.PathFinder;
import service.NodeService;
import pathfinding.Map;
import model.Node;

import java.nio.file.Path;
import java.util.List;

import static java.lang.Math.atan2;

/**
 * Created by Alex on 4/9/2017.
 */
public class MakeDirections {
    private NodeService NS = new NodeService();
    private PathFinder pf = new PathFinder();
    public static String getText(List<MapNode> myPath){
        String output = "";
        String direction;
        int i;
        MapNode currentNode;
        MapNode nextNode;
        MapNode afterNextNode;
        double totalDistance = 0;
        double currentAngle;
        double nextAngle;
        double angleShift;
        double p = Math.PI;
        for(i = 0; i < myPath.size() - 2; i++) {
            currentNode = myPath.get(i);
            nextNode = myPath.get(i+1);
            afterNextNode = myPath.get(i+2);
            currentAngle = getAngle(currentNode, nextNode);
            nextAngle = getAngle(nextNode, afterNextNode);
            angleShift = getAngleShift(currentAngle, nextAngle);
            direction = getDirection(currentAngle);

            if(direction == "horizontal") {
                totalDistance += xDistance(currentNode, nextNode);
            }
            else if(direction == "vertical") {
                totalDistance += yDistance(currentNode, nextNode);
            }
            else {
                totalDistance += distanceBetween(currentNode, nextNode);
            }

            if(angleShift >= -1 * p/6 && angleShift <= p/6) {

            }
            else {
                output.concat("Move straight " + totalDistance + " pixels, then take a ");
                totalDistance = 0;
                if (angleShift > p / 6 && angleShift <= 5 * p / 12) {
                    output.concat("slight left turn\n");
                }
                else if (angleShift > 5 * p / 12 && angleShift <= 7 * p / 12) {
                    output.concat("left turn\n");
                }
                else if (angleShift > 7 * p / 12 && angleShift <= p) {
                    output.concat("sharp left turn\n");
                }
                else if (angleShift < -1 * p / 6 && angleShift >= -5 * p / 12) {
                    output.concat("slight right turn\n");
                }
                else if (angleShift < -5 * p / 12 && angleShift >= -7 * p / 12) {
                    output.concat("right turn\n");
                }
                else if (angleShift < -7 * p / 12 && angleShift >= -1 * p) {
                    output.concat("sharp right turn\n");
                }
            }
        }
        if(output == "") {
            output.concat("Move forward " + totalDistance + " pixels");
        }
        return output;
    }

    private static double distanceBetween(MapNode a, MapNode b) {
        return Math.sqrt(Math.pow((a.getLocation().getX() - b.getLocation().getX()), 2) +
                Math.pow((a.getLocation().getY() - b.getLocation().getY()), 2));
    }

    private static double xDistance(MapNode a, MapNode b) {
        return Math.abs(a.getLocation().getX() - b.getLocation().getX());
    }

    private static double yDistance(MapNode a, MapNode b) {
        return Math.abs(a.getLocation().getY() - b.getLocation().getY());
    }

    /** Angle from starting node A to ending node B
     * @param a First node
     * @param b Second node
     * @return Angle in radians
     */
    private static double getAngle(MapNode a, MapNode b){
        double deltaY = b.getLocation().getY() - a.getLocation().getY();
        double deltaX = b.getLocation().getX() - a.getLocation().getX();
        return Math.atan2(deltaX, deltaY);
    }

    private static double getAngleShift(double firstAngle, double secondAngle) {
        double angle = secondAngle - firstAngle;
        if (angle > Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }

    private static String getDirection(double angle) {
        double p = Math.PI;
        if(angle >= p/(-6) && angle <= p/6 || angle >= 5 * p/6 || angle <= -5 * p/6) {
            return "horizontal";
        }
        else if(angle >= 5 * p/12 && angle <= 7 * p/12 || angle >= -7 * p/12 && angle <= -5 * p/12) {
            return "vertical";
        }
        else {
            return "diagonal";
        }
    }

    @Test
    public void printDirectionsTest1() {
        Map map = new Map(NS.getAllNodes());
        PathFinder pf = new PathFinder();
        Node testNode1 = NS.findNodeByName("Day Surgery");
        Node testNode2 = NS.findNodeByName("Blood Draw");

        MapNode mNode1 = map.getNode(testNode1.getId());
        MapNode mNode2 = map.getNode(testNode2.getId());

        String directions = getText(pf.shortestPath(mNode1, mNode2));
        System.out.print(directions);
    }
}
