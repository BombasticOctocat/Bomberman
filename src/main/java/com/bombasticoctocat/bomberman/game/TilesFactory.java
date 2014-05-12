package com.bombasticoctocat.bomberman.game;

public class TilesFactory {
    private int rows;
    private int cols;
    private double density;

    public TilesFactory(int rows, int cols, double density) {
        this.rows = rows;
        this.cols = cols;
        this.density = density;
    }

    public Tile createForCoordinates(int row, int col) {
        return new Tile(getTypeForCoordinates(row, col), row, col);
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
