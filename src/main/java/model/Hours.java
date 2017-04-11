package model;


public class Hours {
    private int hours1;
    private int minutes1;
    private String ampm1;
    private int hours2;
    private int minutes2;
    private String ampm2;
    private int hours3;
    private int minutes3;
    private String ampm3;
    private int hours4;
    private int minutes4;
    private String ampm4;

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
