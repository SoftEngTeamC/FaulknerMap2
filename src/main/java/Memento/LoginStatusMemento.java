package Memento;

/**
 * Created by Paul on 4/26/2017.
 */
public class LoginStatusMemento {

    // This is our login status. True if admin is currently logged in.
    protected boolean status;

    public LoginStatusMemento(boolean status){
        this.status = status;
    }

    public boolean getStatus(){
        return this.status;
    }

}
