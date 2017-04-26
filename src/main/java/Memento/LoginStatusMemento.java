package Memento;

/**
 * Created by Paul on 4/26/2017.
 * Read this to understand usage: https://sourcemaking.com/design_patterns/memento/java/1
 */
public class LoginStatusMemento {

    // This is our login status. True if admin is currently logged in.
    private boolean status;

    /**
     * constructor
     * @param status boolean status for loginstatus
     */
    public LoginStatusMemento(boolean status){
        this.status = status;
    }

    /**
     * getter for the status boolean
     * @return status
     */
    public boolean getStatus(){
        return this.status;
    }

}
