package Memento;

import java.util.ArrayList;

/**
 * Created by Paul on 4/26/2017.
 */
public class LoginStatusEditor {

    protected ArrayList<Boolean> status;

    public void edit(boolean status){
        this.status.add(status);
    }

    public boolean getContent(){
        return this.status.get(status.size()-1);
    }

    public LoginStatusMemento memento(){
        return new LoginStatusMemento(this.status);
    }
}
