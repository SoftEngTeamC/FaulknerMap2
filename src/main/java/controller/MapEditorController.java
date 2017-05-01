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
import pathfinding.Map;
import pathfinding.MapNode;
import util.ImageViewPane;

import java.util.Collection;

public class MapEditorController extends Controller {
    @FXML private VBox leftContainer;
    @FXML private Pane mapContainer;

    @FXML private Button floorButton1, floorButton2, floorButton3, floorButton4, floorButton5, floorButton6, floorButton7;

    private ObjectProperty<MapNode> selectedNode = new SimpleObjectProperty<>();
    private ObjectProperty<Edge> selectedEdge = new SimpleObjectProperty<>();

    private Map map = new Map(nodeService.getAllNodes());


    @FXML
    public void initialize() {
        setupFloorButtonListeners();
    }

    private void setupFloorButtonListeners() {
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
            System.out.println("Button floor #" + floor);
            clearSelections();
            showFloor(floor);
        };
    }

    private void showFloor(int floor) {
        System.out.println("Showing floor #" + floor);
        mapContainer.getChildren().clear();
        ImageViewPane mapView = new ImageViewPane(ImageProvider.getImageByFloor(floor));
        mapView.prefHeightProperty().bind(mapContainer.heightProperty());
        mapView.prefWidthProperty().bind(mapContainer.widthProperty());
        mapContainer.getChildren().add(mapView);
        mapView.showAllNodes(nodeService.getNodesByFloor(floor));
    }

    private void clearSelections() {
        selectedEdge.set(null);
        selectedNode.set(null);
    }


    private void showSelectedNode() {
        leftContainer.getChildren().clear();
        MapNode node = selectedNode.get();
    }

    private void showSelectedEdge() {
        leftContainer.getChildren().clear();
        Edge edge = selectedEdge.get();
    }

    private void showNodeSearch() {
        leftContainer.getChildren().clear();
    }
}


