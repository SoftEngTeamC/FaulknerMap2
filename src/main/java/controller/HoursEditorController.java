package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Hours;


import javax.xml.soap.Text;
import java.awt.*;


public class HoursEditorController extends Controller{
private Hours hours= new Hours(12,12,12,12,30,30,30,30,"AM","AM","PM","PM");

    public void initialize(){}
    @FXML
    private Button logoutBtn;
    @FXML
    private Button backBtn;
    @FXML
    private Button submitbtn;
    @FXML
    private TextField morninghrs1;
    @FXML
    private TextField morningmin1;
    @FXML
    private TextField morningmin2;
    @FXML
    private TextField morninghrs2;
    @FXML
    private TextField eveninghrs1;
    @FXML
    private TextField eveningmin1;
    @FXML
    private TextField eveningmin2;
    @FXML
    private TextField eveninghrs2;
    @FXML
    private MenuButton ampm1;
    @FXML
    private MenuItem am1;
    @FXML
    private MenuItem pm1;
    @FXML
    private MenuButton ampm2;
    @FXML
    private MenuItem am2;
    @FXML
    private MenuItem pm2;
    @FXML
    private MenuButton ampm3;
    @FXML
    private MenuItem am3;
    @FXML
    private MenuItem pm3;
    @FXML
    private MenuButton ampm4;
    @FXML
    private MenuItem am4;
    @FXML
    private MenuItem pm4;
    @FXML
    public Label failure;

    @FXML
    public void back() throws Exception {
        switchScreen("view/AdminToolMenu.fxml", "Directory Editor", backBtn);
    }

    @FXML
    public void logout() throws Exception {
        switchScreen("view/Main.fxml", "Main", logoutBtn);
    }
    public void settoam1() {
        hours.ampm1="AM";
    }
    public void settopm1() {
        hours.ampm1="PM";

    }
    public void settoam2() {
        hours.ampm2="AM";

    }
    public void settopm2() {
        hours.ampm2="PM";

    }
    public void settoam3() {
        hours.ampm3="AM";

    }
    public void settopm3() {
        hours.ampm3="PM";

    }
    public void settoam4() {
        hours.ampm4="AM";

    }
    public void settopm4() {
        hours.ampm4="PM";

    }


    @FXML
    public void SubmitChanges() throws Exception{
        if (Integer.parseInt(morninghrs1.getText()) <= 12){
            hours.hours1=Integer.parseInt(morninghrs1.getText());
        }
        if (Integer.parseInt(morninghrs2.getText()) <= 12){
            hours.hours2=Integer.parseInt(morninghrs2.getText());
        }
        if (Integer.parseInt(eveninghrs1.getText()) <= 12){
            hours.hours3=Integer.parseInt(eveninghrs1.getText());
        }
        if (Integer.parseInt(eveninghrs2.getText()) <= 12){
            hours.hours4=Integer.parseInt(eveninghrs2.getText());
        }
        if (Integer.parseInt(morningmin1.getText()) <= 59){
            hours.minutes1=Integer.parseInt(morningmin1.getText());
        }
        if (Integer.parseInt(morningmin2.getText()) <= 59){
            hours.minutes2=Integer.parseInt(morningmin2.getText());
        }
        if (Integer.parseInt(eveningmin1.getText()) <= 59){
            hours.minutes3=Integer.parseInt(eveningmin1.getText());
        }
        if (Integer.parseInt(eveningmin2.getText()) <= 59){
            hours.minutes4=Integer.parseInt(eveningmin2.getText());
        }
        System.out.println(hours.hours1+":"+hours.minutes1+" "+hours.ampm1);
        System.out.println(hours.hours2+":"+hours.minutes2+" "+hours.ampm2);
        System.out.println(hours.hours3+":"+hours.minutes3+" "+hours.ampm3);
        System.out.println(hours.hours4+":"+hours.minutes4+" "+hours.ampm4);

    }



}
