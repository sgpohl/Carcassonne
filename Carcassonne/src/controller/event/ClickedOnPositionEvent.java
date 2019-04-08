package controller.event;

import controller.Controller;
import logic.Position;

import java.util.Objects;

public class ClickedOnPositionEvent extends GameEvent {

    private Position pos;

    public ClickedOnPositionEvent(Position pos) {
        super("Clicked on " + pos);
        this.pos = Objects.requireNonNull(pos);
    }

    @Override
    public void handleEventOnController(Controller ctrl) {
        ctrl.UI_clickedOnTile(pos);
    }
}
