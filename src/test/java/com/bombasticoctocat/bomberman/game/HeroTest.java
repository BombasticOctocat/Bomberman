package com.bombasticoctocat.bomberman.game;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;

public class HeroTest {

    private @Mock Directions directions;
    private @Mock CollisionDetector collisionDetector;
    private Hero subject;
    private double initialX;
    private double initialY;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        subject = new Hero();
        initialX = subject.getX();
        initialY = subject.getY();
        Mockito.when(collisionDetector.blockDisplacement(Mockito.eq(subject), Mockito.isA(Displacement.class))).
                thenAnswer(invocationOnMock -> (Displacement) invocationOnMock.getArguments()[1]);
    }

    @Test
    public void testMove() {
        Mockito.when(directions.getHorizontalDirection()).thenReturn(1);
        Mockito.when(directions.getVerticalDirection()).thenReturn(0);
        long timeDelta = 17;

        subject.move(timeDelta, directions, collisionDetector);

        assertEquals(initialY, subject.getY(), 1e-7);
        assertEquals(initialX + timeDelta * subject.speed(), subject.getX(), 1e-7);
    }

    @Test
    public void testDiagonalMovement() {
        Mockito.when(directions.getHorizontalDirection()).thenReturn(-1);
        Mockito.when(directions.getVerticalDirection()).thenReturn(1);
        long timeDelta = 17;

        subject.move(timeDelta, directions, collisionDetector);

        assertEquals(initialY + timeDelta * subject.speed() / Math.sqrt(2), subject.getY(), 1e-7);
        assertEquals(initialX - timeDelta * subject.speed() / Math.sqrt(2), subject.getX(), 1e-7);

    }
}
