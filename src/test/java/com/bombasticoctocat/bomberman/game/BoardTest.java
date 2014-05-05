package com.bombasticoctocat.bomberman.game;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class BoardTest {

    @Mock Hero hero;
    @Mock Directions directions;
    Board subject;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        subject = new Board(hero);
    }

    @Test
    public void testTick() {
        subject.tick(245, directions, false);
        Mockito.verify(hero).move(245, directions);
    }
}