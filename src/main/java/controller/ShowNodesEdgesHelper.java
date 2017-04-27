package controller;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import model.Edge;
import model.Node;
import service.EdgeService;
import service.NodeService;

import java.util.ArrayList;
import java.util.List;


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
    private static Group group;


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

    private static void bindFloor(ScrollPane FloorScrollPane, Slider FloorSlider, TabPane FloorViewsTabPane,
                                  String url) {
        FloorScrollPane.prefWidthProperty().bind(FloorViewsTabPane.widthProperty());
        FloorScrollPane.prefHeightProperty().bind(FloorViewsTabPane.heightProperty());
        ImageView FloorImageView = new ImageView();
        Image FirstFloorMapPic = ImageProvider.getImage(url);
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
        FloorSlider.setValue(FloorSlider.getMin() + ((FloorSlider.getMax() - FloorSlider.getMin()) * 0.25));
        FloorScrollPane.setHvalue((FloorScrollPane.getHmax() + FloorScrollPane.getHmin()) / 2);
        FloorScrollPane.setVvalue((FloorScrollPane.getVmax() + FloorScrollPane.getVmin()) / 2);
    }

    public void ZoomListener(Slider slider, ScrollPane scrlpn) {
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(newValue);
            //find center XY on old zoom based on current XY of scrollpane and old width

            //find center XY on Image relative to full image

            //set XY of scroll pane to be about new imageview
        });
    }

    public void PanningListener(Slider slider, ScrollPane scrlpn) {
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(newValue);
            //find center XY on old zoom based on current XY of scrollpane and old width

            //find center XY on Image relative to full image

            //set XY of scroll pane to be about new imageview
        });
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
    static void MakeLine(Edge e) {
        double x1 = e.getStart().getLocation().getX();
        double y1 = e.getStart().getLocation().getY();
        double x2 = e.getEnd().getLocation().getX();
        double y2 = e.getEnd().getLocation().getY();
        int z = e.getStart().getLocation().getFloor();
        ScrollPane Scrolly = ShowNodesEdgesHelper.checkScroll(z);

        assert Scrolly != null;
        Group group1 = (Group) Scrolly.getContent();
        group = group1;
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

        if (e.isDisabled()) {
            edge.getStrokeDashArray().addAll(2d, 10d);
        }

        edge.setId(e.getId().toString());
        group1.getChildren().add(edge);
    }

    public static void arrow(Edge e) {

        double arrowLength = 4;
        double arrowWidth = 7;

        double ex = e.getEnd().getLocation().getX();
        double ey = e.getEnd().getLocation().getY();
        double sx = e.getStart().getLocation().getX();
        double sy = e.getStart().getLocation().getY();

        Line arrow1 = new Line(0, 0, ex, ey);
        Line arrow2 = new Line(0, 0, ex, ey);

        arrow1.setEndX(ex);
        arrow1.setEndY(ey);
        arrow2.setEndX(ex);
        arrow2.setEndY(ey);

        if (ex == sx && ey == sy) {
            // arrow parts of length 0
            arrow1.setStartX(ex);
            arrow1.setStartY(ey);
            arrow2.setStartX(ex);
            arrow2.setStartY(ey);
        } else {
            double factor = arrowLength / Math.hypot(sx - ex, sy - ey);
            double factorO = arrowWidth / Math.hypot(sx - ex, sy - ey);

            double dx = (sx - ex) * factor;
            double dy = (sy - ey) * factor;

            double ox = (sx - ex) * factorO;
            double oy = (sy - ey) * factorO;

            arrow1.setStartX(ex + dx - oy);
            arrow1.setStartY(ey + dy + ox);
            arrow2.setStartX(ex + dx + oy);
            arrow2.setStartY(ey + dy - ox);
        }

        double xdiff1 = arrow1.getStartX() - arrow1.getEndX();
        double ydiff1 = arrow1.getStartY() - arrow1.getEndY();

        double xdiff2 = arrow2.getStartX() - arrow2.getEndX();
        double ydiff2 = arrow2.getStartY() - arrow2.getEndY();


        double x1 = e.getEnd().getLocation().getX();
        double y1 = e.getEnd().getLocation().getY();
        double x2 = x1 + xdiff1;
        double y2 = y1 + ydiff1;

        double x3 = x1 + xdiff2;
        double y3 = y1 + ydiff2;

        ImageView Map1 = (ImageView) group.getChildren().get(0);

        double ImgW = Map1.getImage().getWidth();
        double ImgH = Map1.getImage().getHeight();
        double ImgR = ImgH / ImgW;

        arrow1.startXProperty().bind(Map1.fitWidthProperty().multiply((x1 / ImgW)));
        arrow1.startYProperty().bind(Map1.fitWidthProperty().multiply(ImgR).multiply((y1 / ImgH)));
        arrow1.endXProperty().bind(Map1.fitWidthProperty().multiply((x2 / ImgW)));
        arrow1.endYProperty().bind(Map1.fitWidthProperty().multiply(ImgR).multiply((y2 / ImgH)));

        arrow2.startXProperty().bind(Map1.fitWidthProperty().multiply((x1 / ImgW)));
        arrow2.startYProperty().bind(Map1.fitWidthProperty().multiply(ImgR).multiply((y1 / ImgH)));
        arrow2.endXProperty().bind(Map1.fitWidthProperty().multiply((x3 / ImgW)));
        arrow2.endYProperty().bind(Map1.fitWidthProperty().multiply(ImgR).multiply((y3 / ImgH)));

        group.getChildren().addAll(arrow1, arrow2);

    }

    static Circle MakeCircle(Node node, Color color) {
        double x = node.getLocation().getX();
        double y = node.getLocation().getY();
        int z = node.getLocation().getFloor();
        // initial size of image and the image ratio
        ScrollPane Scrolly = ShowNodesEdgesHelper.checkScroll(z);

        //  System.out.println(Scrolly.getContent());
        assert Scrolly != null;
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
        circle.fillProperty().setValue(color);

        circle.setId(node.getId().toString());
        group1.getChildren().addAll(circle);
        return circle;
    }

    static List<Circle> showNodes(int currFloor) {
        NodeService NS = new NodeService();
        ShowNodesEdgesHelper.ClearOldPaths();
        List<Node> temp = NS.getNodesByFloor(currFloor);
        List<Circle> circles = new ArrayList<>();
        for (Node n : temp) {
            Circle circle = ShowNodesEdgesHelper.MakeCircle(n, Color.RED);
            circles.add(circle);
        }
        showEdges(currFloor);
        return circles;
    }

    private static void showEdges(int currFloor) {
        //   System.out.println("ShowEdges");
        //Desired Clear old lines
        EdgeService es = new EdgeService();
        List<Edge> edges = es.getAllEdges();
        for (Edge e : edges) {
            if (e.getStart().getLocation().getFloor() == currFloor) {
                ShowNodesEdgesHelper.MakeLine(e);
            }
        }
    }

    static List<Edge> getEdges(int currFloor) {
        EdgeService es = new EdgeService();
        List<Edge> edges = es.getAllEdges();
        List<Edge> retEdges = new ArrayList<>();
        for (Edge e : edges) {
            if (e.getStart().getLocation().getFloor() == currFloor) {
                retEdges.add(e);
            }
        }
        return retEdges;
    }

    static void resetDrawnShapeColors(int currFloor) {
        ScrollPane Scrolly = ShowNodesEdgesHelper.checkScroll(currFloor);
        assert Scrolly != null;
        Group group = (Group) Scrolly.getContent();
        List<javafx.scene.Node> DrawnObjects = group.getChildren();
        for (int i = 1; i < DrawnObjects.size(); i++) {
            try {
                Circle circle = (Circle) DrawnObjects.get(i);
                circle.fillProperty().setValue(Color.RED);
            }
            //found an edge instead
            catch (Exception e) {
                Line line = (Line) DrawnObjects.get(i);
                line.setStrokeWidth(1);
                line.setStroke(Color.BLACK);
            }
        }
    }

    // takes the desired XY and zoom of a map, and applies it to the given
    static void SetMapZoom(int x, int y, int zoom, ScrollPane scrlpn, Slider sldr) {
        sldr.setValue(zoom);
        scrlpn.setVvalue(y);
        scrlpn.setHvalue(x);
    }
}
