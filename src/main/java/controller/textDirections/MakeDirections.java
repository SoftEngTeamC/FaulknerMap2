package controller.textDirections;
import controller.LanguageController;
import pathfinding.MapNode;
import pathfinding.Path;

import java.text.DecimalFormat;

public class MakeDirections {

    public static String getText(Path path, int language) {
        String[] directions = LanguageController.initializeTextPathfinding(language);
        DecimalFormat pathFormat = new DecimalFormat("#.#");
        double pathLength = path.distanceInFeet();
        double pathTime = path.timeInSeconds();
        String output = "";
        String output2 = output.concat("Approximate Distance: " + pathFormat.format(pathLength) +
                " feet. \nEstimated time: " + pathFormat.format(Math.floor(pathTime / 60)) + " minutes and " +
                pathFormat.format(Math.floor(pathTime % 60)) + " seconds.\n\t--------------------------\n");
        output = output2;
        String direction;
        int i;
        MapNode currentNode, nextNode, afterNextNode;
        double totalDistance = 0;
        double currentAngle, nextAngle, angleShift, distance;
        double p = Math.PI;
        for (i = 0; i < path.numNodes() - 2; i++) {
            currentNode = path.getNode(i);
            nextNode = path.getNode(i + 1);
            afterNextNode = path.getNode(i + 2);
            currentAngle = getAngle(currentNode, nextNode);
            nextAngle = getAngle(nextNode, afterNextNode);
            angleShift = getAngleShift(currentAngle, nextAngle);
            direction = getDirection(currentAngle);


            if (currentNode.getLocation().getFloor() != nextNode.getLocation().getFloor()) {
                output2 = output.concat("Take elevator to floor: " + nextNode.getLocation().getFloor() + "\n");
                output = output2;
            } else {
                switch (direction) {
                    case "horizontal":
                        //System.out.println(direction);
                        distance = xDistance(currentNode, nextNode);
                        totalDistance += distance;
                        //System.out.println(distance);
                        break;
                    case "vertical":
                        //System.out.println(direction);
                        totalDistance += yDistance(currentNode, nextNode);
                        break;
                    default:
                        //System.out.println(direction);
                        totalDistance += distanceBetween(currentNode, nextNode);
                        break;
                }

                if (angleShift >= -1 * p / 6 && angleShift <= p / 6) {

                } else {
                    //System.out.println(angleShift);
                    DecimalFormat df = new DecimalFormat("#.#");
                    output2 = output.concat("Move straight " + df.format(Math.round(totalDistance)) + " feet, then take a ");
                    output = output2;
                    totalDistance = 0;
                    if (angleShift > p / 6 && angleShift <= 5 * p / 12) {
                        output2 = output.concat("slight right turn\n");
                        output = output2;
                    } else if (angleShift > 5 * p / 12 && angleShift <= 7 * p / 12) {
                        output2 = output.concat("right turn\n");
                        output = output2;
                    } else if (angleShift > 7 * p / 12 && angleShift <= p) {
                        output2 = output.concat("sharp right turn\n");
                        output = output2;
                    } else if (angleShift < -1 * p / 6 && angleShift >= -5 * p / 12) {
                        output2 = output.concat("slight left turn\n");
                        output = output2;
                    } else if (angleShift < -5 * p / 12 && angleShift >= -7 * p / 12) {
                        output2 = output.concat("left turn\n");
                        output = output2;
                    } else if (angleShift < -7 * p / 12 && angleShift >= -1 * p) {
                        output2 = output.concat("sharp left turn\n");
                        output = output2;
                    }
                }
            }
        }

        currentNode = path.getNode(i);
        nextNode = path.getNode(i + 1);
        currentAngle = getAngle(currentNode, nextNode);
        direction = getDirection(currentAngle);

        switch (direction) {
            case "horizontal":
                //System.out.println(direction);
                distance = xDistance(currentNode, nextNode);
                totalDistance += Math.round(distance);
                //System.out.println(distance);
                break;
            case "vertical":
                //System.out.println(direction);
                totalDistance += Math.round(yDistance(currentNode, nextNode));
                break;
            default:
                //System.out.println(direction);
                totalDistance += Math.round(distanceBetween(currentNode, nextNode));
                break;
        }
        DecimalFormat df = new DecimalFormat("#.#");
        output2 = output.concat("Move forward " + df.format(Math.round(totalDistance)) + " feet. You have arrived.");
        output = output2;

        return output;
    }

    private static double distanceBetween(MapNode a, MapNode b) {
        return Math.sqrt(Math.pow((a.getLocation().getX() - b.getLocation().getX()), 2) +
                Math.pow((a.getLocation().getY() - b.getLocation().getY()), 2)) *
                MapNode.FEET_PER_PIXEL;
    }

    private static double xDistance(MapNode a, MapNode b) {
        double x1 = a.getLocation().getX();
        double x2 = b.getLocation().getX();
        //System.out.println(x1);
        //System.out.println(x2);
        return Math.abs(x1 - x2) * MapNode.FEET_PER_PIXEL;
    }

    private static double yDistance(MapNode a, MapNode b) {
        return Math.abs(a.getLocation().getY() - b.getLocation().getY()) * MapNode.FEET_PER_PIXEL;
    }

    /**
     * Angle from starting node A to ending node B
     *
     * @param a First node
     * @param b Second node
     * @return Angle in radians
     */
    private static double getAngle(MapNode a, MapNode b) {
        double deltaY = b.getLocation().getY() - a.getLocation().getY();
        double deltaX = b.getLocation().getX() - a.getLocation().getX();
        return Math.atan2(deltaY, deltaX);
    }

    private static double getAngleShift(double firstAngle, double secondAngle) {
        double angle = secondAngle - firstAngle;
        if (angle > Math.PI) {
            angle -= 2 * Math.PI;
        }
        if (angle < -1 * Math.PI) {
            angle += 2 * Math.PI;
        }
        return angle;
    }

    private static String getDirection(double angle) {
        double p = Math.PI;
        if (angle >= p / (-6) && angle <= p / 6 || angle >= 5 * p / 6 || angle <= -5 * p / 6) {
            return "horizontal";
        } else if (angle >= 5 * p / 12 && angle <= 7 * p / 12 || angle >= -7 * p / 12 && angle <= -5 * p / 12) {
            return "vertical";
        } else {
            return "diagonal";
        }
    }
}
