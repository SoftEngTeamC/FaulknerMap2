package controller;

import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import model.Edge;
import model.Node;
import service.EdgeService;
import service.NodeService;

import java.util.List;

/**
 * Created by Gina on 4/13/17.
 */
class ShowNodesEdgesHelper {
    private static ScrollPane FirstFloorScrollPane;
    private static ScrollPane SecondFloorScrollPane;
    private static ScrollPane ThirdFloorScrollPane;
    private static ScrollPane FourthFloorScrollPane;
    private static ScrollPane FifthFloorScrollPane;
    private static ScrollPane SixthFloorScrollPane;
    private static ScrollPane SeventhFloorScrollPane;
    private static Slider FirstFloorSlider;
    private static Slider SecondFloorSlider;
    private static Slider ThirdFloorSlider;
    private static Slider FourthFloorSlider;
    private static Slider FifthFloorSlider;
    private static Slider SixthFloorSlider;
    private static Slider SeventhFloorSlider;
    private static TabPane FloorViewsTabPane;

    ShowNodesEdgesHelper(ScrollPane FirstFloorScrollPane, ScrollPane SecondFloorScrollPane,
                         ScrollPane ThirdFloorScrollPane, ScrollPane FourthFloorScrollPane,
                         ScrollPane FifthFloorScrollPane, ScrollPane SixthFloorScrollPane,
                         ScrollPane SeventhFloorScrollPane,
                         Slider FirstFloorSlider, Slider SecondFloorSlider,
                         Slider ThirdFloorSlider, Slider FourthFloorSlider,
                         Slider FifthFloorSlider, Slider SixthFloorSlider,
                         Slider SeventhFloorSlider,
                         TabPane FloorViewsTabPane) {
        ShowNodesEdgesHelper.FirstFloorScrollPane = FirstFloorScrollPane;
        ShowNodesEdgesHelper.SecondFloorScrollPane = SecondFloorScrollPane;
        ShowNodesEdgesHelper.ThirdFloorScrollPane = ThirdFloorScrollPane;
        ShowNodesEdgesHelper.FourthFloorScrollPane = FourthFloorScrollPane;
        ShowNodesEdgesHelper.FifthFloorScrollPane = FifthFloorScrollPane;
        ShowNodesEdgesHelper.SixthFloorScrollPane = SixthFloorScrollPane;
        ShowNodesEdgesHelper.SeventhFloorScrollPane = SeventhFloorScrollPane;
        ShowNodesEdgesHelper.FirstFloorSlider = FirstFloorSlider;
        ShowNodesEdgesHelper.SecondFloorSlider = SecondFloorSlider;
        ShowNodesEdgesHelper.ThirdFloorSlider = ThirdFloorSlider;
        ShowNodesEdgesHelper.FourthFloorSlider = FourthFloorSlider;
        ShowNodesEdgesHelper.FifthFloorSlider = FifthFloorSlider;
        ShowNodesEdgesHelper.SixthFloorSlider = SixthFloorSlider;
        ShowNodesEdgesHelper.SeventhFloorSlider = SeventhFloorSlider;
        ShowNodesEdgesHelper.FloorViewsTabPane = FloorViewsTabPane;
    }

    static void ClearOldPaths() {

        Group group1 = (Group) FirstFloorScrollPane.getContent();
        group1.getChildren().remove(1, group1.getChildren().size());
        Group group2 = (Group) SecondFloorScrollPane.getContent();
        group2.getChildren().remove(1, group2.getChildren().size());
        Group group3 = (Group) ThirdFloorScrollPane.getContent();
        group3.getChildren().remove(1, group3.getChildren().size());
        Group group4 = (Group) FourthFloorScrollPane.getContent();
        group4.getChildren().remove(1, group4.getChildren().size());
        Group group5 = (Group) FifthFloorScrollPane.getContent();
        group5.getChildren().remove(1, group5.getChildren().size());
        Group group6 = (Group) SixthFloorScrollPane.getContent();
        group6.getChildren().remove(1, group6.getChildren().size());
        Group group7 = (Group) SeventhFloorScrollPane.getContent();
        group7.getChildren().remove(1, group7.getChildren().size());
    }

    //----------------------------------Build Zoomable Maps----------------------------------------------
    static void InitializeMapViews() {
        //FIRST FLOOR
        bindFloor(FirstFloorScrollPane, FirstFloorSlider, FloorViewsTabPane, "images/1_thefirstfloor.png");

        //SECOND FLOOR
        bindFloor(SecondFloorScrollPane, SecondFloorSlider, FloorViewsTabPane, "images/2_thesecondfloor.png");

        //THIRD FLOOR
        bindFloor(ThirdFloorScrollPane, ThirdFloorSlider, FloorViewsTabPane, "images/3_thethirdfloor.png");

        //FOURTH FLOOR
        bindFloor(FourthFloorScrollPane, FourthFloorSlider, FloorViewsTabPane, "images/4_thefourthfloor.png");

        //FIFTH FLOOR
        bindFloor(FifthFloorScrollPane, FifthFloorSlider, FloorViewsTabPane, "images/5_thefifthfloor.png");

        //SIXTH FLOOR
        bindFloor(SixthFloorScrollPane, SixthFloorSlider, FloorViewsTabPane, "images/6_thesixthfloor.png");

        //SEVENTH FLOOR
        bindFloor(SeventhFloorScrollPane, SeventhFloorSlider, FloorViewsTabPane, "images/7_theseventhfloor.png");
    }

    static void bindFloor(ScrollPane FloorScrollPane, Slider FloorSlider, TabPane FloorViewsTabPane,
                          String url) {
        FloorScrollPane.prefWidthProperty().bind(FloorViewsTabPane.widthProperty());
        FloorScrollPane.prefHeightProperty().bind(FloorViewsTabPane.heightProperty());
        ImageView FloorImageView = new ImageView();
        Image FirstFloorMapPic = new Image(url);
        FloorImageView.setImage(FirstFloorMapPic);
        FloorImageView.setPreserveRatio(true);
        Group FloorGroup = new Group();
        FloorGroup.getChildren().add(FloorImageView);
        FloorScrollPane.setContent(FloorGroup);
        FloorScrollPane.setPannable(true);
        FloorScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        FloorScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        FloorSlider.setMax(FirstFloorMapPic.getWidth());
        FloorSlider.minProperty().bind(FloorViewsTabPane.widthProperty());
        FloorImageView.fitWidthProperty().bind(FloorSlider.valueProperty());
    }

    static ScrollPane checkScroll(int z) {
        switch (z) {
            case 1:
                return FirstFloorScrollPane;
            case 2:
                return SecondFloorScrollPane;
            case 3:
                return ThirdFloorScrollPane;
            case 4:
                return FourthFloorScrollPane;
            case 5:
                return FifthFloorScrollPane;
            case 6:
                return SixthFloorScrollPane;
            case 7:
                return SeventhFloorScrollPane;
            default:
                System.out.println("You gave MakeCircle() a floor that doesn't exist, or it isnt an int");
                return null;
        }
    }

    //MakeLine take 2 points (effectively) and draws a line from point to point
    //this line is bounded to the image such that resizing does not effect the relative position of the line and image
    static void MakeLine(double x1, double y1, double x2, double y2, int z) {
        ScrollPane Scrolly = ShowNodesEdgesHelper.checkScroll(z);

        Group group1 = (Group) Scrolly.getContent();
        ImageView Map1 = (ImageView) group1.getChildren().get(0);

        double ImgW = Map1.getImage().getWidth();
        double ImgH = Map1.getImage().getHeight();
        double ImgR = ImgH / ImgW;

        Line edge = new Line();
        //the points are bound to the fit width property of the image and scaled by the initial image ratio
        edge.startXProperty().bind(Map1.fitWidthProperty().multiply((x1 / ImgW)));
        edge.startYProperty().bind(Map1.fitWidthProperty().multiply(ImgR).multiply((y1 / ImgH)));
        edge.endXProperty().bind(Map1.fitWidthProperty().multiply((x2 / ImgW)));
        edge.endYProperty().bind(Map1.fitWidthProperty().multiply(ImgR).multiply((y2 / ImgH)));
        group1.getChildren().add(edge);
    }

    public static void MakeCircle(double x, double y, int z, String name) {
        // initial size of image and the image ratior
        ScrollPane Scrolly = ShowNodesEdgesHelper.checkScroll(z);

        //  System.out.println(Scrolly.getContent());
        Group group1 = (Group) Scrolly.getContent();

        ImageView Map1 = (ImageView) group1.getChildren().get(0);

        double ImgW = Map1.getImage().getWidth();
        double ImgH = Map1.getImage().getHeight();
        double ImgR = ImgH / ImgW;

        Circle circle = new Circle();
        //These bind the center positions relative to the width property of the image
        //the new center is calculated using the initial ratios
        circle.centerXProperty().bind(Map1.fitWidthProperty().multiply(x / ImgW));
        circle.centerYProperty().bind(Map1.fitWidthProperty().multiply(ImgR).multiply(y / ImgH));
        circle.radiusProperty().bind(Map1.fitWidthProperty().multiply(10 / ImgW));
        circle.fillProperty().setValue(Paint.valueOf("#ff2d1f"));


        Text text = new Text(x, y
                , name);

        text.setBoundsType(TextBoundsType.VISUAL);
        StackPane stack = new StackPane();
        stack.getChildren().addAll(text);

        group1.getChildren().addAll(circle);

        //  group1.getChildren().addAll(circle);

        group1.setOnMouseClicked(event -> {
            System.out.print(event.getSceneX() + " ");
            System.out.println(event.getSceneY());
        });
    }

    public static void showNodes(int currFloor){
        NodeService NS = new NodeService();
        ShowNodesEdgesHelper.ClearOldPaths();
        List<Node> temp = NS.getNodesByFloor(currFloor);
        for (Node n : temp) {
            ShowNodesEdgesHelper.MakeCircle(n.getLocation().getX(), n.getLocation().getY(),
                    n.getLocation().getFloor(), n.getName());
        }
        EdgeService es = new EdgeService();
        List<Edge> edges = es.getAllEdges();
        for (Edge e : edges) {
            if (e.getStart().getLocation().getFloor() == currFloor) {
                ShowNodesEdgesHelper.MakeLine(e.getStart().getLocation().getX(), e.getStart().getLocation().getY(),
                        e.getEnd().getLocation().getX(), e.getEnd().getLocation().getY(),
                        e.getStart().getLocation().getFloor());
            }
        }
    }

}
