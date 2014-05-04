package com.bombasticoctocat.bomberman;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

public class BombermanModule extends AbstractModule {
    @Override
    protected void configure() {
        bindListener(Matchers.any(), new Slf4jLoggerTypeListener());
    }
}
