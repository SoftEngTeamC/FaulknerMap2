package db.dbClasses;

import db.Driver;
import db.dbHelpers.EdgesHelper;

import java.util.List;
import java.util.UUID;

/**
 * Created by jack on 3/31/17.
 */
public class Node {
    private List<Node> neighbors;
    private Coordinate position;
    private UUID id;
    private String name;

    public Node(List<Node> neighbors, Coordinate position, String name){
        this.neighbors = neighbors;
        this.position = position;
        this.name = name;
        id = UUID.randomUUID(); //set id to random
    }


    public List<Node> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<Node> neighbors) {
        this.neighbors = neighbors;
    }

    public Coordinate getPosition() {
        return position;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String toString(){
        return "Node " + name + ": " + position.toString();
    }

    public void addEdge(Node from){
        EdgesHelper eh = Driver.getEdgesHelper();
        float length = (float)Math.sqrt((Math.pow(this.position.getXpos() - from.position.getXpos(),2)+
                        (Math.pow(this.position.getYpos() - from.position.getYpos(),2))));

        Edge edge1 = new Edge(this, from, length);
      //  Edge edge2 = new Edge( from, this, length);

        eh.addEdge(edge1);
    //    eh.addEdge(edge2);
    }

}
