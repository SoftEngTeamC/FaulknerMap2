package Singleton;

import Memento.LoginStatusMemento;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.util.Duration;

import java.util.ArrayList;

public class LoginStatusSingleton {

    // this is our list of mementos
    private ArrayList<LoginStatusMemento> mentos = new ArrayList<>();

    // this is our reference to the current screen
    private Button currentScreen;

    private LoginStatusSingleton(){
    }

    private static LoginStatusSingleton ourInstance;
    // init to 2 minutes
    private static int timeout = 3;

    static {
        if (ourInstance == null) {
            try {
                ourInstance = new LoginStatusSingleton();
//                System.out.println("Created new idle singleton");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static LoginStatusSingleton getInstance() {
        if (ourInstance == null) {
            ourInstance = new LoginStatusSingleton();
        }
        return ourInstance;
    }

    /**
     * function for adding a memento to this object's list
     *
     * @param m
     */
    public void addMemento(LoginStatusMemento m) {
//        System.out.println("adding memento");
        mentos.add(m);
    }

    /**
     * getter for the memento
     *
     * @return most recent memento
     */
    public LoginStatusMemento getMemento() {
        if(mentos.size() < 1){
            return new LoginStatusMemento(false);
        } else {
            return mentos.remove(mentos.size() - 1);
        }
    }

    /**
     * getter for the timeout
     *
     * @return timeout: int
     */
    public int getTimeout() {
        return this.timeout;
    }

    /**
     * setter for the timeout
     *
     * @param timeout
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    // getters and setters for the button

    public void setButton(Button b){
        this.currentScreen = b;
    }

    public Button getButton(){
        return this.currentScreen;
    }




}
