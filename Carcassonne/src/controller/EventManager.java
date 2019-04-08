package controller;

import controller.event.GameEvent;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class EventManager {


    private static EventManager instance = new EventManager();

    public static EventManager getInstance() {
        return instance;
    }

    private EventManager() {

        new Thread(() -> {
            while (true) {
                try {
                    GameEvent event = events.takeFirst();
                    handle(event);
                } catch (Exception e) {
                    System.out.println("interrupted at takeFirst: " + e.getMessage());
                }
            }
        }).start();
    }


    private final List<Controller> controllerList = new ArrayList<>();
    private final BlockingDeque<GameEvent> events = new LinkedBlockingDeque<>();

    public synchronized void fire(GameEvent event) {
        try {
            events.putLast(event);
        } catch (InterruptedException ex) {
            System.out.println("interrupted at fire: " + ex.getMessage());
        }
    }

    public synchronized void handle(GameEvent event) {
        controllerList.forEach(event::handleEventOnController);
    }

    public synchronized void registerController(Controller controller) {
        controllerList.add(controller);
    }


}
