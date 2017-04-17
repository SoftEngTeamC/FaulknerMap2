package controller;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import model.Coordinate;
import model.Edge;
import model.Node;
import service.CoordinateService;
import service.EdgeService;
import service.NodeService;

import java.util.ArrayList;
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

    private static NodeService ns;

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

        ns = new NodeService();
    }

    static void ClearOldPaths() {
        System.out.println("Clearing Old Paths");
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

    static Slider checkSlider(int z) {
        switch (z) {
            case 1:
                return FirstFloorSlider;
            case 2:
                return SecondFloorSlider;
            case 3:
                return ThirdFloorSlider;
            case 4:
                return FourthFloorSlider;
            case 5:
                return FifthFloorSlider;
            case 6:
                return SixthFloorSlider;
            case 7:
                return SeventhFloorSlider;
            default:
                return null;
        }
    }

    //MakeLine take 2 points (effectively) and draws a line from point to point
    //this line is bounded to the image such that resizing does not effect the relative position of the line and image
    public static Line MakeLine(Edge e){
        double x1 = e.getStart().getLocation().getX();
        double y1 = e.getStart().getLocation().getY();
        double x2 = e.getEnd().getLocation().getX();
        double y2 = e.getEnd().getLocation().getY();
        int z = e.getStart().getLocation().getFloor();
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

        edge.setId(e.getId().toString());
        group1.getChildren().add(edge);
        return edge;
    }

    public static Circle MakeCircle(Node node) {
        double x = node.getLocation().getX();
        double y = node.getLocation().getY();
        int z = node.getLocation().getFloor();
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
        circle.fillProperty().setValue(Color.RED);

        circle.setId(node.getId().toString());
        group1.getChildren().addAll(circle);
        return circle;
    }

    static List<Circle> showNodes(int currFloor) {
        System.out.println("ShowNodes");
        NodeService NS = new NodeService();
        ShowNodesEdgesHelper.ClearOldPaths();
        List<Node> temp = NS.getNodesByFloor(currFloor);
        List<Circle> circles = new ArrayList<Circle>();
        for (Node n : temp){
            Circle circle = ShowNodesEdgesHelper.MakeCircle(n);
            circles.add(circle);
        }
        showEdges(currFloor);
        return circles;
    }

    static void showEdges(int currFloor) {
        System.out.println("ShowEdges");
        //Desired Clear old lines
        EdgeService es = new EdgeService();
        List<Edge> edges = es.getAllEdges();
        List<Edge> retEdges = new ArrayList<Edge>();
        for (Edge e : edges){
            if (e.getStart().getLocation().getFloor() == currFloor) {
                retEdges.add(e);
                ShowNodesEdgesHelper.MakeLine(e);
            }
        }
    }

    static List<Edge> getEdges(int currFloor){
        EdgeService es = new EdgeService();
        List<Edge> edges = es.getAllEdges();
        List<Edge> retEdges = new ArrayList<Edge>();
        for (Edge e : edges) {
            if (e.getStart().getLocation().getFloor() == currFloor) {
                retEdges.add(e);
            }
        }
        return retEdges;
    }

    static void resetDrawnShapeColors(int currFloor){
        ScrollPane Scrolly = ShowNodesEdgesHelper.checkScroll(currFloor);
        Group group = (Group) Scrolly.getContent();
        List<javafx.scene.Node> DrawnObjects = group.getChildren();
        for(int i=1;i<DrawnObjects.size();i++){
            try {
                Circle circle = (Circle) DrawnObjects.get(i);
                circle.fillProperty().setValue(Color.RED);
            }
            //found an edge instead
            catch(Exception e){
                Line line = (Line) DrawnObjects.get(i);
                line.setStrokeWidth(1);
                line.setStroke(Color.BLACK);
            }
        }
    }

    static void unPannable(boolean dec){
        if(dec){
            int currFloor = MapEditorController.getCurrFloor();
                List<Circle> circles = showNodes(currFloor);
                for (int j = 0; j < circles.size(); j++) {
                    setDraggable(circles.get(j), currFloor);
                }

        } else {
            FirstFloorScrollPane.setPannable(true);
            SecondFloorScrollPane.setPannable(true);
            ThirdFloorScrollPane.setPannable(true);
            FourthFloorScrollPane.setPannable(true);
            FifthFloorScrollPane.setPannable(true);
            SixthFloorScrollPane.setPannable(true);
            SeventhFloorScrollPane.setPannable(true);
        }
    }


    public void makeDraggable(int currFloor) {

    }

    static void setDraggable(Circle circle, int currFloor) {
        NodeService NS = new NodeService();
//        final double orgx = circle.getCenterX();
//        final double orgy =  circle.getCenterY();
////        final double[] transx = circle.getSouc
////        final double[] transy = new double[1];
//        final double[] offsetX = new double[1];
//        final double[] offsetY = new double[1];
//        NodeService ns = new NodeService();
        //  System.out.println("Start " + ns.find(Long.parseLong(circle.getId())).getLocation().toString());

//        EventHandler<MouseEvent> circleOnMousePressedEventHandler =
//                t -> {
//                    orgx[0] = t.getSceneX();
//                    orgy[0] = t.getSceneY();
//                    transx[0] = ((Circle) (t.getSource())).getTranslateX();
//                    transy[0] = ((Circle) (t.getSource())).getTranslateY();
//                    System.out.println("Pressed");
//                };
//
//        EventHandler<MouseEvent> circleOnMouseDraggedEventHandler =
//                t -> {
//                    ScrollPane tempScrollPane = ShowNodesEdgesHelper.checkScroll(currFloor);
//                    tempScrollPane.setPannable(false);
//                    offsetX[0] = t.getSceneX() - orgx;
//                    offsetY[0] = t.getSceneY() - orgy;
////                    double newTranslateX = transx[0] + offsetX[0];
////                    double newTranslateY = transy[0] + offsetY[0];
//
//                    ((Circle) (t.getSource())).setTranslateX(newTranslateX);
//                    ((Circle) (t.getSource())).setTranslateY(newTranslateY);
//                    Node node = NS.find(Long.parseLong(circle.getId()));
//                    CoordinateService CS = new CoordinateService();
//                    Coordinate coor = CS.find(node.getLocation().getId());
//                    coor.setX(coor.getX() + offsetX[0]);
//                    coor.setY(coor.getY() + offsetY[0]);
//                    CS.merge(coor);
//
////                    NS.merge(node);
//                 //   System.out.println("End " + node.getLocation().toString());
//                    System.out.println("Dragging");
//                };
//
//        EventHandler<DragEvent> circleOnMouseDragExitEventHandler =
//                t -> {
//
////                    Node node = NS.find(Long.parseLong(circle.getId()));
////                    CoordinateService CS = new CoordinateService();
////                    Coordinate coor = CS.find(node.getLocation().getId());
////                    coor.setX(coor.getX() + offsetX[0]);
////                    coor.setY(coor.getY() + offsetY[0]);
////                    CS.merge(coor);
////                    NS.merge(node);
//                    System.out.println("End ");
//                };
//
//
//        circle.setCursor(Cursor.CROSSHAIR);
//       // circle.setOnMousePressed(circleOnMousePressedEventHandler);
//        circle.setOnMouseDragged(circleOnMouseDraggedEventHandler);
//        circle.setOnDragDone(circleOnMouseDragExitEventHandler);
//        circle.setOnDragDropped(circleOnMouseDragExitEventHandler);
//       circle.setOnDragOver(circleOnMouseDragExitEventHandler);

        final double[] orgx = new double[1];
        final double[] orgy = new double[1];
        final double[] transx = new double[1];
        final double[] transy = new double[1];
        final double[] offsetX = new double[1];
        final double[] offsetY = new double[1];
        CoordinateService CS = new CoordinateService();


        EventHandler<MouseEvent> circleOnMousePressedEventHandler =
                t -> {
                    orgx[0] = t.getSceneX();
                    orgy[0] = t.getSceneY();
                    transx[0] = ((Circle) (t.getSource())).getTranslateX();
                    transy[0] = ((Circle) (t.getSource())).getTranslateY();
                    System.out.println("Pressed");
                };
        EventHandler<MouseEvent> circleOnMouseDraggedEventHandler =
                t -> {
                    ScrollPane tempScrollPane = ShowNodesEdgesHelper.checkScroll(currFloor);
                    Slider tempSlider = ShowNodesEdgesHelper.checkSlider(currFloor);
                    System.out.println("Slider: " + tempSlider.getValue());

                    double per = (tempSlider.getValue() - 730) / (5000 - 730);

                    tempScrollPane.setPannable(false);
                    offsetX[0] = t.getSceneX() - orgx[0];
                    offsetY[0] = t.getSceneY() - orgy[0];
                    double newTranslateX = transx[0] + offsetX[0];
                    double newTranslateY = transy[0] + offsetY[0];

//                    System.out.println("orgx " + orgx[0]);
//                    System.out.println("orgy " + orgy[0]);
//                    System.out.println("transx " + transx[0]);
//                    System.out.println("transy " + transy[0]);
//                    System.out.println("offsetX " + offsetX[0]);
//                    System.out.println("offsetY " + offsetY[0]);
//                    System.out.println("newTranslateX " + newTranslateX);
//                    System.out.println("newTranslateY " + newTranslateY);


                    ((Circle) (t.getSource())).setTranslateX(newTranslateX);
                    ((Circle) (t.getSource())).setTranslateY(newTranslateY);

//                    circle.centerXProperty().bind(Map1.fitWidthProperty().multiply(newTranslateX / ImgW));
//                    circle.centerYProperty().bind(Map1.fitWidthProperty().multiply(ImgR).multiply(newTranslateY / ImgH));
//                    circle.radiusProperty().bind(Map1.fitWidthProperty().multiply(10 / ImgW));

                    Node node = NS.find(Long.parseLong(circle.getId()));
                    System.out.println("start: " + node.getLocation().toString());
                    Coordinate coor = CS.find(node.getLocation().getId());
                    coor.setX(coor.getX() + (offsetX[0]));
                    coor.setY(coor.getY() + (offsetY[0]));
                    System.out.println("Per " + per);
                    CS.merge(coor);
                    Node node1 = NS.find(Long.parseLong(circle.getId()));
                    System.out.println("final: " + node1.getLocation().toString());
                    System.out.println("Dragging");
                };


        circle.setCursor(Cursor.CROSSHAIR);
        circle.setOnMousePressed(circleOnMousePressedEventHandler);
        circle.setOnMouseDragged(circleOnMouseDraggedEventHandler);
    }

}
