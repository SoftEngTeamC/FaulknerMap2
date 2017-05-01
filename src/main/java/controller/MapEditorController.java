package controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.Edge;
import pathfinding.MapNode;
import util.ImageViewPane;

public class MapEditorController extends Controller {
    @FXML private VBox leftContainer;
    @FXML private Pane mapContainer;

    @FXML private Button floorButton1, floorButton2, floorButton3, floorButton4, floorButton5, floorButton6, floorButton7;

    private ObjectProperty<MapNode> selectedNode = new SimpleObjectProperty<>();
    private ObjectProperty<Edge> selectedEdge = new SimpleObjectProperty<>();

    @FXML
    public void initialize() {
        setupButtonListeners();
    }

    private void setupButtonListeners() {
        floorButton1.setOnAction(makeFloorButtonEvent(1));
        floorButton2.setOnAction(makeFloorButtonEvent(2));
        floorButton3.setOnAction(makeFloorButtonEvent(3));
        floorButton4.setOnAction(makeFloorButtonEvent(4));
        floorButton5.setOnAction(makeFloorButtonEvent(5));
        floorButton6.setOnAction(makeFloorButtonEvent(6));
        floorButton7.setOnAction(makeFloorButtonEvent(7));
    }

    private EventHandler<ActionEvent> makeFloorButtonEvent(int floor) {
        return event -> {
            clearSelections();
            showFloor(floor);
        };
    }

    private void showFloor(int floor) {
        mapContainer.getChildren().clear();
        mapContainer.getChildren().add(new ImageViewPane(ImageProvider.getImageByFloor(floor)));
    }

    private void clearSelections() {
        selectedEdge.set(null);
        selectedNode.set(null);
    }


    private void showSelectedNode() {

    }

    private void showSelectedEdge() {

    }

    private void showNodeSearch() {

    }
}


