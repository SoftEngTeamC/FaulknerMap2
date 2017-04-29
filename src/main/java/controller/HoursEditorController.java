package controller;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Hours;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class HoursEditorController extends Controller {
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
    private ToggleGroup ampm1;
    @FXML
    private ToggleGroup ampm2;
    @FXML
    private ToggleGroup ampm3;
    @FXML
    private ToggleGroup ampm4;
    @FXML
    private AnchorPane Parent;

    private Stage stage;

    public void initialize() {
        setButton(backBtn);
        addHourValidation(morninghrs1);
        addHourValidation(morninghrs2);
        addHourValidation(eveninghrs1);
        addHourValidation(eveninghrs2);

        addMinuteValidation(morningmin1);
        addMinuteValidation(morningmin2);
        addMinuteValidation(eveningmin1);
        addMinuteValidation(eveningmin2);

        startIdleListener(Parent, backBtn);
    }

    private void setStage(){
        stage = (Stage) backBtn.getScene().getWindow();
    }

    @FXML
    public void back() {
        setStage();
        switchScreen("view/AdminToolMenu.fxml", "Directory Editor", stage);
    }

    @FXML
    public void logout() {
        setStage();
        switchToMainScreen(logoutBtn);
    }

    @FXML
    public void SubmitChanges() {
        Hours currentHours = hoursService.find(1L);
        SimpleDateFormat dateParser = new SimpleDateFormat("h:m a");

        try {
            Date newMorningStart = dateParser.parse(morninghrs1.getText() + ":" + morningmin1.getText() + " " + selectedAMPM(ampm1));
            Date newMorningEnd = dateParser.parse(morninghrs2.getText() + ":" + morningmin2.getText() + " " + selectedAMPM(ampm2));
            Date newEveningStart = dateParser.parse(eveninghrs1.getText() + ":" + eveningmin1.getText() + " " + selectedAMPM(ampm3));
            Date newEveningEnd = dateParser.parse(eveninghrs2.getText() + ":" + eveningmin2.getText() + " " + selectedAMPM(ampm4));

            currentHours.setVisitingHoursMorningStart(newMorningStart);
            currentHours.setVisitingHoursMorningEnd(newMorningEnd);
            currentHours.setVisitingHoursEveningStart(newEveningStart);
            currentHours.setVisitingHoursEveningEnd(newEveningEnd);

            hoursService.merge(currentHours);

        } catch (ParseException e) {
            e.printStackTrace();
            System.err.println("Couldn't parse hours.");

        }
    }

    private void addMinuteValidation(TextField minuteField) {
        addIntegerRangeValidation(minuteField, 0, 59);
    }

    private void addHourValidation(TextField hourField) {
        addIntegerRangeValidation(hourField, 1, 12);
    }

    private void addIntegerRangeValidation(TextField integerField, int minimum, int maximum) {
        integerField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")) {
                ((StringProperty) observable).setValue("");
                return;
            }
            try {
                Integer number = Integer.parseInt(newValue);
                if (number >= minimum && number <= maximum) ((StringProperty) observable).setValue(number.toString());
                else ((StringProperty) observable).setValue(oldValue);
            } catch (NumberFormatException e) {
                ((StringProperty) observable).setValue(oldValue);
            }
        });
    }

    private static String selectedAMPM(ToggleGroup group) {
        RadioButton selected = (RadioButton) group.getSelectedToggle();
        return selected.getText();
    }
}