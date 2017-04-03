package pathfinding;

public class Coordinate {
    private double x, y;
    private int floor;

    public Coordinate(double x, double y, int floor) {
        this.x = x;
        this.y = y;
        this.floor = floor;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getFloor() {
        return floor;
    }
}
