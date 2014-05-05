package com.bombasticoctocat.bomberman.game;

import org.junit.Test;

import static org.junit.Assert.*;

public class TilesFactoryTest {

    @Test
    public void testCreateForCoordinates() throws Exception {
        TilesFactory subject = new TilesFactory(10, 10, 0.0);

        assertEquals(Tile.CONCRETE, subject.createForCoordinates(0, 7).getType());
        assertEquals(Tile.CONCRETE, subject.createForCoordinates(3, 9).getType());
        assertEquals(Tile.CONCRETE, subject.createForCoordinates(2, 2).getType());
        assertEquals(Tile.EMPTY, subject.createForCoordinates(5, 5).getType());
        assertEquals(Tile.EMPTY, subject.createForCoordinates(6, 3).getType());

        subject = new TilesFactory(10, 10, 1.0);

        assertEquals(Tile.BRICKS, subject.createForCoordinates(5, 5).getType());
        assertEquals(Tile.BRICKS, subject.createForCoordinates(6, 3).getType());
    }
}