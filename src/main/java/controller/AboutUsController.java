package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;
import model.Edge;
import model.HospitalProfessional;

import model.Node;
import pathfinding.MapNode;
import pathfinding.PathFinder;
import service.EdgeService;

import pathfinding.Map;
import pathfinding.MapNode;
import pathfinding.PathFinder;
import service.EMFProvider;
import service.HospitalProfessionalService;
import service.NodeService;
import model.Hours;
import textDirections.MakeDirections;

import java.awt.event.MouseEvent;
import java.util.Collections;

import java.util.LinkedList;
import java.util.List;


public class AboutUsController extends Controller{

    @FXML
    private Button MeetTheTeamButton;

    @FXML
    private Button backbtn;

    @FXML
    public void OpenMeetTheGroup() throws Exception {
        // goto genres screen
        System.out.println("Meet the group");
        Stage stage = (Stage) MeetTheTeamButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/MeetTheGroup.fxml"));
        stage.setTitle("Meet The Group");
        stage.setScene(new Scene(root, 300, 300));
        stage.show();
    }


      @FXML
      public void back() throws Exception {
          switchScreen("view/Main.fxml", "Main", backbtn);
      }
}