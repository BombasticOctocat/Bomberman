package com.bombasticoctocat.bomberman.game;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class BoardTest {

    private @Mock Hero hero;
    private @Mock Directions directions;
    private @Mock BoardMap boardMap;
    private @Mock CollisionDetector collisionDetector;
    private Board subject;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        subject = new Board(hero, boardMap, collisionDetector);
    }

    @Test
    public void testTick() {
        subject.tick(245, directions, false);
        Mockito.verify(hero).move(245, directions, collisionDetector);
    }
}