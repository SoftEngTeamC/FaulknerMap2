package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import model.Hours;
import service.EMFProvider;


public class HoursEditorController extends Controller{
public Hours hours= new Hours("12","12","12","12","30","30","30","30","AM","AM","PM","PM");

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
    @FXML
    private Text startTimeErrorMorning;
    @FXML
    private Text startTimeErrorEvening;

    EMFProvider emf;

    public void initialize(){
        //displayerror.setVisible(false);
        emf = new EMFProvider();

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

    public void validateEqualMorning() {
        if((morninghrs1.getText().trim().equals(morninghrs2.getText().trim())) &&
                (morningmin1.getText().trim().equals(morningmin2.getText().trim())) &&
                (hours.ampm1.equals(hours.ampm2))){
            startTimeErrorMorning.setVisible(true);
        }
        else if (morninghrs1.getText().length()>2){
            displayerror.setVisible(true);
        }
        else if (morningmin1.getText().length()>2){
            displayerror.setVisible(true);
        }
        else if (morninghrs2.getText().length()>2){
            displayerror.setVisible(true);
        }
        else if (morningmin2.getText().length()>2){
            displayerror.setVisible(true);
        }
        else{
            startTimeErrorMorning.setVisible(false);
            displayerror.setVisible(false);
        }
    }

    public void validateEqualEvening() {
        if((eveninghrs1.getText().trim().equals(eveninghrs2.getText().trim())) &&
                (eveningmin1.getText().trim().equals(eveningmin2.getText().trim())) &&
                (hours.ampm3.equals(hours.ampm4))){
            startTimeErrorEvening.setVisible(true);
        }
        else if (eveninghrs1.getText().length()>2){
            displayerror.setVisible(true);
        }
        else if (eveningmin1.getText().length()>2){
            displayerror.setVisible(true);
        }
        else if (eveninghrs2.getText().length()>2){
            displayerror.setVisible(true);
        }
        else if (eveningmin2.getText().length()>2){
            displayerror.setVisible(true);
        }
        else{
            startTimeErrorEvening.setVisible(false);
            displayerror.setVisible(false);

        }
    }
    @FXML
    public void SubmitChanges() throws Exception{

        if (morninghrs1.getText().trim().isEmpty() && morninghrs2.getText().trim().isEmpty()
                && eveninghrs1.getText().trim().isEmpty() && eveninghrs2.getText().trim().isEmpty()
                && morningmin1.getText().trim().isEmpty() && morningmin2.getText().trim().isEmpty()
                && eveningmin1.getText().trim().isEmpty() && eveningmin2.getText().trim().isEmpty()){
            displayerror.setVisible(true);
        }
        else if (morningmin1.getText().length()<2){
            displayerror.setVisible(true);
        }
        else if (morningmin2.getText().length()<2){
            displayerror.setVisible(true);
        }
        else if (eveningmin2.getText().length()<2){
            displayerror.setVisible(true);
        }
        else if (eveningmin1.getText().length()<2){
            displayerror.setVisible(true);
        }
        else if (inputval.checktime(morninghrs1, 1,12) && inputval.checktime(morninghrs2, 1,12)
                && inputval.checktime(eveninghrs1, 1,12) && inputval.checktime(eveninghrs2, 1,12)
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
            hours = new Hours(hours.hours1,
                    hours.hours2,
                    hours.hours3,
                    hours.hours4,
                    hours.minutes1,
                    hours.minutes2,
                    hours.minutes3,
                    hours.minutes4,
                    hours.ampm1,
                    hours.ampm2,
                    hours.ampm3,
                    hours.ampm4);
            displayerror.setVisible(false);
            emf.hours = this.hours;
            logout();
        }

        else{
            displayerror.setVisible(true);
        }


    }



}
