package com.bombasticoctocat.bomberman.game;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;

public class HeroTest {
    private @Mock Detonator detonator;
    private @Mock Directions directions;
    private @Mock CollisionDetector collisionDetector;
    private Hero subject;
    private double initialX;
    private double initialY;

    @Before
    public void setUp() {
        initMocks(this);
        subject = new Hero(detonator);
        initialX = subject.getX();
        initialY = subject.getY();
        when(collisionDetector.blockDisplacement(eq(subject), isA(Displacement.class))).
                thenAnswer(invocationOnMock -> (Displacement) invocationOnMock.getArguments()[1]);
    }

    @Test
    public void testMove() {
        when(directions.getHorizontalDirection()).thenReturn(1);
        when(directions.getVerticalDirection()).thenReturn(0);
        long timeDelta = 17;

        subject.move(timeDelta, directions, collisionDetector);

        assertEquals(initialY, subject.getY(), 1e-7);
        assertEquals(initialX + timeDelta * subject.speed(), subject.getX(), 1e-7);
    }

    @Test
    public void testDiagonalMovement() {
        when(directions.getHorizontalDirection()).thenReturn(-1);
        when(directions.getVerticalDirection()).thenReturn(1);
        long timeDelta = 17;

        subject.move(timeDelta, directions, collisionDetector);

        assertEquals(initialY + timeDelta * subject.speed() / Math.sqrt(2), subject.getY(), 1e-7);
        assertEquals(initialX - timeDelta * subject.speed() / Math.sqrt(2), subject.getX(), 1e-7);

    }
}
