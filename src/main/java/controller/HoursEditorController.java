package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Hours;

import java.awt.*;


public class HoursEditorController extends Controller{
private Hours hours= new Hours("12","12","12","12","30","30","30","30","AM","AM","PM","PM");

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
    private Text displayerror;

    public void initialize(){
        //displayerror.setVisible(false);

    }


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
        ampm1.setText("AM");
    }
    public void settopm1() {
        hours.ampm1="PM";
        ampm1.setText("PM");


    }
    public void settoam2() {
        hours.ampm2="AM";
        ampm2.setText("AM");

    }
    public void settopm2() {
        hours.ampm2="PM";
        ampm2.setText("PM");

    }
    public void settoam3() {
        hours.ampm3="AM";
        ampm3.setText("AM");

    }
    public void settopm3() {
        hours.ampm3="PM";
        ampm3.setText("PM");

    }
    public void settoam4() {
        hours.ampm4="AM";
        ampm4.setText("AM");

    }
    public void settopm4() {
        hours.ampm4="PM";
        ampm4.setText("PM");

    }


    @FXML
    public void SubmitChanges() throws Exception{

        if (morninghrs1.getText().trim().isEmpty() && morninghrs2.getText().trim().isEmpty()
                && eveninghrs1.getText().trim().isEmpty() && eveninghrs2.getText().trim().isEmpty()
                && morningmin1.getText().trim().isEmpty() && morningmin2.getText().trim().isEmpty()
                && eveningmin1.getText().trim().isEmpty() && eveningmin2.getText().trim().isEmpty()){
            displayerror.setVisible(true);
            morningmin1.setText("0");
            morninghrs1.setText("0");
            morningmin2.setText("0");
            morninghrs2.setText("0");
            eveninghrs2.setText("0");
            eveninghrs1.setText("0");
            eveningmin2.setText("0");
            eveningmin1.setText("0");
        }
        else if (inputval.checktime(morninghrs1, 0,12) && inputval.checktime(morninghrs2, 0,12)
                && inputval.checktime(eveninghrs1, 0,12) && inputval.checktime(eveninghrs2, 0,12)
                && inputval.checktime(morningmin1, 0,59) && inputval.checktime(morningmin2, 0,59)
                && inputval.checktime(eveningmin1, 0,59) && inputval.checktime(eveningmin2, 0,59)){
            hours.hours1=morninghrs1.getText();
            hours.hours2=morninghrs2.getText();
            hours.hours3=eveninghrs1.getText();
            hours.hours4=eveninghrs2.getText();
            hours.minutes1=morningmin1.getText();
            hours.minutes2=morningmin2.getText();
            hours.minutes3=eveningmin1.getText();
            hours.minutes4=eveningmin2.getText();
            System.out.println(hours.hours1+":"+hours.minutes1+" "+hours.ampm1);
            System.out.println(hours.hours2+":"+hours.minutes2+" "+hours.ampm2);
            System.out.println(hours.hours3+":"+hours.minutes3+" "+hours.ampm3);
            System.out.println(hours.hours4+":"+hours.minutes4+" "+hours.ampm4);
            displayerror.setVisible(false);

        }



        else{
            displayerror.setVisible(true);
            morningmin1.setText("0");
            morninghrs1.setText("0");
            morningmin2.setText("0");
            morninghrs2.setText("0");
            eveninghrs2.setText("0");
            eveninghrs1.setText("0");
            eveningmin2.setText("0");
            eveningmin1.setText("0");


        }


    }



}
