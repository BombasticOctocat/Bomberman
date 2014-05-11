package com.bombasticoctocat.bomberman.game;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.junit.Assert.assertEquals;

public class TilesFactoryTest {
    private @Mock BoardMap boardMap;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateForCoordinates() throws Exception {
        TilesFactory subject = new TilesFactory(boardMap, 10, 10, 0.0);

        assertEquals(Tile.CONCRETE, subject.createForCoordinates(0, 7).getType());
        assertEquals(Tile.CONCRETE, subject.createForCoordinates(3, 9).getType());
        assertEquals(Tile.CONCRETE, subject.createForCoordinates(2, 2).getType());
        assertEquals(Tile.EMPTY, subject.createForCoordinates(5, 5).getType());
        assertEquals(Tile.EMPTY, subject.createForCoordinates(6, 3).getType());

        subject = new TilesFactory(boardMap, 10, 10, 1.0);

        assertEquals(Tile.BRICKS, subject.createForCoordinates(5, 5).getType());
        assertEquals(Tile.BRICKS, subject.createForCoordinates(6, 3).getType());
    }
}