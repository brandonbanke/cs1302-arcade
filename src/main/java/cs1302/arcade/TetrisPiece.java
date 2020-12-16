package cs1302.arcade;

import java.util.Random;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * Class that represents the creation of a block that appears in the tetris game.
 */
public class TetrisPiece {

    private static Shape [][] grid = new Shape [2][4];
    enum BlockShape {
        ZBLOCK,
        SBLOCK,
        LBLOCK,
        REVERSEL,
        LINEBLOCK,
        TBLOCK,
        SQUARE,
        SINGLEBLOCK;
    }
    private static BlockShape blockShape;
    /**
     * Method to create the block in an single block formation.
     *
     * @return Shape[][] representing the physical block 
     *
     */
    public static Shape [][] spawnLine () {
        Rectangle nullShape = new Rectangle (20, 20, Color.TRANSPARENT);
        Rectangle r = new Rectangle (20,20);
        r.setFill(Color.AQUA);
        grid [0][0] = r;
        grid [0][1] = r;
        grid [1][0] = nullShape;
        grid [1][1] = nullShape;
        grid [0][2] = r;
        grid [0][3] = r;
        grid [1][2] = nullShape;
        grid [1][3] = nullShape;
        blockShape = BlockShape.LINEBLOCK;
        return grid;
    }
    
    /**
     * Method to create the block in an single block formation.
     *
     * @return Shape[][] representing the physical block
     *
     */
    public static Shape [][] spawnSingle () {
        Rectangle nullShape = new Rectangle (20, 20, Color.TRANSPARENT);
        Rectangle r = new Rectangle (20,20);
        r.setFill(Color.GRAY);
        grid [0][0] = r;
        grid [0][1] = nullShape;
        grid [0][2] = nullShape;
        grid [0][3] = nullShape;
        grid [1][0] = nullShape;
        grid [1][1] = nullShape;
        grid [1][2] = nullShape;
        grid [1][3] = nullShape;
        blockShape = BlockShape.SINGLEBLOCK;
        return grid;
    } //spawnSingle

    /**
     * Method to create the block in an L formation. 
     *
     * @return Shape[][] representing the physical block 
     *
     */
    public static Shape [][] spawnL () {
        Rectangle nullShape = new Rectangle (20, 20, Color.TRANSPARENT);
        Rectangle r = new Rectangle (20,20);
        r.setFill(Color.BLUE);
        grid [0][0] = r;
        grid [0][1] = nullShape;
        grid [0][2] = nullShape;
        grid [0][3] = nullShape;
        grid [1][0] = r;
        grid [1][1] = r;
        grid [1][2] = r;
        grid [1][3] = nullShape;
        blockShape = BlockShape.LBLOCK;
        return grid;
    } //spawnL

    /**
     * Method to create the block in a backwards L formation. 
     *
     * @return Shape[][] representing the physical block
     *
     */
    public static Shape [][] spawnRevL () {
        Rectangle nullShape = new Rectangle (20, 20, Color.TRANSPARENT);
        Rectangle r = new Rectangle (20,20);
        r.setFill(Color.ORANGE);
        grid [0][1] = nullShape;
        grid [0][2] = nullShape;
        grid [0][0] = nullShape;
        grid [0][3] = r;
        grid [1][3] = r;
        grid [1][2] = r;
        grid [1][1] = r;
        grid [1][0] = nullShape;
        blockShape = BlockShape.REVERSEL;
        return grid;
    } //spawnRevL

    /**
     * Method to create the block in a Z formation.
     *
     * @return Shape[][] representing the physical block
     *
     */
    public static Shape [][] spawnZ () {
        Rectangle r = new Rectangle (20,20);
        Rectangle nullShape = new Rectangle (20, 20, Color.TRANSPARENT);
        r.setFill(Color.RED);
        grid [0][0] = r;
        grid [0][1] = r;
        grid [0][2] = nullShape;
        grid [0][3] = nullShape;
        grid [1][0] = nullShape;
        grid [1][1] = r;
        grid [1][2] = r;
        grid [1][3] = nullShape;
        blockShape = BlockShape.ZBLOCK;
        return grid;
    }

    /**
     * Method to create the block in an S formation. 
     *
     * @return Shape[][] representing the physical block 
     *
     */
     public static Shape [][] spawnS () {
        Rectangle nullShape = new Rectangle (20, 20, Color.TRANSPARENT);
        Rectangle r = new Rectangle (20,20);
        r.setFill(Color.GREEN);
        grid [0][0] = nullShape;
        grid [0][1] = r;
        grid [0][2] = r;
        grid [0][3] = nullShape;
        grid [1][0] = r;
        grid [1][1] = r;
        grid [1][2] = nullShape;
        grid [1][3] = nullShape;
        blockShape = BlockShape.SBLOCK;
        return grid;
    }
    
    /**
     * Method to create the block in an Square formation.
     *
     * @return Shape[][] representing the physical block 
     *
     */
    public static Shape [][] spawnSquare () {
        Rectangle nullShape = new Rectangle (20, 20, Color.TRANSPARENT);
        Rectangle r = new Rectangle (20,20);
        r.setFill(Color.YELLOW);
        grid [0][0] = r;
        grid [0][1] = r;
        grid [1][1] = r;
        grid [1][2] = nullShape;
        grid [0][2] = nullShape;
        grid [0][3] = nullShape;
        grid [1][0] = r;
        grid [1][3] = nullShape;
        blockShape = BlockShape.SQUARE;
        return grid;
    }

    /**
     * Method to create the block in an T formation.
     *
     * @return Shape[][] representing the physical block 
     */
    public static Shape [][] spawnT () {
        Rectangle nullShape = new Rectangle (20, 20, Color.TRANSPARENT);
        Rectangle r = new Rectangle (20,20);
        r.setFill(Color.PURPLE);
        grid [0][0] = nullShape;
        grid [0][1] = r;
        grid [0][2] = nullShape;
        grid [0][3] = nullShape;
        grid [1][0] = r;
        grid [1][1] = r;
        grid [1][2] = r;
        grid [1][3] = nullShape;
        blockShape = BlockShape.TBLOCK;
        return grid;
    } //spawnT

    /**
     * Method to return the shape of the current falling block in the TetrisApp class
     *
     * @return BlockShape the enum value of the current block object
     *
     */
    public static BlockShape getShape() {
        return blockShape;
    } // getShape

    public static Shape[][] rotateShape() {
        Shape[][] rotationShape = new Shape[4][2];
        for (int i = 0; i < 2; i++) {
            for (int n = 0; n < 4; n++) {
                rotationShape[n][i] = grid[i][n];
            } // for
        } // for
        return rotationShape;
    } // rotateShape
    
    /**
     * Method that generates a random value for a block and returns a formation for the new piece.
     *
     * @ return Shape[][] representing the new block introduced into the scene.
     *
     */
    public static Shape [][] spawnBlock () {
        Shape [][] temp = new Shape [2][4];
        Random ran = new Random ();
        int num = ran.nextInt(9);
        switch (num) {
        case 1:
            temp = spawnLine();
            break;
        case 2:
            temp = spawnSingle();
            break;
        case 3:
            temp = spawnL();
            break;
        case 4:
            temp = spawnRevL();
            break;
        case 5:
            temp = spawnZ();
            break;
        case 6:
            temp = spawnS();
            break;
        case 7:
            temp = spawnSquare();
            break;
        case 8:
            temp = spawnT();
        default:
            temp = spawnLine();
        } //switch
        return temp;
    } //spawnShape

} //TetrisBlock
