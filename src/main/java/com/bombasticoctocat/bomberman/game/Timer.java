package com.bombasticoctocat.bomberman.game;

import java.util.Iterator;
import java.util.LinkedList;

public class Timer {
    private class Event {
        public long time;
        public Runnable callback;

        public Event(long time, Runnable callback) {
            this.time = time;
            this.callback = callback;
        }
    }

    long timePassed = 0;
    LinkedList<Event> events = new LinkedList<>();

    public void schedule(long time, Runnable callback) {
        events.add(new Event(timePassed + time, callback));
    }

    public void clear() {
        events.clear();
    }

    public void tick(long timeDelta) {
        timePassed += timeDelta;

        LinkedList<Event> executionList = new LinkedList<>();
        Iterator<Event> iterator = events.iterator();


        while (iterator.hasNext()) {
            Event event = iterator.next();
            if (event.time <= timePassed) {
                iterator.remove();
                executionList.add(event);
            }
        }

        for (Event event : executionList) {
            event.callback.run();
        }
    }
}
