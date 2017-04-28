package Memento;

import java.util.ArrayList;

/**
 * Created by Paul on 4/26/2017.
 * originator class. Used to set and save mementos.
 */
public class LoginStatusEditor {

    private boolean status;

    /**
     * changes the current status to given status
     * @param status boolean
     */
    public void setStatus(boolean status){
        this.status = status;
    }

    /**
     * sets the local status from given memento
     */
    public void restore(LoginStatusMemento m){
        status = m.getStatus();
    }

    /**
     * Saves to a new memento with current status
     * @return memento object
     */
    public LoginStatusMemento save(){
        return new LoginStatusMemento(status);
    }

}
