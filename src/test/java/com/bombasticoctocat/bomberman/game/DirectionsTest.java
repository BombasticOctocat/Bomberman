package com.bombasticoctocat.bomberman.game;

import org.junit.Test;

import static org.junit.Assert.*;

public class DirectionsTest {

    @Test
    public void testVerticalDirection() {
        assertEquals(0, new Directions().getVerticalDirection());
        assertEquals(1, new Directions(Directions.DOWN).getVerticalDirection());
        assertEquals(-1, new Directions(Directions.UP).getVerticalDirection());
        assertEquals(0, new Directions(Directions.UP | Directions.DOWN).getVerticalDirection());
    }

    @Test
    public void testHorizontalDirection() {
        assertEquals(0, new Directions().getHorizontalDirection());
        assertEquals(1, new Directions(Directions.RIGHT).getHorizontalDirection());
        assertEquals(-1, new Directions(Directions.LEFT).getHorizontalDirection());
        assertEquals(0, new Directions(Directions.RIGHT | Directions.LEFT).getHorizontalDirection());
    }
}