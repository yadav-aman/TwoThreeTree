package applicationTwoThreeTree;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import twoThreeTreeAlgo.Node;
import twoThreeTreeAlgo.Operations;

public class TwoThreeTreeApp extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception{
        // Set Title
        primaryStage.setTitle("2-3 Tree Visualization");
        // open app maximised
        primaryStage.setMaximized(true);
        // creating an instance of 2-3 tree
        Operations<Integer> tree = new Operations<>();
        // create DrawTree object
        DrawTree viewTree = new DrawTree(tree);
        // creating a border pane
        BorderPane treePane = new BorderPane();
        // set Tree pane
        treePane.setCenter(viewTree);
        // Create HBox for controls
        HBox controls = new HBox(10);
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

        insert.setOnMouseClicked(e->{
            try{
                int key =Integer.parseInt(keyText.getText());
                keyText.setText("");
                tree.add(key);
                viewTree.displayTree();
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
                    viewTree.displayTree();
                }
            }
            catch (NumberFormatException ex){
                System.out.println("Wrong format provided");
            }
        });

        // position controls
        BorderPane.setMargin(controls,new Insets(15,15,15,15));
        controls.setAlignment(Pos.TOP_CENTER);
        // add elements to HBox
        Text enterKey = new Text("Enter Key: ");
        enterKey.setFont(Font.font(Font.getDefault().toString(), FontWeight.BOLD,20));
        controls.getChildren().addAll(enterKey,keyText,insert,delete,find);
        // set HBox to the top
        treePane.setTop(controls);
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
        }

        private void draw2Node(String key, double xCord, double yCord, boolean isLeaf, double horizontalGap){
            Rectangle box = new Rectangle(xCord,yCord,30,30);
            box.setFill(Color.LIGHTGREEN);
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
                Line leftLine = new Line(xCord,yCord+25,xCord - horizontalGap + 15,yCord+60);
                Line rightLine = new Line(xCord+30,yCord+25,xCord + horizontalGap + 15,yCord+60);
                getChildren().addAll(box,keyText,leftLine,rightLine);
            }
        }
        private void draw3Node(String key1, String key2, double xCord, double yCord, boolean isLeaf, double horizontalGap){
            Rectangle box1 = new Rectangle(xCord,yCord,30,30);
            Rectangle box2 = new Rectangle(xCord+30,yCord,30,30);
            box1.setFill(Color.RED);
            box2.setFill(Color.BLUE);
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
                Line leftLine = new Line(xCord,yCord+25,xCord - horizontalGap + 15,yCord+60);
                Line rightLine = new Line(xCord+60,yCord+25,xCord+horizontalGap+10,yCord+60);
                Line middleLine = new Line(xCord+30,yCord+30,xCord+30,yCord+60);
                this.getChildren().addAll(box1,box2,keyText1,keyText2,leftLine,middleLine,rightLine);
            }
        }
        public void displayTree(){
            // first clear the screen
            this.getChildren().clear();
            // display tree is root is not null
            if (tree.getRoot() != null){
                displayTree(tree.getRoot(),getWidth()/2,0,getWidth()/4, 60);
            }
            Text status = new Text(getWidth()/4,getHeight()-20,"Height: " + tree.height() + ", Vertices: " + tree.getVertices());
            status.setFont(Font.font(Font.getDefault().toString(), FontWeight.BOLD,20));
            getChildren().add(status);

        }
        private void displayTree(Node<Integer> n, double w, double h, double horizontalGap, double verticalGap){
            if(n == null)
                return;
            displayTree(n.getLeftNode(),w-horizontalGap,h+verticalGap, horizontalGap/3, verticalGap);
            if(n.getRightElement() != null)
                displayTree(n.getMidNode(),w+15,h+verticalGap, horizontalGap/3, verticalGap);
            else
                displayTree(n.getMidNode(),w+horizontalGap,h+verticalGap, horizontalGap/3,verticalGap);
            displayTree(n.getRightNode(),w+horizontalGap,h+verticalGap, horizontalGap/3,verticalGap);
            if(n.is2Node()){
                if(n.getLeftElement() != null)
                    draw2Node(n.getLeftElement().toString(),w,h,n.isLeaf(), horizontalGap);
            }
            if(n.is3Node()){
                draw3Node(n.getLeftElement().toString(),n.getRightElement().toString(),w,h,n.isLeaf(),horizontalGap);
            }
        }
    }
}
