package model;


public class Hours {
    public int hours1;
    public int minutes1;
    public String ampm1;
    public int hours2;
    public int minutes2;
    public String ampm2;
    public int hours3;
    public int minutes3;
    public String ampm3;
    public int hours4;
    public int minutes4;
    public String ampm4;

    public Hours(int hours1, int hours2, int hours3, int hours4, int minutes1, int minutes2, int minutes3, int minutes4, String ampm1, String ampm2, String ampm3, String ampm4){
        this.hours1=hours1;
        this.hours2=hours2;
        this.hours3=hours3;
        this.hours4=hours4;
        this.minutes1=minutes1;
        this.minutes2=minutes2;
        this.minutes3=minutes3;
        this.minutes4=minutes4;
        this.ampm1=ampm1;
        this.ampm2=ampm2;
        this.ampm3=ampm3;
        this.ampm4=ampm4;
    }
}
