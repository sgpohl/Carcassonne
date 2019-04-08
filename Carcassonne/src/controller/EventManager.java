package controller;

import controller.event.GameEvent;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class EventManager {
    private static EventManager instance = new EventManager();

    public static EventManager getInstance() {
        return instance;
    }

    private EventManager() {

        Runnable run = () -> {
            while (true) {
                try {
                    handleNextEvent();
                    this.wait(1000);
                } catch (InterruptedException e) {
                }
            }
        };
        eventThread = new Thread(run);
        eventThread.start();
    }


    private List<Controller> controllerList = new ArrayList<>();
    private Deque<GameEvent> events = new LinkedList<>();
    private Thread eventThread;

    public synchronized void fire(GameEvent event) {
        events.push(event);
        eventThread.notify();
    }

    public synchronized void handleNextEvent() {
        if (!events.isEmpty()) {
            var event = events.pop();
            controllerList.forEach(event::handleEventOnController);
        }
    }

    public synchronized void registerController(Controller controller) {
        controllerList.add(controller);
    }


}
