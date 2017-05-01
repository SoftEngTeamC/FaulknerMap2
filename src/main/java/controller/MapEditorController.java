package controller;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.Edge;
import model.Node;
import pathfinding.Map;
import util.ImageViewPane;

public class MapEditorController extends Controller {
    @FXML private VBox leftContainer;
    @FXML private Pane mapContainer;

    @FXML private Button floorButton1, floorButton2, floorButton3, floorButton4, floorButton5, floorButton6, floorButton7;

    private Map map = new Map(nodeService.getAllNodes());

    private ImageViewPane currentMapView;

    @FXML
    public void initialize() {
        setupFloorButtonListeners();
        showFloor(1);
        currentMapView.selectedNode.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                showSelectedNode();
            }
            if (oldValue != null && newValue == null) {
                showNodeSearch();
            }
        });
        currentMapView.selectedEdge.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                showSelectedEdge();
            }
            if (oldValue != null && newValue == null) {
                showNodeSearch();
            }
        });
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                searchResults.clear();
                searchResults.addAll(nodeService.search(newValue));
            }
        });
        searchResultsView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                currentMapView.selectedNode.set(newValue);
            }
        });
        neighborsView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                currentMapView.selectedEdge.set(edgeService.findByNodes(currentMapView.selectedNode.get(), newValue));
            }
        });
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
            showFloor(floor);
        };
    }

    private IntegerProperty currentFloor = new SimpleIntegerProperty();
    private void showFloor(int floor) {
        currentFloor.set(floor);
        mapContainer.getChildren().clear();
        ImageViewPane mapView = new ImageViewPane(ImageProvider.getImageByFloor(floor));
        currentMapView = mapView;
        mapView.prefHeightProperty().bind(mapContainer.heightProperty());
        mapView.prefWidthProperty().bind(mapContainer.widthProperty());
        mapContainer.getChildren().add(mapView);
        mapView.showAllEdges(edgeService.getAllEdgesOnFloor(floor));
        mapView.showAllNodes(nodeService.getNodesByFloor(floor));
    }

    private ObservableList<Node> neighbors = FXCollections.observableArrayList();
    private ListView<Node> neighborsView = new ListView<>(neighbors);
    private void showSelectedNode() {
        leftContainer.getChildren().clear();
        Node node = currentMapView.selectedNode.get();
        neighbors.clear();
        neighbors.addAll(nodeService.neighbors(node.getId()));
        leftContainer.getChildren().add(new Label(node.getName()));
        leftContainer.getChildren().add(neighborsView);
    }

    private void showSelectedEdge() {
        leftContainer.getChildren().clear();
        Edge edge = currentMapView.selectedEdge.get();
        leftContainer.getChildren().add(new Label(edge.getStart().getName() + "  < ---- >  " + edge.getEnd().getName()));

        HBox buttonBox = new HBox();

        Button disableButton = new Button("Disable");
        disableButton.setOnAction(e -> {
            edge.setDisabled(true);
            edgeService.merge(edge);
            currentMapView.wipe();
            currentMapView.showAllNodes(nodeService.getNodesByFloor(currentFloor.get()));
            currentMapView.showAllEdges(edgeService.getAllEdgesOnFloor(currentFloor.get()));
            showNodeSearch();
        });
        buttonBox.getChildren().add(disableButton);

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
            edgeService.remove(edge);
            currentMapView.selectedEdge.set(null);
            currentMapView.wipe();
            currentMapView.showAllNodes(nodeService.getNodesByFloor(currentFloor.get()));
            currentMapView.showAllEdges(edgeService.getAllEdgesOnFloor(currentFloor.get()));
            showNodeSearch();
        });
        buttonBox.getChildren().add(deleteButton);

        leftContainer.getChildren().add(buttonBox);
    }

    private ObservableList<Node> searchResults = FXCollections.observableArrayList();
    private ListView<Node> searchResultsView = new ListView<>(searchResults);
    private TextField searchField = new TextField();
    private void showNodeSearch() {
        leftContainer.getChildren().clear();
        leftContainer.getChildren().add(searchField);
        leftContainer.getChildren().add(searchResultsView);
    }
}


