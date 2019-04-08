package controller.event;

import controller.Controller;
import controller.EventManager;

import java.util.function.Consumer;

public abstract class GameEvent {

    public GameEvent() {
        this(null);
    }

    public GameEvent(String msg) {
        if (msg != null) {
            System.out.println("[EVENT]: " + msg);
        }
    }


    public abstract void handleEventOnController(Controller ctrl);

}
