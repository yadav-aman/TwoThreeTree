package applicationTwoThreeTree;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
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
        BorderPane controlsPane = new BorderPane();
        // Create HBox
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


        // position controls
        BorderPane.setMargin(controls,new Insets(15,15,15,15));
        controls.setAlignment(Pos.TOP_CENTER);
        // add elements to HBox
        Text enterKey = new Text("Enter Key: ");
        enterKey.setFont(Font.font(Font.getDefault().toString(), FontWeight.BOLD,20));
        controls.getChildren().addAll(enterKey,keyText,insert,delete,find);
        // set HBox to the top
        controlsPane.setTop(controls);
        Scene scene = new Scene(controlsPane);
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
    }
}
