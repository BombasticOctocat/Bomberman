package com.bombasticoctocat.bomberman.game;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;

/**
 * Created by kustosz on 05/05/14.
 */
public class HeroTest {

    private @Mock Directions directions;
    private Hero subject;
    private double initialX;
    private double initialY;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        subject = new Hero();
        initialX = subject.getX();
        initialY = subject.getY();
    }

    @Test
    public void testMove() {
        Mockito.when(directions.getHorizontalDirection()).thenReturn(1);
        Mockito.when(directions.getVerticalDirection()).thenReturn(0);
        long timeDelta = 17;

        //subject.move(timeDelta, directions);

        assertEquals(initialY, subject.getY(), 1e-7);
        assertEquals(initialX + timeDelta * subject.speed(), subject.getX(), 1e-7);
    }

    @Test
    public void testDiagonalMovement() {
        Mockito.when(directions.getHorizontalDirection()).thenReturn(-1);
        Mockito.when(directions.getVerticalDirection()).thenReturn(1);
        long timeDelta = 17;

        //subject.move(timeDelta, directions);

        assertEquals(initialY + timeDelta * subject.speed() / Math.sqrt(2), subject.getY(), 1e-7);
        assertEquals(initialX - timeDelta * subject.speed() / Math.sqrt(2), subject.getX(), 1e-7);

    }
}
