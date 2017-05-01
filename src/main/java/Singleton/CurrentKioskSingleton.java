package Singleton;

import model.Node;

/**
 * Created by Paul on 5/1/2017.
 */
public class CurrentKioskSingleton {

    private static CurrentKioskSingleton ourInstance = new CurrentKioskSingleton();

    private Node n;

    public static CurrentKioskSingleton getInstance() {
        return ourInstance;
    }

    private CurrentKioskSingleton() {
    }

    /**
     * setter for the node.
     * 
     * @param n
     */
    public void setNode(Node n){
        this.n = n;
    }

    /**
     * getter for the node.
     *
     * @return
     */
    public Node getNode(){
        return this.n;
    }

}
