package Singleton;

import Memento.LoginStatusMemento;

import java.util.ArrayList;

/**
 * Created by Paul on 4/26/2017.
 *
 * this is our caretaker class. Used to add mementos and get the current one
 *
 */
public class LoginStatusSingleton {

    private static LoginStatusSingleton ourInstance = new LoginStatusSingleton();
    // init to 2 minutes
    private int timeout = 120;
    // this is our list of mementos
    private ArrayList<LoginStatusMemento> mentos = new ArrayList<>();

    public static LoginStatusSingleton getInstance() {
        return ourInstance;
    }

    private LoginStatusSingleton(){}

    /**
     * function for adding a memento to this object's list
     * @param m
     */
    public void addMemento(LoginStatusMemento m){
        mentos.add(m);
    }

    /**
     * getter for the memento
     * @return most recent memento
     */
    public LoginStatusMemento getMemento(){
        return mentos.get(1);
    }

    /**
     * getter for the timeout
     * @return timeout: int
     */
    public int getTimeout(){
        return this.timeout;
    }

    /**
     * setter for the timeout
     * @param timeout
     */
    public void setTimeout(int timeout){
        this.timeout = timeout;
    }




}
