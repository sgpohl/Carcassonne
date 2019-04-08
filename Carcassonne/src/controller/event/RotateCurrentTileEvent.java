package controller.event;

import controller.Controller;

public class RotateCurrentTileEvent extends GameEvent {

    public RotateCurrentTileEvent() {
        super("roated current tile");
    }

    @Override
    public void handleEventOnController(Controller ctrl) {
        ctrl.UI_rotateCurrentTile();
    }

}
