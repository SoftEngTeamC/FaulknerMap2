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
    public static LoginStatusSingleton getInstance() {
        return ourInstance;
    }

    // this is our list of mementos
    private ArrayList<LoginStatusMemento> mentos = new ArrayList<>();

    private LoginStatusSingleton(){}

    /**
     * function for adding a memento to this object's list
     * @param m
     */
    public void addMemento(LoginStatusMemento m){
        mentos.add(m);
    }

    public LoginStatusMemento getMemento(){
        return mentos.get(1);
    }


}
