package controller;

import javafx.scene.control.TextField;

/**
 * Created by Guillermo on 4/11/17.
 */
public class inputval {

    public static boolean checktime(TextField field, int lower, int upper){
        if (Integer.parseInt(field.getText()) <= upper && Integer.parseInt(field.getText()) >= lower){
        return true ;
        }
        else {
            return false;
        }
    }
}
