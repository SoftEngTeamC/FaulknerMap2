package textDirections;

import pathfinding.MapNode;

import java.util.List;

import static java.lang.Math.atan2;

/**
 * Created by Alex on 4/9/2017.
 */
public class MakeDirections {
    public static String getText(List<MapNode> myPath){

        return new String();
    }

    /** Angle from starting node A to ending node B
     * @param a First node
     * @param b Second node
     * @return Angle in radians
     */
    private double getAngle(MapNode a, MapNode b){
        double deltaY = b.getLocation().getY() - a.getLocation().getY();
        double deltaX = b.getLocation().getX() - a.getLocation().getX();
        return Math.atan2(deltaY, deltaX);
    }
}
