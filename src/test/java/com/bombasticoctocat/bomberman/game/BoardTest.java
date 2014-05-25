package com.bombasticoctocat.bomberman.game;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

public class BoardTest {
    private @Mock Timer timer;
    private @Mock Hero hero;
    private @Mock Directions directions;
    private @Mock BoardMap boardMap;
    private @Mock CollisionDetector collisionDetector;
    private @Mock DeathDetector deathDetector;
    private @Mock GoombaTouchDetector goombaTouchDetector;
    private @Mock Detonator detonator;
    private @Mock LevelConfiguration configuration;
    private @Mock Door door;
    private @Mock Powerup powerup;
    private Board subject;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(boardMap.getDoor()).thenReturn(door);
        when(boardMap.getPowerup()).thenReturn(powerup);
        subject = new Board(timer, hero, boardMap, collisionDetector, deathDetector, new ArrayList<Goomba>(),
                goombaTouchDetector, detonator, configuration);
    }

    @Test
    public void testTick() {
        subject.tick(245, directions, false, false);
        Mockito.verify(hero).move(245, directions, collisionDetector, deathDetector, goombaTouchDetector);
    }
}