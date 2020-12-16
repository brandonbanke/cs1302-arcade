package cs1302.arcade;

import java.util.*;
import java.net.*;
import java.io.*;
import java.util.Random;
import javafx.event.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.GridPane;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.animation.*;
import javafx.util.Duration;
import javafx.scene.shape.Shape;
import cs1302.arcade.TetrisPiece;
import javafx.scene.layout.HBox;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.animation.Animation.Status;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

/**
 * Application subclass for {@code ArcadeApp}.
 * @version 2019.fa
 */
public class TetrisApp extends Application {
    private Scene scene;
    private HBox paneDisplay;
    private GridPane pane = new GridPane();
    private Rectangle r = new Rectangle(20, 20);
    private Shape [][] block;
    private StackPane [][] stackPane = new StackPane [24][12];
    private Shape [][] boardAr = new Shape [24][12];
    private boolean [][] currentBlock = new boolean [24][12];
    private boolean isDoneFalling = false;
    private boolean canMoveLeft = true;
    private boolean canMoveRight = true;
    private Timeline timeline;
    private double dropSpeed = .5;
    private int score = 0;
    private KeyFrame keyFrame;
    private int rotationNum;
    private int rotCount;

    public void doPreparations () {
        for (int i = 0; i < 24; i++) {
            RowConstraints row = new RowConstraints(20);
            pane.getRowConstraints().add(row);
            for (int n = 0; n < 12; n++) {
                ColumnConstraints column = new ColumnConstraints(20);
                pane.getColumnConstraints().add(column);
            }
        }
        for (int i = 0; i < 24; i++) {
            for (int n = 0; n < 12; n++) {
                currentBlock[i][n] = false;
            }
        }                
        for (int i = 0; i < 24; i++) {
            for (int n = 0; n < 12; n++) {
                boardAr[i][n] = new Rectangle (20, 20, Color.TRANSPARENT);
            }
        }
        for (int i = 0; i < 24; i++) {
            for (int n = 0; n < 12; n++) {
                stackPane[i][n] = new StackPane ();
                stackPane[i][n].getChildren().add(boardAr[i][n]);
                pane.add(stackPane[i][n], n, i);
            }
        }
        pane.setGridLinesVisible(true);
    } 


    private void blockDown() {
        for (int i = 22; i >= 0; i--) {
            for (int n = 11; n >= 0; n--) {
                if (boardAr[i][n].getFill() != Color.TRANSPARENT && currentBlock[i][n]) {
                    if (boardAr[i + 1][n].getFill() == Color.TRANSPARENT
                        && !isDoneFalling) {
                        boardAr[i + 1][n].setFill(boardAr[i][n].getFill());
                        boardAr[i][n].setFill(Color.TRANSPARENT);
                        currentBlock[i + 1][n] = true;
                        currentBlock[i][n] = false;
                    }
                }
            }
        }
        if (isAtBottom() == true) {
            isDoneFalling = true;
            newRound();
        }
    }
    
    
    /**
     * This is the method that causes the block to fall.
     */
        public void moveBlockDown () {
        Platform.runLater(() -> {
            EventHandler<ActionEvent> handler = event -> {
                blockDown();
            };
            keyFrame = new KeyFrame(Duration.seconds(dropSpeed), handler);
            timeline = new Timeline();
            timeline.setCycleCount(timeline.INDEFINITE);
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();
        });
    } //moveBlockDown

    public boolean isAtBottom() {
        for (int i = 0; i < 24; i++) {
            for (int n = 0; n < 12; n++) {
                if (currentBlock[i][n] && i == 23) {
                    return true;
                }
                if ((currentBlock[i][n] && boardAr[i][n].getFill()
                     != Color.TRANSPARENT) && (!currentBlock[i + 1][n]
                                        && boardAr[i + 1][n].getFill() != Color.TRANSPARENT)) {
                    return true;
                }
            } // for
        } // for
        return false;
    } // isAtBottom
    
    /**
     * Method used to reset the boolean array of currentBlock for a new Shape.
     */
    public void resetFalling () {
        for (int i = 0; i < 24; i++) {
            for (int n = 0; n < 12; n++) {
                currentBlock[i][n] = false;
            } //for
        } //for
    } //printFalling

    /**
     * This method creates a new block at the top of the grid.
     */
    public void spawnNew () {
        block = new Shape [2][4];
        int j = 0;
        int k = 0;
        while (j < 2) {
            while (k < 4) {
                block[j][k] = new Rectangle(20, 20, Color.TRANSPARENT);
                k++;
            } //for
            j++;
        } //for
        block = TetrisPiece.spawnBlock();
        for (int i = 0; i < 2; i++) {
            for (int n = 0; n < 4; n++) {
                if (block[i][n] != null) {
                    if (block[i][n].getFill() != Color.TRANSPARENT) {
                        currentBlock[i][n + 5] = true;
                        boardAr[i][n + 5].setFill(block[i][n].getFill());
                    } else {
                        boardAr[i][n + 5].setFill(Color.TRANSPARENT);
                    } //else
                } else {
                    spawnNew();
                }
            } //for
        } //for
        isDoneFalling = false;
    } //spawnNew

    /**
     * Creates the next round by placing the block permanently and creating a new block.
     */
    public void newRound () {
        deleteFilled();
        resetFalling();
        if (isGameOver()) {
            timeline.stop();
            Stage gOScreen = new Stage ();
            VBox loseVbox = new VBox ();
            Scene gameOver = new Scene (loseVbox, 200, 200);
            scene.setFill(Color.BLACK);
            HBox gOHbox = new HBox ();
            Label gOText = new Label ("GAME OVER");
            HBox scoreHbox = new HBox ();
            Label scoreText = new Label ();
            scoreText.setText(String.valueOf(score));
            gOHbox.getChildren().add(gOText);
            scoreHbox.getChildren().add(scoreText);
            loseVbox.getChildren().addAll(gOHbox, scoreHbox);
            gOScreen.setMaxWidth(200);
            gOScreen.setMaxHeight(200);
            gOScreen.setTitle("cs1302-arcade! Game Over!");
            gOScreen.setScene(gameOver);
            gOScreen.sizeToScene();
            gOScreen.show();
        } //if
        spawnNew();
    } //newRound
    
    /**
     * Method to check if the game is over or not.
     *@return boolean
     */
    public boolean isGameOver () {
        for (int i = 0; i < 12; i++) {
            if (boardAr[0][i].getFill() != Color.TRANSPARENT && !currentBlock[0][i]) {
                return true;
            } //if
        } //for
        return false;
    } //isGameOver

    /**
     * Method to be called once user clicks left that moves shape 1 unit left.
     */
    public void moveLeft () {
        for (int n = 0; n < 12; n++) {
            for (int i = 0; i < 23; i++) {
                if (canMoveLeft) {
                    if (boardAr[i][n].getFill() != Color.TRANSPARENT && currentBlock[i][n]) {
                        if (n == 0) {
                            canMoveLeft = false;
                        } else {
                            canMoveLeft = true;
                            if (!isDoneFalling) {
                                if (boardAr[i][n - 1].getFill() == Color.TRANSPARENT) {
                                    boardAr[i][n - 1].setFill(boardAr[i][n].getFill());
                                    boardAr[i][n].setFill(Color.TRANSPARENT);
                                    currentBlock[i][n - 1] = true;
                                    currentBlock[i][n] = false;
                                    canMoveRight = true;
                                } //if
                            } //if
                        } //else
                    } //if
                } //if
            } //for
        } //for
    } //moveLeft

    /**
     * Method to be called once user clicks right that moves shape 1 unit right.
     */
    public void moveRight () {
        for (int n = 11; n >= 0; n--) {
            for (int i = 23; i >= 0; i--) {
                if (canMoveRight) {
                    if (boardAr[i][n].getFill() != Color.TRANSPARENT && currentBlock[i][n]) {
                        if (n == 11 || boardAr[i][n + 1].getFill() != Color.TRANSPARENT) {
                            canMoveRight = false;
                            break;
                        } else {
                            canMoveRight = true;
                            if (!isDoneFalling) {
                                if (boardAr[i][n + 1].getFill() == Color.TRANSPARENT) {
                                    boardAr[i][n + 1].setFill(boardAr[i][n].getFill());
                                    boardAr[i][n].setFill(Color.TRANSPARENT);
                                    currentBlock[i][n + 1] = true;
                                    currentBlock[i][n] = false;
                                    canMoveLeft = true;
                                } //if
                            } //if
                        } //else
                    } //if
                } //if
            } //for
        } //for
    } //moveRight

    public boolean canRotate() {
        for (int i = 0; i < 24; i++) {
            for (int n = 0; n < 12; n++) {
                for (int j = 0; j < 2; j++) {
                    for (int k = 0; k < 4; k++) {
                        if (currentBlock[i][n]) {
                            if (i + j >= 24 || n - k < 0 || n + k >= 12 || i - j < 0) {
                                return false;
                            } // if
                            if (boardAr[i + j][n + k].getFill() != Color.TRANSPARENT
                                && !currentBlock[i + j][n + k]) {
                                return false;
                            } // if
                        } // if
                    } // for
                } // for
            } // for
        } // for
        return true;
    } // canRotate

    public void rotateLine() {
        rotCount = 0;
        boolean hasDone0 = false;
        boolean hasDone1 = false;
        boolean hasDone2 = false;
        boolean hasDone3 = false;
        for (int i = 0; i < 24; i++) {
            for (int n = 0; n < 12; n++) {
                if (currentBlock[i][n]) {
                    boardAr[i][n].setFill(Color.TRANSPARENT);
                    currentBlock[i][n] = false;
                    if (rotCount == 0 && !hasDone0) {
                        boardAr[i - 2][n + 1].setFill(Color.AQUA);
                        currentBlock[i - 2][n + 1] = true;
                        hasDone0 = true;
                        rotCount++;
                    } else if (rotCount == 1 && !hasDone1) {
                        boardAr[i - 1][n].setFill(Color.AQUA);
                        currentBlock[i - 1][n] = true;
                        hasDone1 = true;
                        rotCount++;
                    } else if (rotCount == 2 && !hasDone2) {
                        boardAr[i][n - 1].setFill(Color.AQUA);
                        currentBlock[i][n - 1] = true;
                        hasDone2 = true;
                        rotCount++;
                    } else if (rotCount == 3 && !hasDone3) {
                        boardAr[i - 2][n - 2].setFill(Color.AQUA);
                        currentBlock[i - 2][n - 2] = true;
                        hasDone3 = true;
                        rotCount++;
                    }
                }
            }
        }
    } // rotateLine

    public void rotateZ() {
        rotCount = 0;
        boolean hasDone0 = false;
        boolean hasDone1 = false;
        boolean hasDone2 = false;
        boolean hasDone3 = false;
        for (int i = 0; i < 24; i++) {
            for (int n = 0; n < 12; n++) {
                if (currentBlock[i][n]) {
                    boardAr[i][n].setFill(Color.TRANSPARENT);
                    currentBlock[i][n] = false;
                    if (rotCount == 0 && !hasDone0) {
                        boardAr[i][n].setFill(Color.RED);
                        currentBlock[i][n] = true;
                        hasDone0 = true;
                        rotCount++;
                    } else if (rotCount == 1 && !hasDone1) {
                        boardAr[i][n].setFill(Color.RED);
                        currentBlock[i][n] = true;
                        hasDone1 = true;
                        rotCount++;
                    } else if (rotCount == 2 && !hasDone2) {
                        boardAr[i][n- 1].setFill(Color.RED);
                        currentBlock[i][n - 1] = true;
                        hasDone2 = true;
                        rotCount++;
                    } else if (rotCount == 3 && !hasDone3) {
                        boardAr[i - 2][n - 1].setFill(Color.RED);
                        currentBlock[i - 2][n - 1] = true;
                        hasDone3 = true;
                        rotCount++;
                    }
                }
            }
        }
    }

    public void rotateS() {
        rotCount = 0;
        boolean hasDone0 = false;
        boolean hasDone1 = false;
        boolean hasDone2 = false;
        boolean hasDone3 = false;
        for (int i = 0; i < 24; i++) {
            for (int n = 0; n < 12; n++) {
                if (currentBlock[i][n]) {
                    boardAr[i][n].setFill(Color.TRANSPARENT);
                    currentBlock[i][n] = false;
                    if (rotCount == 0 && !hasDone0) {
                        boardAr[i][n].setFill(Color.GREEN);
                        currentBlock[i][n] = true;
                        hasDone0 = true;
                        rotCount++;
                    } else if (rotCount == 1 && !hasDone1) {
                        boardAr[i][n].setFill(Color.GREEN);
                        currentBlock[i][n] = true;
                        hasDone1 = true;
                        rotCount++;
                    } else if (rotCount == 2 && !hasDone2) {
                        boardAr[i - 2][n + 1].setFill(Color.GREEN);
                        currentBlock[i - 2][n + 1] = true;
                        hasDone2 = true;
                        rotCount++;
                    } else if (rotCount == 3 && !hasDone3) {
                        boardAr[i][n + 1].setFill(Color.GREEN);
                        currentBlock[i][n + 1] = true;
                        hasDone3 = true;
                        rotCount++;
                    }
                }
            }
        }
    }
    
    public void rotateL() {
        rotCount = 0;
        boolean hasDone0 = false;
        boolean hasDone1 = false;
        boolean hasDone2 = false;
        boolean hasDone3 = false;
        for (int i = 0; i < 24; i++) {
            for (int n = 0; n < 12; n++) {
                if (currentBlock[i][n]) {
                    boardAr[i][n].setFill(Color.TRANSPARENT);
                    currentBlock[i][n] = false;
                    if (rotCount == 0 && !hasDone0) {
                        boardAr[i - 1][n + 2].setFill(Color.BLUE);
                        currentBlock[i - 1][n + 2] = true;
                        hasDone0 = true;
                        rotCount++;
                    } else if (rotCount == 1 && !hasDone1) {
                        boardAr[i - 2][n + 1].setFill(Color.BLUE);
                        currentBlock[i - 2][n + 1] = true;
                        hasDone1 = true;
                        rotCount++;
                    } else if (rotCount == 2 && !hasDone2) {
                        boardAr[i][n].setFill(Color.BLUE);
                        currentBlock[i][n] = true;
                        hasDone2 = true;
                        rotCount++;
                    } else if (rotCount == 3 && !hasDone3) {
                        boardAr[i - 1][n - 1].setFill(Color.BLUE);
                        currentBlock[i - 1][n - 1] = true;
                        hasDone3 = true;
                        rotCount++;
                    }
                }
            }
        }
    }
    
    public void rotateRevL() {
        rotCount = 0;
        boolean hasDone0 = false;
        boolean hasDone1 = false;
        boolean hasDone2 = false;
        boolean hasDone3 = false;
        for (int i = 0; i < 24; i++) {
            for (int n = 0; n < 12; n++) {
                if (currentBlock[i][n]) {
                    boardAr[i][n].setFill(Color.TRANSPARENT);
                    currentBlock[i][n] = false;
                    if (rotCount == 0 && !hasDone0) {
                        boardAr[i - 1][n - 1].setFill(Color.ORANGE);
                        currentBlock[i - 1][n - 1] = true;
                        hasDone0 = true;
                        rotCount++;
                    } else if (rotCount == 1 && !hasDone1) {
                        boardAr[i - 2][n].setFill(Color.ORANGE);
                        currentBlock[i - 2][n] = true;
                        hasDone1 = true;
                        rotCount++;
                    } else if (rotCount == 2 && !hasDone2) {
                        boardAr[i][n].setFill(Color.ORANGE);
                        currentBlock[i][n] = true;
                        hasDone2 = true;
                        rotCount++;
                    } else if (rotCount == 3 && !hasDone3) {
                        boardAr[i - 1][n - 1].setFill(Color.ORANGE);
                        currentBlock[i - 1][n - 1] = true;
                        hasDone3 = true;
                        rotCount++;
                    }
                }
            }
        }
    }


    
    public void rotateT() {
        rotCount = 0;
        boolean hasDone0 = false;
        for (int i = 0; i < 24; i++) {
            for (int n = 0; n < 12; n++) {
                if (currentBlock[i][n]) {
                    if (rotCount == 0 && !hasDone0 && boardAr[i][n - 1].getFill() ==
                        Color.TRANSPARENT && boardAr[i][n + 1].getFill() == Color.TRANSPARENT) {
                        boardAr[i][n].setFill(Color.TRANSPARENT);
                        currentBlock[i][n] = false;
                        boardAr[i + 2][n].setFill(Color.PURPLE);
                        currentBlock[i + 2][n] = true;
                        hasDone0 = true;
                        rotCount++;
                    }
                }
            }
        }
    }

    
    
    public void rotateBlock() {
        TetrisPiece.BlockShape blockShape = TetrisPiece.getShape();
        if (blockShape == TetrisPiece.BlockShape.SQUARE
            || blockShape == TetrisPiece.BlockShape.SINGLEBLOCK) {
            return;
        }
        if (canRotate()) {
            rotCount = 0;
            switch (blockShape) {
            case LINEBLOCK:
                rotateLine();
                break;
            case SBLOCK:
                rotateS();
                break;
            case ZBLOCK:
                rotateZ();
                break;
            case TBLOCK:
                rotateT();
                break;
            case LBLOCK:
                rotateL();
                break;
            case REVERSEL:
                rotateRevL();
                break;
            default:
            }

        }
    } // rotateBlock
    
    /**
     * Return a key event handler that moves to the rectangle to the left
     * or the right depending on what key event is generated by the associated
     * node.
     * @return the key event handler
     */
    private EventHandler <? super KeyEvent> createKeyHandler () {
        return event -> {
            switch (event.getCode()) {
            case LEFT:  // KeyCode.LEFT
                moveLeft();
                break;
            case RIGHT: // KeyCode.RIGHT
                moveRight();
                break;
            case DOWN:
                blockDown();
                break;
            case SPACE:
                rotateBlock();
                System.out.println("block rotated");
                break;
            default:
                // do nothing
            } // switch
            // TODO bounds checking
        };
    } // createKeyHandler

    /**
     * This is a method that returns the value of whether or not the column specified is full.
     *
     * @param arr is a Shape [][] that corresponds to the block grid
     * @param row is an int that corresponds to the row you want to check if its full or not
     *
     * @return boolean value of full or not
     */
    public boolean getFullValue (Shape [][] arr, int row) {
        for (int i = 0; i < 12; i++) {
            if (arr[row][i].getFill() == Color.TRANSPARENT && arr[row][i] != null) {
                return false;
            } //if
        } //for
        return true;
    } //boolean

    /**
     * Method for removing a row and adding 100 to a score once a row is full.
     */
    public void deleteFilled () {
        boolean full;
        for (int i = 0; i < 24; i++) {
            for (int n = 0; n < 12; n++) {
                if (getFullValue(boardAr, i)) {
                    score += 100;
                    for (int j = 0; j < 12; j++) {
                        boardAr[i][j].setFill(Color.TRANSPARENT);
                    }
                } //if
            } //for
        } //for
    } //deleteFilled

    /**
     * Method for starting the game.
     */
    public void play () {
        spawnNew();
        moveBlockDown();
    } //play

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        pane.setOnKeyPressed(createKeyHandler()); // left-right key presses move the rectangle
        doPreparations();
        pane.setOnKeyPressed(createKeyHandler()); // left-right key presses move the rectangle
        paneDisplay = new HBox ();
        paneDisplay.getChildren().add(pane);
        scene = new Scene(paneDisplay, 240, 500);
        stage.setMaxWidth(240);
        stage.setMaxHeight(500);
        stage.setTitle("cs1302-arcade!");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();

        // the group must request input focus to receive key events
        // @see https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Node.html#requestFocus--
        pane.requestFocus();
        play();
    } // start

} // ArcadeApp
