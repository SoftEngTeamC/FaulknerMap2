package controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.Duration;
import model.Hours;
import service.EMFProvider;


public class HoursEditorController extends Controller{
    public static Hours hours= new Hours("12","12","12","12","30","30","30","30","AM","AM","PM","PM");
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
    private MenuButton ampm2;

    @FXML
    private MenuButton ampm3;

    @FXML
    private MenuButton ampm4;

    @FXML
    private Text displayerror;
    @FXML
    private Text startTimeErrorMorning;
    @FXML
    private Text startTimeErrorEvening;
    @FXML
    private Text displaySuccess;

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
        this.hours.ampm1="AM";
        ampm1.setText("AM");
    }
    public void settopm1() {
        this.hours.ampm1="PM";
        ampm1.setText("PM");


    }
    public void settoam2() {
        this.hours.ampm2="AM";
        ampm2.setText("AM");

    }
    public void settopm2() {
        this.hours.ampm2="PM";
        ampm2.setText("PM");

    }
    public void settoam3() {
        this.hours.ampm3="AM";
        ampm3.setText("AM");

    }
    public void settopm3() {
        this.hours.ampm3="PM";
        ampm3.setText("PM");

    }
    public void settoam4() {
        this.hours.ampm4="AM";
        ampm4.setText("AM");

    }
    public void settopm4() {
        this.hours.ampm4="PM";
        ampm4.setText("PM");

    }

    public boolean validateEqualMorning() {
        System.out.println();
        System.out.print(morninghrs1);
        System.out.println();
        //if all the boxes have numbers and all the numbers are equal to each other
        if((!morninghrs1.getText().trim().equals("") &&
            !morninghrs2.getText().trim().equals("") &&
            !morningmin1.getText().trim().equals("") &&
            !morningmin2.getText().trim().equals("")) &&
                (morninghrs1.getText().trim().equals(morninghrs2.getText().trim())) &&
                (morningmin1.getText().trim().equals(morningmin2.getText().trim())) &&
                (hours.ampm1.equals(hours.ampm2))){
            startTimeErrorMorning.setVisible(true);
            final Timeline timeline = new Timeline();
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2500),
                    new KeyValue(startTimeErrorMorning.visibleProperty(), false)));
            timeline.play();
        }
        else if (morninghrs1.getText().length()>2){
            flashErrorMessage();
        }
        else if (morningmin1.getText().length()>2){
            flashErrorMessage();
        }
        else if (morninghrs2.getText().length()>2){
            flashErrorMessage();
        }
        else if (morningmin2.getText().length()>2){
            flashErrorMessage();
        }
        else{
            startTimeErrorMorning.setVisible(false);
            displayerror.setVisible(false);
            return true;
        }
        return false;
    }

    public boolean validateEqualEvening() {
        //if all the boxes have numbers and all the numbers are equal to each other
        if((!eveninghrs1.getText().trim().equals("") &&
            !eveninghrs2.getText().trim().equals("") &&
            !eveningmin1.getText().trim().equals("") &&
            !eveningmin2.getText().trim().equals("")) &&
                (eveninghrs1.getText().trim().equals(eveninghrs2.getText().trim())) &&
                (eveningmin1.getText().trim().equals(eveningmin2.getText().trim())) &&
                (hours.ampm3.equals(hours.ampm4))){
            startTimeErrorEvening.setVisible(true);
            final Timeline timeline = new Timeline();
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2500),
                    new KeyValue(startTimeErrorEvening.visibleProperty(), false)));
            timeline.play();
        }
        else if (eveninghrs1.getText().length()>2){
            flashErrorMessage();
        }
        else if (eveningmin1.getText().length()>2){
            flashErrorMessage();
        }
        else if (eveninghrs2.getText().length()>2){
            flashErrorMessage();
        }
        else if (eveningmin2.getText().length()>2){
            flashErrorMessage();
        }
        else{
            startTimeErrorEvening.setVisible(false);
            displayerror.setVisible(false);
            return true;
        }
        return false;
    }
    @FXML
    public void SubmitChanges() throws Exception{

        if (morninghrs1.getText().trim().isEmpty() && morninghrs2.getText().trim().isEmpty()
                && eveninghrs1.getText().trim().isEmpty() && eveninghrs2.getText().trim().isEmpty()
                && morningmin1.getText().trim().isEmpty() && morningmin2.getText().trim().isEmpty()
                && eveningmin1.getText().trim().isEmpty() && eveningmin2.getText().trim().isEmpty()){
            flashErrorMessage();
        }
        else if(!validateEqualMorning() || !validateEqualEvening()){
            //All the stuff is handled in the booleans in the if statement
        }
        else if(!ampm1.getText().trim().equals("AM") && !ampm1.getText().trim().equals("PM")){
            flashErrorMessage();
        }
        else if(!ampm2.getText().trim().equals("AM") && !ampm2.getText().trim().equals("PM")){
            flashErrorMessage();
        }
        else if(!ampm4.getText().trim().equals("AM") && !ampm4.getText().trim().equals("PM")){
            flashErrorMessage();
        }
        else if(!ampm3.getText().trim().equals("AM") && !ampm3.getText().trim().equals("PM")){
            flashErrorMessage();
        }
        else if (morningmin1.getText().length()!=2){
            flashErrorMessage();
        }
        else if (morningmin2.getText().length()!=2){
            flashErrorMessage();
        }
        else if (eveningmin2.getText().length()!=2){
            flashErrorMessage();
        }
        else if (eveningmin1.getText().length()!=2){
            flashErrorMessage();
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
            flashSuccessMessage();

        }

        else{
            flashErrorMessage();
        }


    }

    private void flashSuccessMessage(){
        //THESE LINES ARE THE WINNERS! COPY PASTE EVERYWHERE! THEY WORK!
        //This is how you make a message flash on for only two and a half seconds.
        //Change the "displaySuccess" in "displaySuccess.visibleProperty()" to
        //the name of the message that you want to flash.
        displaySuccess.setVisible(true);
        final Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2500),
                new KeyValue(displaySuccess.visibleProperty(), false)));
        timeline.play();

    }

    private void flashErrorMessage(){
        displayerror.setVisible(true);
        final Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2500),
                new KeyValue(displayerror.visibleProperty(), false)));
        timeline.play();
    }

}
