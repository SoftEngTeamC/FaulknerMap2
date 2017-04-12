package textDirections;

//import org.testng.annotations.Test;
import pathfinding.MapNode;
import pathfinding.PathFinder;
import service.NodeService;
import pathfinding.Map;
import model.Node;

import java.awt.*;
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
        String output2 = "";
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
        double distance;
        for(i = 0; i < myPath.size() - 2; i++) {
            currentNode = myPath.get(i);
            nextNode = myPath.get(i+1);
            afterNextNode = myPath.get(i+2);
            currentAngle = getAngle(currentNode, nextNode);
            nextAngle = getAngle(nextNode, afterNextNode);
            angleShift = getAngleShift(currentAngle, nextAngle);
            //System.out.println(angleShift);
            direction = getDirection(currentAngle);

            //System.out.println(direction);

            if(direction.equals("horizontal")) {
                //System.out.println(direction);
                distance = xDistance(currentNode, nextNode);
                totalDistance += distance;
                //System.out.println(distance);
            }
            else if(direction.equals("vertical")) {
                //System.out.println(direction);
                totalDistance += yDistance(currentNode, nextNode);
            }
            else {
                //System.out.println(direction);
                totalDistance += distanceBetween(currentNode, nextNode);
            }

            if(angleShift >= -1 * p/6 && angleShift <= p/6) {

            }
            else {
                System.out.println(angleShift);
                output2 = output.concat("Move straight " + totalDistance + " pixels, then take a ");
                output = output2;
                totalDistance = 0;
                if (angleShift > p / 6 && angleShift <= 5 * p / 12) {
                    output2 = output.concat("slight right turn\n");
                    output = output2;
                }
                else if (angleShift > 5 * p / 12 && angleShift <= 7 * p / 12) {
                    output2 = output.concat("right turn\n");
                    output = output2;
                }
                else if (angleShift > 7 * p / 12 && angleShift <= p) {
                    output2 = output.concat("sharp right turn\n");
                    output = output2;
                }
                else if (angleShift < -1 * p / 6 && angleShift >= -5 * p / 12) {
                    output2 = output.concat("slight left turn\n");
                    output = output2;
                }
                else if (angleShift < -5 * p / 12 && angleShift >= -7 * p / 12) {
                    output2 = output.concat("left turn\n");
                    output = output2;
                }
                else if (angleShift < -7 * p / 12 && angleShift >= -1 * p) {
                    output2 = output.concat("sharp left turn\n");
                    output = output2;
                }
            }
        }

        currentNode = myPath.get(i);
        nextNode = myPath.get(i+1);
        currentAngle = getAngle(currentNode, nextNode);
        direction = getDirection(currentAngle);

        if(direction.equals("horizontal")) {
            //System.out.println(direction);
            distance = xDistance(currentNode, nextNode);
            totalDistance += distance;
            //System.out.println(distance);
        }
        else if(direction.equals("vertical")) {
            //System.out.println(direction);
            totalDistance += yDistance(currentNode, nextNode);
        }
        else {
            //System.out.println(direction);
            totalDistance += distanceBetween(currentNode, nextNode);
        }

        output2 = output.concat("Move forward " + totalDistance + " pixels");
        output = output2;

        return output;
    }

    private static double distanceBetween(MapNode a, MapNode b) {
        return Math.sqrt(Math.pow((a.getLocation().getX() - b.getLocation().getX()), 2) +
                Math.pow((a.getLocation().getY() - b.getLocation().getY()), 2));
    }

    private static double xDistance(MapNode a, MapNode b) {
        double x1 = a.getLocation().getX();
        double x2 = b.getLocation().getX();
        //System.out.println(x1);
        //System.out.println(x2);
        return Math.abs(x1 - x2);
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
        return Math.atan2(deltaY, deltaX);
    }

    private static double getAngleShift(double firstAngle, double secondAngle) {
        double angle = secondAngle - firstAngle;
        if (angle > Math.PI) {
            angle -= 2 * Math.PI;
        }
        if(angle < -1 * Math.PI) {
            angle += 2 * Math.PI;
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

//    @Test
//    public void printDirectionsTest1() {
//        Map map = new Map(NS.getAllNodes());
//        PathFinder pf = new PathFinder();
//        Node testNode1 = NS.findNodeByName("Day Surgery");
//        Node testNode2 = NS.findNodeByName("Center for Preoperative Evaluation");
//
//        MapNode mNode1 = map.getNode(testNode1.getId());
//        MapNode mNode2 = map.getNode(testNode2.getId());
//
//        List<MapNode> path = pf.shortestPath(mNode1, mNode2);
//
//        String directions = getText(path);
//        if(directions.equals("")) {
//            System.out.println("no directions");
//        }
//        System.out.println(directions);
//    }
}
