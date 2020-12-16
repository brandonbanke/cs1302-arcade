package cs1302.arcade;
 
import javafx.application.Application;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.util.*;
import java.io.*;


/**
 * Class for game menu.
 */

public class ArcadeApp extends Application {

    /**
     * Creates GUI and menu options.
     *
     * @param stage Stage var
     */
    public void start (Stage stage) {
        VBox vBox = new VBox ();
        Scene scene = new Scene (vBox, 300, 300);
        HBox title = new HBox ();
        Label titleText = new Label ("Arcade App");
        title.getChildren().add(titleText);
        Button game = new Button ("Tetris");
        HBox gameChoice = new HBox ();
        gameChoice.getChildren().addAll(game);
        vBox.getChildren().addAll(title, gameChoice);
        game.setOnAction(e -> {
            TetrisApp tetrisGame = new TetrisApp ();
            VBox vbox1 = new VBox();
            Scene s = new Scene(vbox1, 240, 500);
            stage.sizeToScene();
            tetrisGame.start(stage);
        });
 
        stage.setMaxWidth(300);
        stage.setMaxHeight(300);
        stage.sizeToScene();
        stage.setTitle("cs1302-arcadeApp");
        stage.setScene(scene);
        stage.show();
    }
}
 
