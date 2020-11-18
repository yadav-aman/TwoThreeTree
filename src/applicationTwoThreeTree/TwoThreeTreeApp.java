package applicationTwoThreeTree;

import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import twoThreeTreeAlgo.Node;
import twoThreeTreeAlgo.Operations;

public class TwoThreeTreeApp extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception{
        // Set Title
        primaryStage.setTitle("2-3 Tree Visualization");
        primaryStage.setHeight(700);
        primaryStage.setWidth(1500);
        // creating an instance of 2-3 tree
        Operations<Integer> tree = new Operations<>();
        // create DrawTree object
        DrawTree viewTree = new DrawTree(tree);
        // creating a border pane
        BorderPane treePane = new BorderPane();
        // Insert scroll pane
        ZoomableScrollPane scrollPane = new ZoomableScrollPane(viewTree);
        // set Tree pane
        treePane.setCenter(scrollPane);
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

        Button root = new Button("Root");
        root.setStyle("-fx-background-color: #d83def");
        root.setFont(Font.font(Font.getDefault().toString(), FontWeight.BOLD,10));

        Button clear = new Button("Clear");
        clear.setStyle("-fx-background-color: #f50606");
        clear.setFont(Font.font(Font.getDefault().toString(), FontWeight.BOLD,10));

        insert.setOnMouseClicked(e->{
            try{
                int key =Integer.parseInt(keyText.getText());
                keyText.setText( key+5+"");
                boolean isExist = false;
                if(!tree.isEmpty()){
                    isExist = tree.search(key);
                }
                tree.add(key);
                viewTree.displayTree(null);
                status.getChildren().clear();
                if(isExist)
                    status.getChildren().add(viewTree.updateMessage(key + " is already in the tree"));
                else
                    status.getChildren().add(viewTree.updateMessage(key + " is added"));
            }
            catch (NumberFormatException ex){
                System.out.println("Wrong format provided");
            }
        });

        delete.setOnMouseClicked(e->{
            try{
                int key =Integer.parseInt(keyText.getText());
                keyText.setText("");
                boolean isExist = tree.search(key);
                if (tree.isEmpty()){
                    viewTree.displayTree(null);
                    status.getChildren().clear();
                    status.getChildren().add(viewTree.updateMessage("Tree is empty"));
                }
                else{
                    tree.remove(key);
                    viewTree.displayTree(null);
                    status.getChildren().clear();
                    if(isExist)
                        status.getChildren().add(viewTree.updateMessage(key + " is deleted"));
                    else
                        status.getChildren().add(viewTree.updateMessage(key + " is not in the tree"));
                }
            }
            catch (NumberFormatException ex){
                System.out.println("Wrong format provided");
            }
        });

        root.setOnMouseClicked(e->{
            scrollPane.setHvalue(scrollPane.getVmax()/2);
            scrollPane.setVvalue(scrollPane.getHmin());
        });

        clear.setOnMouseClicked(e->{
            tree.clear();
            viewTree.setPrefSize(1300,800);
            viewTree.displayTree(null);
            status.getChildren().clear();
            status.getChildren().add(viewTree.updateMessage("Tree is cleared"));
        });

        find.setOnMouseClicked(e->{
            try {
                String key = keyText.getText();
                keyText.setText("");

                if (!tree.search(Integer.parseInt(key))) {
                    viewTree.displayTree(null);
                    status.getChildren().clear();
                    status.getChildren().add(viewTree.updateMessage(key + " not found"));
                }
                else {
                    viewTree.displayTree(key);
                    status.getChildren().clear();
                    status.getChildren().add(viewTree.updateMessage(key + " found"));
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
        controls.getChildren().addAll(enterKey,keyText,insert,delete,find,root,blankLabel,clear);
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
        DrawTree(Operations<Integer> tree){
            this.tree = tree;
            this.setPrefSize(1300,800);
        }

        //draw nodes
        private void draw2Node(String key, double xCord, double yCord, boolean isLeaf, double horizontalGap,double verticalGap, String keyToCheck) {
            Rectangle box = new Rectangle(xCord, yCord, 30, 30);
            if (key.equals(keyToCheck)){
                box.setFill(Color.DARKORANGE);
                box.setStroke(Color.DARKRED);
            }
            else {
                box.setFill(Color.LIGHTGREEN);
            }
            box.setArcHeight(15);
            box.setArcWidth(15);
            Text keyText = new Text(xCord + 2,yCord + 18, key);
            keyText.setFill(Color.BLACK);
            keyText.setFont(Font.font(Font.getDefault().toString(), FontWeight.EXTRA_BOLD,15));
            if (isLeaf)
            {
                // if leaf node then draw node only
                getChildren().addAll(box,keyText);
            }
            else{
                // if not leaf node then draw node and lines
                Line leftLine = new Line(xCord,yCord+25,xCord - horizontalGap + 15,yCord+verticalGap);
                Line rightLine = new Line(xCord+30,yCord+25,xCord + horizontalGap + 15,yCord+verticalGap);
                getChildren().addAll(box,keyText,leftLine,rightLine);
            }
        }
        private void draw3Node(String key1, String key2, double xCord, double yCord, boolean isLeaf, double horizontalGap, double verticalGap, String keyToCheck){
            Rectangle box1 = new Rectangle(xCord,yCord,30,30);
            Rectangle box2 = new Rectangle(xCord+30,yCord,30,30);
            if(key1.equals(keyToCheck)) {
                box1.setFill(Color.DARKORANGE);
                box1.setStroke(Color.DARKRED);
            } else {
                box1.setFill(Color.LIGHTGREEN);
            }
            if(key2.equals(keyToCheck)) {
                box2.setFill(Color.DARKORANGE);
                box2.setStroke(Color.DARKRED);
            } else {
                box2.setFill(Color.LIGHTGREEN);
            }
            box1.setArcHeight(15);
            box1.setArcWidth(15);
            box2.setArcHeight(15);
            box2.setArcWidth(15);

            Text keyText1 = new Text(xCord + 2,yCord + 18, key1);
            keyText1.setFill(Color.BLACK);
            keyText1.setFont(Font.font(Font.getDefault().toString(), FontWeight.EXTRA_BOLD,15));
            Text keyText2 = new Text(xCord + 32,yCord + 18, key2);
            keyText2.setFill(Color.BLACK);
            keyText2.setFont(Font.font(Font.getDefault().toString(), FontWeight.EXTRA_BOLD,15));

            if (isLeaf)
            {
                // if leaf node then draw node only
                this.getChildren().addAll(box1,box2,keyText1,keyText2);
            }
            else{
                // if not leaf node then draw node and lines
                Line leftLine = new Line(xCord,yCord+25,xCord - horizontalGap + 15,yCord+verticalGap);
                Line rightLine = new Line(xCord+60,yCord+25,xCord+horizontalGap+15,yCord+verticalGap);
                Line middleLine = new Line(xCord+30,yCord+30,xCord+30,yCord+verticalGap);
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
                displayTree(n.getMidNode(),xCord+15,yCord+verticalGap, horizontalGap/3, verticalGap+1*treeHeight,treeHeight, key);
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

        public Text updateMessage(String msg){
            Text message = new Text("Height: " + tree.height() + ", Vertices: " + tree.getVertices()+"\t\t Status: "+ msg);
            message.setFont(Font.font(Font.getDefault().toString(), FontWeight.BOLD,20));
            return  message;
        }
    }
    class ZoomableScrollPane extends ScrollPane {
        private double scaleValue = 0.7;
        private double zoomIntensity = 0.02;
        private javafx.scene.Node target;
        private javafx.scene.Node zoomNode;

        public ZoomableScrollPane(javafx.scene.Node target) {
            super();
            this.target = target;
            this.zoomNode = new Group(target);
            setContent(outerNode(zoomNode));

            setPannable(true);
            setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            setFitToHeight(true); //center
            setFitToWidth(true); //center

            updateScale();
        }

        private javafx.scene.Node outerNode(javafx.scene.Node node) {
            javafx.scene.Node outerNode = centeredNode(node);
            outerNode.setOnScroll(e -> {
                e.consume();
                onScroll(e.getTextDeltaY(), new Point2D(e.getX(), e.getY()));
            });
            return outerNode;
        }

        private javafx.scene.Node centeredNode(javafx.scene.Node node) {
            VBox vBox = new VBox(node);
            vBox.setAlignment(Pos.CENTER);
            return vBox;
        }

        private void updateScale() {
            target.setScaleX(scaleValue);
            target.setScaleY(scaleValue);
        }

        private void onScroll(double wheelDelta, Point2D mousePoint) {
            double zoomFactor = Math.exp(wheelDelta * zoomIntensity);

            Bounds innerBounds = zoomNode.getLayoutBounds();
            Bounds viewportBounds = getViewportBounds();

            // calculate pixel offsets from [0, 1] range
            double valX = this.getHvalue() * (innerBounds.getWidth() - viewportBounds.getWidth());
            double valY = this.getVvalue() * (innerBounds.getHeight() - viewportBounds.getHeight());

            scaleValue = scaleValue * zoomFactor;
            updateScale();
            this.layout(); // refresh ScrollPane scroll positions & target bounds

            // convert target coordinates to zoomTarget coordinates
            Point2D posInZoomTarget = target.parentToLocal(zoomNode.parentToLocal(mousePoint));

            // calculate adjustment of scroll position (pixels)
            Point2D adjustment = target.getLocalToParentTransform().deltaTransform(posInZoomTarget.multiply(zoomFactor - 1));

            // convert back to [0, 1] range
            // (too large/small values are automatically corrected by ScrollPane)
            Bounds updatedInnerBounds = zoomNode.getBoundsInLocal();
            this.setHvalue((valX + adjustment.getX()) / (updatedInnerBounds.getWidth() - viewportBounds.getWidth()));
            this.setVvalue((valY + adjustment.getY()) / (updatedInnerBounds.getHeight() - viewportBounds.getHeight()));
        }
    }
}
