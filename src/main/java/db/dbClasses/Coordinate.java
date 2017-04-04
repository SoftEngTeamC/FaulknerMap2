package db.dbClasses;

/**
 * Created by jack on 3/31/17.
 */
public class Coordinate {
    private float xpos;
    private float ypos;
    private int zpos;

    public Coordinate(float xpos, float ypos, int zpos){
        this.xpos = xpos;
        this.ypos = ypos;
        this.zpos = zpos;
    }

    float distanceTo(Coordinate otherCord){return 0;}

    public float getXpos() {
        return xpos;
    }

    public void setXpos(float xpos) {
        this.xpos = xpos;
    }

    public float getYpos() {
        return ypos;
    }

    public void setYpos(float ypos) {
        this.ypos = ypos;
    }

    public int getZpos() {
        return zpos;
    }

    public void setZpos(int zpos) {
        this.zpos = zpos;
    }

    //toString to print out easy to read coordinates
    public String toString(){
        return "[" + xpos + "," + ypos + "," + zpos + "]";
    }
}
