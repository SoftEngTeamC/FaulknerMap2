package util;

import controller.ImageProvider;
import javafx.beans.property.*;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import model.Coordinate;
import model.Edge;
import model.Node;
import pathfinding.MapNode;
import pathfinding.Path;
import service.CoordinateService;
import service.NodeService;

import java.util.*;


public class ImageViewPane extends Region {

    private ObjectProperty<ImageView> imageViewProperty = new SimpleObjectProperty<>();

    public ObjectProperty<ImageView> imageViewProperty() {
        return imageViewProperty;
    }

    public ImageView getImageView() {
        return imageViewProperty.get();
    }

    public void setImageView(ImageView imageView) {
        this.imageViewProperty.set(imageView);
    }


    private ObjectProperty<Pane> drawPaneProperty = new SimpleObjectProperty<>();

    public ObjectProperty<Pane> drawPaneProperty() {
        return drawPaneProperty;
    }

    public Pane getDrawPane() {
        return drawPaneProperty.get();
    }

    public void setDrawPane(Pane drawPane) {
        drawPaneProperty.set(drawPane);
    }


    private ObjectProperty<Rectangle2D> viewportProperty = new SimpleObjectProperty<>();

    public ObjectProperty<Rectangle2D> viewportProperty() {
        return viewportProperty;
    }

    public Rectangle2D getViewport() {
        return viewportProperty.get();
    }

    public void setViewport(Rectangle2D viewport) {
        viewportProperty.set(viewport);
        imageViewProperty.get().viewportProperty().set(viewport);
    }

    private DoubleProperty scaleProperty = new SimpleDoubleProperty();


    private ObjectProperty<Path> pathProperty = new SimpleObjectProperty<>();

    public void setPath(Path path) {
        BooleanProperty initialized = new SimpleBooleanProperty(false);
        needsLayoutProperty().addListener((observable, oldValue, newValue) -> {
//            System.out.println("new Value: " + newValue);
            if (!newValue && !initialized.get()) {
//                System.out.println("INITIALIZED");
                pathProperty.set(path);
                initialized.set(true);
            }
        });
    }

    public ObjectProperty<Node> selectedNode = new SimpleObjectProperty<>();
    public ObjectProperty<Edge> selectedEdge = new SimpleObjectProperty<>();


    @Override
    protected void layoutChildren() {
        ImageView imageView = imageViewProperty.get();
        if (imageView != null) {
            imageView.setFitWidth(getWidth());
            imageView.setFitHeight(getHeight());
            layoutInArea(imageView, 0, 0, getWidth(), getHeight(), 0, HPos.CENTER, VPos.CENTER);
        }

        Pane drawPane = getDrawPane();
        if (drawPane != null && imageView != null) {
            //TODO: Fix this transform on rescale
            double aspect = imageView.getImage().getWidth() / imageView.getImage().getHeight();
            double viewAspect = imageView.getFitWidth() / imageView.getFitHeight();
            boolean isWidthConstrained = viewAspect < aspect;
            boolean isHeightConstrained = viewAspect > aspect;

            double widthScale = isWidthConstrained ? (imageView.getFitWidth() - imageView.getBoundsInLocal().getWidth()) / imageView.getFitWidth() : 0;
            double heightScale = isHeightConstrained ? (imageView.getFitHeight() - imageView.getBoundsInLocal().getHeight()) / imageView.getFitHeight() : 0;

            double areaRatio = (imageView.getFitHeight() * imageView.getFitWidth()) / (imageView.getBoundsInLocal().getWidth() * imageView.getBoundsInLocal().getHeight());

            double scaleFactor = 1 / (scaleProperty.get());
            drawPane.scaleXProperty().set(scaleFactor);
            drawPane.scaleYProperty().set(scaleFactor);
            double xOffset = (imageView.getFitWidth() - imageView.getBoundsInLocal().getWidth()) / 2;
            double yOffset = (imageView.getFitHeight() - imageView.getBoundsInLocal().getHeight()) / 2;

            // Get the top left corner of the viewport
            Point2D topLeft = imageToContainerCoordinate(imageView, new Point2D(0, 0));
            layoutInArea(drawPane, topLeft.getX() + xOffset, topLeft.getY() + yOffset, drawPane.getWidth(), drawPane.getHeight(), 0, HPos.CENTER, VPos.CENTER);
        }
    }

    public ImageViewPane(Path path) {
        this(ImageProvider.getImageByFloor(path.getFloor(), false));
        pathProperty.setValue(path);
    }

    public ImageViewPane(Image image) {
        imageViewProperty.addListener((prop, oldImageView, newImageView) -> {
            if (oldImageView != null) {
                getChildren().remove(oldImageView);
            }
            if (newImageView != null) {
                getChildren().add(newImageView);
                setupImageViewListeners();
            }
        });
        drawPaneProperty.addListener((prop, oldDrawPane, newDrawPane) -> {
            if (oldDrawPane != null) {
                getChildren().remove(oldDrawPane);
            }
            if (newDrawPane != null) {
                getChildren().add(newDrawPane);
                newDrawPane.setPickOnBounds(false);
            }
        });

        pathProperty.addListener((observable, oldValue, newValue) -> {
            makePathCircles(newValue).forEach(circle -> getDrawPane().getChildren().add(circle));
            makeEdgeLines(newValue).forEach(line -> getDrawPane().getChildren().add(line));
            makeArrows(newValue).forEach(line -> getDrawPane().getChildren().add(line));
        });

        this.imageViewProperty.set(new ImageView(image));
        getImageView().setPreserveRatio(true);

        this.drawPaneProperty.set(new Pane());
        resetImageView();
    }

    private Group makeArrow(Edge e) {

        double arrowLength = 5;
        double arrowWidth = 1.1;

        Point2D edgeStart = imageToImageViewCoordinate(e.getStart().getLocation());
        Point2D edgeEnd = imageToImageViewCoordinate(e.getEnd().getLocation());

        double ex = edgeStart.getX();
        double ey = edgeStart.getY();
        double sx = edgeEnd.getX();
        double sy = edgeEnd.getY();

        Line arrow1 = new Line(0, 0, ex, ey);
        Line arrow2 = new Line(0, 0, ex, ey);

        arrow1.setEndX(ex);
        arrow1.setEndY(ey);
        arrow2.setEndX(ex);
        arrow2.setEndY(ey);

        if (ex == sx && ey == sy) {
            // makeArrow parts of length 0
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

        Group arrow = new Group();
        arrow.getChildren().add(arrow1);
        arrow.getChildren().add(arrow2);
        return arrow;
    }

    private List<Line> makeEdgeLines(Path path) {
        List<Line> lines = new LinkedList<>();
        for (Edge edge : path.edges()) {
            lines.add(makeEdgeLine(edge));
        }
        return lines;
    }

    private Line makeEdgeLine(Edge edge) {
        Point2D edgeStart = imageToImageViewCoordinate(edge.getStart().getLocation());
        Point2D edgeEnd = imageToImageViewCoordinate(edge.getEnd().getLocation());
        Line edgeLine = new Line(edgeStart.getX(), edgeStart.getY(), edgeEnd.getX(), edgeEnd.getY());

        if (edge.isDisabled()) {
            edgeLine.getStrokeDashArray().addAll(2d, 10d);
        }

        edgeLine.setOnMouseClicked(e -> {
            if (e.getClickCount() == 1) {
                selectedEdge.set(edge);
            }
        });

        return edgeLine;
    }

    private List<Group> makeArrows(Path path){
        List<Group> lines = new ArrayList<>();
        String first;
        for(int i = 0; i < path.numNodes()-1; i++){
            first = path.getNode(i).getModelNode().getName();
            if(!first.equals(path.edges().get(i).getStart().getName())) {
                lines.add(makeArrow(path.edges().get(i)));
            } else {
                Edge temp = new Edge(path.edges().get(i).getEnd(),
                        path.edges().get(i).getStart(), path.edges().get(i).getStart().getLocation().getFloor());
                lines.add(makeArrow(temp));
            }
        }
        return lines;
    }

    private List<Circle> makePathCircles(Path path) {
        List<Circle> circles = new LinkedList<>();
        circles.add(makeNodeCircle(path.getNode(0)));
        circles.add(makeNodeCircle(path.getNode(path.numNodes()-1)));

        return circles;
    }

    private Circle makeNodeCircle(Node node) {
        Point2D drawLocation = imageToImageViewCoordinate(node.getLocation());
        Circle nodeCircle = new Circle(drawLocation.getX(), drawLocation.getY(), 2);

        nodeCircle.setOnMouseClicked(e -> {
            if (e.getClickCount() == 1) {
                selectedNode.set(node);
            }
        });

        nodeCircle.setOnMouseDragged(e -> {
            nodeCircle.centerXProperty().set(e.getX());
            nodeCircle.centerYProperty().set(e.getY());
        });

        nodeCircle.setOnMouseReleased(e -> {
            Point2D newPoint = imageViewToImageCoordinate(new Point2D(e.getX(), e.getY()));
            node.getLocation().setX(newPoint.getX());
            node.getLocation().setY(newPoint.getY());
            CoordinateService cs = new CoordinateService();
            cs.merge(node.getLocation());
        });

        return nodeCircle;
    }


    private Circle makeNodeCircle(MapNode node) {
        return makeNodeCircle(node.getModelNode());
    }


    private static final int MIN_PIXELS = 400;
    private void setupImageViewListeners() {
        ObjectProperty<Point2D> mouseDown = new SimpleObjectProperty<>();

        getImageView().setOnMousePressed(e -> {
            selectedEdge.set(null);
            selectedNode.set(null);
            Point2D mousePress = imageViewToImageCoordinate(getImageView(), new Point2D(e.getX(), e.getY()));
            mouseDown.set(mousePress);
        });

        getImageView().setOnMouseDragged(e -> {
            Point2D dragPoint = imageViewToImageCoordinate(getImageView(), new Point2D(e.getX(), e.getY()));
            shift(dragPoint.subtract(mouseDown.get()));
            mouseDown.set(imageViewToImageCoordinate(getImageView(), new Point2D(e.getX(), e.getY())));
        });

        getImageView().setOnScroll(e -> {
            Rectangle2D viewport = getImageView().getViewport();
            double width = getImageView().getImage().getWidth();
            double height = getImageView().getImage().getHeight();
            double delta = -e.getDeltaY();
            double scale = clamp(Math.pow(1.005, delta),
                    // don't scale so we're zoomed in to fewer than MIN_PIXELS in any direction:
                    Math.min(MIN_PIXELS / viewport.getWidth(), MIN_PIXELS / viewport.getHeight()),
                    // don't scale so that we're bigger than image dimensions
                    Math.max(width / viewport.getWidth(), height / viewport.getHeight()));
            zoom(scale, e.getX(), e.getY());
        });

        getImageView().setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                Point2D clickOnMap = imageViewToImageCoordinate(getImageView(), new Point2D(e.getX(), e.getY()));
                NodeService nodeService = new NodeService();
                CoordinateService coordinateService = new CoordinateService();
                Coordinate location = new Coordinate(clickOnMap.getX(), clickOnMap.getY(), 1); // TODO FIX THIS YOU F UCK
                coordinateService.persist(location);
                Node newNode = new Node("new noodle", location);
                nodeService.persist(newNode);
                getDrawPane().getChildren().add(makeNodeCircle(newNode));
            }
        });
    }

    public void zoom(double scale, double x, double y) {
        Rectangle2D viewport = getImageView().getViewport();
        scaleProperty.set(scale * scaleProperty.get());
        Point2D mouse = imageViewToImageCoordinate(getImageView(), new Point2D(x, y));
        double width = getImageView().getImage().getWidth();
        double height = getImageView().getImage().getHeight();
        double newWidth = viewport.getWidth() * scale;
        double newHeight = viewport.getHeight() * scale;
        // To keep the visual point under the mouse from moving, we need
        // (x - newViewportMinX) / (x - currentViewportMinX) = scale
        // where x is the mouse X coordinate in the image

        // solving this for newViewportMinX gives

        // newViewportMinX = x - (x - currentViewportMinX) * scale

        // we then clamp this value so the image never scrolls out
        // of the floorView
        double newMinX = clamp(
                mouse.getX() - scale*(mouse.getX() - viewport.getMinX()),
                0,
                width - newWidth);
        double newMinY = clamp(
                mouse.getY() - scale*(mouse.getY() - viewport.getMinY()),
                0,
                height - newHeight
        );

        setViewport(new Rectangle2D(newMinX, newMinY, newWidth, newHeight));
    }


    private void resetImageView() {
        scaleProperty.set(1);
        double width = getImageView().getImage().getWidth();
        double height = getImageView().getImage().getHeight();
        setViewport(new Rectangle2D(0, 0, width, height));
    }

    private void shift(Point2D delta) {
        Rectangle2D viewport = getImageView().getViewport();

        double width = getImageView().getImage().getWidth();
        double height = getImageView().getImage().getHeight();

        double maxX = width - viewport.getWidth();
        double maxY = height - viewport.getHeight();

        double minX = clamp(viewport.getMinX() - delta.getX(), 0, maxX);
        double minY = clamp(viewport.getMinY() - delta.getY(), 0, maxY);

        setViewport(new Rectangle2D(minX, minY, viewport.getWidth(), viewport.getHeight()));
    }

    private static Point2D imageViewToImageCoordinate(ImageView floorView, Point2D floorViewCoordinates) {
        // Transform mouse coordinates on the floorView to the pixel coordinates of the image.
        double xProp = floorViewCoordinates.getX() / floorView.getBoundsInLocal().getWidth();
        double yProp = floorViewCoordinates.getY() / floorView.getBoundsInLocal().getHeight();

        Rectangle2D viewport = floorView.getViewport();
        return new Point2D(
                viewport.getMinX() + xProp*viewport.getWidth(),
                viewport.getMinY() + yProp*viewport.getHeight()
        );
    }

    private Point2D imageViewToImageCoordinate(Point2D ivc) {
        double pixelRatio = getImageView().getImage().getWidth() / getImageView().getBoundsInLocal().getWidth();
        return ivc.multiply(pixelRatio);
    }

    private Point2D imageToImageViewCoordinate(Coordinate coordinate) {
        double pixelRatio = getImageView().getBoundsInLocal().getWidth() / getImageView().getImage().getWidth();
        return new Point2D(coordinate.getX()*pixelRatio, coordinate.getY()*pixelRatio);
    }

    private Point2D imageToImageViewCoordinate(Point2D point) {
        double pixelRatio = getImageView().getBoundsInLocal().getWidth() / getImageView().getImage().getWidth();
        return point.multiply(pixelRatio);
    }

    private static Point2D imageToContainerCoordinate(ImageView floorView, Point2D imageCoordinates) {
        double xProp = floorView.getBoundsInLocal().getWidth() / floorView.getViewport().getWidth();
        double yProp = floorView.getBoundsInLocal().getHeight() / floorView.getViewport().getHeight();
        Rectangle2D viewport = floorView.getViewport();
        return new Point2D(
                (imageCoordinates.getX() - viewport.getMinX())*xProp,
                (imageCoordinates.getY() - viewport.getMinY())*yProp
        );
    }

    private static double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

    public void wipe() {
        getDrawPane().getChildren().clear();
    }

    private Map<String, Circle> nodeCircleMap = new HashMap<>();
    private BooleanProperty nodesAdded = new SimpleBooleanProperty(false);
    public void showAllNodes(Collection<Node> nodes) {
        needsLayoutProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && !nodesAdded.get()) {
                nodeCircleMap.clear();
                for (Node node : nodes) {
                    Circle nodeCircle = makeNodeCircle(node);
                    nodeCircleMap.put(node.getName(), nodeCircle);
                    getDrawPane().getChildren().add(nodeCircle);
                }
                nodesAdded.set(true);
            }
        });
    }

    private BooleanProperty edgesAdded = new SimpleBooleanProperty(false);
    public void showAllEdges(Collection<Edge> edges) {
        needsLayoutProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && !edgesAdded.get() && nodesAdded.get()) {
                for (Edge edge : edges) {
                    Line edgeLine = makeEdgeLine(edge);

                    if (nodeCircleMap.containsKey(edge.getStart().getName()) && nodeCircleMap.containsKey(edge.getEnd().getName())) {
                        edgeLine.startXProperty().bind(nodeCircleMap.get(edge.getStart().getName()).centerXProperty());
                        edgeLine.startYProperty().bind(nodeCircleMap.get(edge.getStart().getName()).centerYProperty());

                        edgeLine.endXProperty().bind(nodeCircleMap.get(edge.getEnd().getName()).centerXProperty());
                        edgeLine.endYProperty().bind(nodeCircleMap.get(edge.getEnd().getName()).centerYProperty());
                    } else {
                        System.out.println(edge + " didn't work.");
                    }
                    getDrawPane().getChildren().add(edgeLine);
                }
                edgesAdded.set(true);
            }
        });
    }
}