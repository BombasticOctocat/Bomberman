package com.bombasticoctocat.bomberman.game;

/**
 * Created by marcin on 05/05/14.
 */
public class TilesFactory {
    private int rows;
    private int cols;
    private double density;
    private Timer timer;

    public TilesFactory(Timer timer, int rows, int cols, double density) {
        this.rows = rows;
        this.cols = cols;
        this.density = density;
        this.timer = timer;
    }

    public Tile createForCoordinates(BoardMap boardMap, int row, int col) {
        return new Tile(boardMap, timer, getTypeForCoordinates(row, col), row, col);
    }

    private Tile.Type getTypeForCoordinates(int row, int col) {
        if (tileIsConcrete(row, col))
            return Tile.CONCRETE;
        else if (Math.random() < density)
            return Tile.BRICKS;
        else
            return Tile.EMPTY;
    }

    private boolean tileIsConcrete(int row, int col) {
        return row == 0 || col == 0 || row == rows - 1 || col == cols - 1 ||
                (row % 2 == 0 && col % 2 == 0);
    }

}
