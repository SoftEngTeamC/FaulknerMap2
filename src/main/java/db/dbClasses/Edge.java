package db.dbClasses;

import java.util.UUID;

/**
 * Created by jack on 3/31/17.
 */
public class Edge {
    private UUID id;
    private Node from;
    private Node to;
    private float length;
    private boolean disabled;

    public Edge(Node from, Node to, float length){
        this.from = from;
        this.to = to;
        this.length = length;
        disabled = false;
        id = UUID.randomUUID(); //set id to random
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Node getFrom() {
        return from;
    }

    public void setFrom(Node from) {
        this.from = from;
    }

    public Node getTo() {
        return to;
    }

    public void setTo(Node to) {
        this.to = to;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String toString(){
        return "EDGE: " + from.getPosition().toString() + " -->" + to.getPosition().toString();
    }

}
