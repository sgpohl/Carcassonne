package controller.event;

import controller.Controller;
import controller.Player;
import logic.Position;
import model.Tile;
import util.Tuple;

import java.util.Collection;
import java.util.Objects;

public class StartNewTurnEvent extends GameEvent {

    private Player player;
    private Collection<Tuple<Position, Tile>> placementOptions;

    public StartNewTurnEvent(Player player, Collection<Tuple<Position, Tile>> placementOptions) {
        super("Starting turn of " + player + " with " + placementOptions.size() + " placement options");
        this.player = Objects.requireNonNull(player);
        this.placementOptions = Objects.requireNonNull(placementOptions);
    }


    @Override
    public void handleEventOnController(Controller ctrl) {
        ctrl.game_startNewTurn(player, placementOptions);
    }
}
