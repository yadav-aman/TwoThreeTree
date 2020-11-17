package applicationTwoThreeTree;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import twoThreeTreeAlgo.Node;
import twoThreeTreeAlgo.Operations;

public class TwoThreeTreeApp extends Application{
    ScrollPane scrollPane = new ScrollPane();
    private final DoubleProperty zoomProperty = new SimpleDoubleProperty(1.0d);
    private final DoubleProperty deltaY = new SimpleDoubleProperty(0.0d);

    private final Group group = new Group();
    @Override
    public void start(Stage primaryStage) throws Exception{
        // Set Title
        primaryStage.setTitle("2-3 Tree Visualization");
        primaryStage.setHeight(700);
        primaryStage.setWidth(1500);
        // creating an instance of 2-3 tree
        Operations<Integer> tree = new Operations<>();
        // create DrawTree object
        DrawTree viewTree = new DrawTree(tree,primaryStage.getWidth());
        // creating a border pane
        BorderPane treePane = new BorderPane();
        // Insert scroll pane
        scrollPane.setContent(viewTree);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPannable(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        AnchorPane.setTopAnchor(scrollPane, 10.0d);
        AnchorPane.setRightAnchor(scrollPane, 10.0d);
        AnchorPane.setBottomAnchor(scrollPane, 10.0d);
        AnchorPane.setLeftAnchor(scrollPane, 10.0d);
        AnchorPane root = new AnchorPane();
        group.getChildren().add(viewTree);
        PanAndZoomPane panAndZoomPane = new PanAndZoomPane();
        zoomProperty.bind(panAndZoomPane.myScale);
        deltaY.bind(panAndZoomPane.deltaY);
        panAndZoomPane.getChildren().add(group);

        SceneGestures sceneGestures = new SceneGestures(panAndZoomPane);

        scrollPane.setContent(panAndZoomPane);
        panAndZoomPane.toBack();
        scrollPane.addEventFilter( MouseEvent.MOUSE_CLICKED, sceneGestures.getOnMouseClickedEventHandler());
        scrollPane.addEventFilter( MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        scrollPane.addEventFilter( MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        scrollPane.addEventFilter( ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
        root.getChildren().add(scrollPane);
        // set Tree pane
        treePane.setCenter(root);
        // Create HBox for controls
        HBox controls = new HBox(10);
        // HBox for Height and vertices
        HBox status = new HBox();
        // text field to insert keys
        TextField keyText =new TextField();
        keyText.setPrefWidth(50);
        keyText.setAlignment(Pos.BOTTOM_CENTER);
        // Buttons to insert, delete and Search
        Button insert = new Button("Insert");
        insert.setStyle("-fx-background-color: #69e597");
        insert.setFont(Font.font(Font.getDefault().toString(), FontWeight.BOLD,10));

        Button delete = new Button("Delete");
        delete.setStyle("-fx-background-color: #ee8084;");
        delete.setFont(Font.font(Font.getDefault().toString(), FontWeight.BOLD,10));

        Button find = new Button("Search");
        find.setStyle("-fx-background-color: #7982e3");
        find.setFont(Font.font(Font.getDefault().toString(), FontWeight.BOLD,10));

        Button clear = new Button("Clear");
        clear.setStyle("-fx-background-color: #f50606");
        clear.setFont(Font.font(Font.getDefault().toString(), FontWeight.BOLD,10));

        insert.setOnMouseClicked(e->{
            try{
                int key =Integer.parseInt(keyText.getText());
                keyText.setText( key+5+"");
                tree.add(key);
                viewTree.displayTree(null);
                status.getChildren().clear();
                status.getChildren().add(viewTree.updateMessage());
            }
            catch (NumberFormatException ex){
                System.out.println("Wrong format provided");
            }
        });

        delete.setOnMouseClicked(e->{
            try{
                int key =Integer.parseInt(keyText.getText());
                keyText.setText("");
                if (tree.isEmpty()){
                    System.out.println("Tree is empty");
                }
                else{
                    tree.remove(key);
                    viewTree.displayTree(null);
                    status.getChildren().clear();
                    status.getChildren().add(viewTree.updateMessage());
                }
            }
            catch (NumberFormatException ex){
                System.out.println("Wrong format provided");
            }
        });

        clear.setOnMouseClicked(e->{
            tree.clear();
            viewTree.setPrefSize(1300,800);
            viewTree.displayTree(null);
            status.getChildren().clear();
            status.getChildren().add(viewTree.updateMessage());
        });

        find.setOnMouseClicked(e->{
            try {
                String key = keyText.getText();
                keyText.setText("");
                if (!tree.search(Integer.parseInt(key))) {
                    System.out.println("No such item in the tree");
                    viewTree.displayTree(null);
                } else {
                    viewTree.displayTree(key);
                }
            }
            catch(NumberFormatException ex){
                System.out.println("Wrong format provided");
            }
        });

        // position controls
        BorderPane.setMargin(controls,new Insets(15,15,15,15));
        controls.setAlignment(Pos.TOP_CENTER);
        // add elements to HBox
        Text enterKey = new Text("Enter Key: ");
        enterKey.setFont(Font.font(Font.getDefault().toString(), FontWeight.BOLD,20));
        Label blankLabel = new Label();
        blankLabel.setPrefWidth(40);
        controls.getChildren().addAll(enterKey,keyText,insert,delete,find,blankLabel,clear);
        // set HBox to the top
        treePane.setTop(controls);
        // position status
        status.setAlignment(Pos.BOTTOM_CENTER);
        treePane.setBottom(status);
        Scene scene = new Scene(treePane);
        primaryStage.setScene(scene);
        // Show Stage
        primaryStage.show();
    }

    //---------------------------INNER CLASS TO DRAW TREE-----------------------------------
    class DrawTree extends Pane{
        private Operations<Integer> tree;
        // constructor
        DrawTree(Operations<Integer> tree,double width){
            this.tree = tree;
            this.setPrefSize(width,700);
        }

        //draw nodes
        private void draw2Node(String key, double xCord, double yCord, boolean isLeaf, double horizontalGap,double verticalGap, String keyToCheck) {
            Rectangle box = new Rectangle(xCord, yCord, 50, 50);
            if (key.equals(keyToCheck)){
                box.setFill(Color.DARKORANGE);
                box.setStroke(Color.DARKRED);
            }
            else {
                box.setFill(Color.LIGHTGREEN);
                box.setStroke(Color.BLACK);
            }
            box.setArcHeight(15);
            box.setArcWidth(15);
            Text keyText = new Text(xCord + 5,yCord + 38, key);
            keyText.setFill(Color.BLACK);
            keyText.setFont(Font.font(Font.getDefault().toString(), FontWeight.EXTRA_BOLD,25));
            if (isLeaf)
            {
                // if leaf node then draw node only
                getChildren().addAll(box,keyText);
            }
            else{
                // if not leaf node then draw node and lines
                Line leftLine = new Line(xCord,yCord+45,xCord - horizontalGap + 25,yCord+verticalGap);
                Line rightLine = new Line(xCord+50,yCord+45,xCord + horizontalGap + 25,yCord+verticalGap);
                leftLine.setStrokeWidth(4);
                rightLine.setStrokeWidth(4);
                getChildren().addAll(box,keyText,leftLine,rightLine);
            }
        }
        private void draw3Node(String key1, String key2, double xCord, double yCord, boolean isLeaf, double horizontalGap, double verticalGap, String keyToCheck){
            Rectangle box1 = new Rectangle(xCord,yCord,50,50);
            Rectangle box2 = new Rectangle(xCord+50,yCord,50,50);
            if(key1.equals(keyToCheck)) {
                box1.setFill(Color.DARKORANGE);
                box1.setStroke(Color.DARKRED);
            } else {
                box1.setFill(Color.LIGHTGREEN);
                box1.setStroke(Color.BLACK);
            }
            if(key2.equals(keyToCheck)) {
                box2.setFill(Color.DARKORANGE);
                box2.setStroke(Color.DARKRED);
            } else {
                box2.setFill(Color.LIGHTGREEN);
                box2.setStroke(Color.BLACK);
            }
            box1.setArcHeight(15);
            box1.setArcWidth(15);
            box2.setArcHeight(15);
            box2.setArcWidth(15);

            Text keyText1 = new Text(xCord + 5,yCord + 38, key1);
            keyText1.setFill(Color.BLACK);
            keyText1.setFont(Font.font(Font.getDefault().toString(), FontWeight.EXTRA_BOLD,25));
            Text keyText2 = new Text(xCord + 55,yCord + 38, key2);
            keyText2.setFill(Color.BLACK);
            keyText2.setFont(Font.font(Font.getDefault().toString(), FontWeight.EXTRA_BOLD,25));

            if (isLeaf)
            {
                // if leaf node then draw node only
                this.getChildren().addAll(box1,box2,keyText1,keyText2);
            }
            else{
                // if not leaf node then draw node and lines
                Line leftLine = new Line(xCord,yCord+45,xCord - horizontalGap + 45,yCord+verticalGap);
                Line rightLine = new Line(xCord+100,yCord+45,xCord+horizontalGap+25,yCord+verticalGap);
                Line middleLine = new Line(xCord+50,yCord+50,xCord+50,yCord+verticalGap);
                leftLine.setStrokeWidth(4);
                rightLine.setStrokeWidth(4);
                middleLine.setStrokeWidth(4);
                this.getChildren().addAll(box1,box2,keyText1,keyText2,leftLine,middleLine,rightLine);
            }
        }

        public void displayTree(String key){
            // first clear the screen
            this.getChildren().clear();
            double treeHeight = tree.height();
            // make space for more nodes
            if(tree.height()>3){
                this.setPrefSize(Math.pow(3,tree.height()-3)*1500,1500);
            }
            // display tree is root is not null
            if (tree.getRoot() != null){
                displayTree(tree.getRoot(),getWidth()/2,5,getWidth()/3, 150,treeHeight, key);
            }
        }
        private void displayTree(Node<Integer> n, double xCord, double yCord, double horizontalGap, double verticalGap,double treeHeight, String key){
            if(n == null)
                return;
            displayTree(n.getLeftNode(),xCord-horizontalGap,yCord+verticalGap, horizontalGap/3, verticalGap+1*treeHeight,treeHeight, key);
            if(n.getRightElement() != null)
                displayTree(n.getMidNode(),xCord+25,yCord+verticalGap, horizontalGap/3, verticalGap+1*treeHeight,treeHeight, key);
            else
                displayTree(n.getMidNode(),xCord+horizontalGap,yCord+verticalGap, horizontalGap/3,verticalGap+1*treeHeight,treeHeight, key);
            displayTree(n.getRightNode(),xCord+horizontalGap,yCord+verticalGap, horizontalGap/3,verticalGap+1*treeHeight,treeHeight, key);
            if(n.is2Node()){
                if(n.getLeftElement() != null)
                    draw2Node(n.getLeftElement().toString(),xCord,yCord,n.isLeaf(), horizontalGap, verticalGap, key);
            }
            if(n.is3Node()){
                draw3Node(n.getLeftElement().toString(),n.getRightElement().toString(),xCord,yCord,n.isLeaf(),horizontalGap,verticalGap, key);
            }
        }

        public Text updateMessage(){
            Text message = new Text("Height: " + tree.height() + ", Vertices: " + tree.getVertices());
            message.setFont(Font.font(Font.getDefault().toString(), FontWeight.BOLD,20));
            return  message;
        }
    }
    // source - https://stackoverflow.com/questions/29788184/javafx-8-dynamic-node-scaling
    class PanAndZoomPane extends Pane {

        public static final double DEFAULT_DELTA = 1.3d;
        DoubleProperty myScale = new SimpleDoubleProperty(1.0);
        public DoubleProperty deltaY = new SimpleDoubleProperty(0.0);
        private Timeline timeline;


        public PanAndZoomPane() {

            this.timeline = new Timeline(60);

            // add scale transform
            scaleXProperty().bind(myScale);
            scaleYProperty().bind(myScale);
        }


        public double getScale() {
            return myScale.get();
        }

        public void setScale( double scale) {
            myScale.set(scale);
        }

        public void setPivot( double x, double y, double scale) {
            // note: pivot value must be untransformed, i. e. without scaling
            // timeline that scales and moves the node
            timeline.getKeyFrames().clear();
            timeline.getKeyFrames().addAll(
                    new KeyFrame(Duration.millis(200), new KeyValue(translateXProperty(), getTranslateX() - x)),
                    new KeyFrame(Duration.millis(200), new KeyValue(translateYProperty(), getTranslateY() - y)),
                    new KeyFrame(Duration.millis(200), new KeyValue(myScale, scale))
            );
            timeline.play();

        }

        public void fitWidth () {
            double scale = getParent().getLayoutBounds().getMaxX()/getLayoutBounds().getMaxX();
            double oldScale = getScale();

            double f = scale - oldScale;

            double dx = getTranslateX() - getBoundsInParent().getMinX() - getBoundsInParent().getWidth()/2;
            double dy = getTranslateY() - getBoundsInParent().getMinY() - getBoundsInParent().getHeight()/2;

            double newX = f*dx + getBoundsInParent().getMinX();
            double newY = f*dy + getBoundsInParent().getMinY();

            setPivot(newX, newY, scale);

        }

        public void resetZoom () {
            double scale = 1.0d;

            double x = getBoundsInParent().getWidth()/2;
            double y = getTranslateY();

            setPivot(x, y, scale);
        }

        public double getDeltaY() {
            return deltaY.get();
        }
        public void setDeltaY( double dY) {
            deltaY.set(dY);
        }
    }


    /**
     * Mouse drag context used for scene and nodes.
     */
    class DragContext {

        double mouseAnchorX;
        double mouseAnchorY;

        double translateAnchorX;
        double translateAnchorY;

    }

    /**
     * Listeners for making the scene's canvas draggable and zoomable
     */
    public class SceneGestures {

        private DragContext sceneDragContext = new DragContext();

        PanAndZoomPane panAndZoomPane;

        public SceneGestures( PanAndZoomPane canvas) {
            this.panAndZoomPane = canvas;
        }

        public EventHandler<MouseEvent> getOnMouseClickedEventHandler() {
            return onMouseClickedEventHandler;
        }

        public EventHandler<MouseEvent> getOnMousePressedEventHandler() {
            return onMousePressedEventHandler;
        }

        public EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
            return onMouseDraggedEventHandler;
        }

        public EventHandler<ScrollEvent> getOnScrollEventHandler() {
            return onScrollEventHandler;
        }

        private EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

            public void handle(MouseEvent event) {

                sceneDragContext.mouseAnchorX = event.getX();
                sceneDragContext.mouseAnchorY = event.getY();

                sceneDragContext.translateAnchorX = panAndZoomPane.getTranslateX();
                sceneDragContext.translateAnchorY = panAndZoomPane.getTranslateY();

            }

        };

        private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {

                panAndZoomPane.setTranslateX(sceneDragContext.translateAnchorX + event.getX() - sceneDragContext.mouseAnchorX);
                panAndZoomPane.setTranslateY(sceneDragContext.translateAnchorY + event.getY() - sceneDragContext.mouseAnchorY);

                event.consume();
            }
        };

        /**
         * Mouse wheel handler: zoom to pivot point
         */
        private EventHandler<ScrollEvent> onScrollEventHandler = new EventHandler<ScrollEvent>() {

            @Override
            public void handle(ScrollEvent event) {

                double delta = PanAndZoomPane.DEFAULT_DELTA;

                double scale = panAndZoomPane.getScale(); // currently we only use Y, same value is used for X
                double oldScale = scale;

                panAndZoomPane.setDeltaY(event.getDeltaY());
                if (panAndZoomPane.deltaY.get() < 0) {
                    scale /= delta;
                } else {
                    scale *= delta;
                }

                double f = (scale / oldScale)-1;

                double dx = (event.getX() - (panAndZoomPane.getBoundsInParent().getWidth()/2 + panAndZoomPane.getBoundsInParent().getMinX()));
                double dy = (event.getY() - (panAndZoomPane.getBoundsInParent().getHeight()/2 + panAndZoomPane.getBoundsInParent().getMinY()));

                panAndZoomPane.setPivot(f*dx, f*dy, scale);

                event.consume();

            }
        };

        /**
         * Mouse click handler
         */
        private EventHandler<MouseEvent> onMouseClickedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    if (event.getClickCount() == 2) {
                        panAndZoomPane.resetZoom();
                    }
                }
                if (event.getButton().equals(MouseButton.SECONDARY)) {
                    if (event.getClickCount() == 2) {
                        panAndZoomPane.fitWidth();
                    }
                }
            }
        };
    }
}
