package controller.event;

import controller.Controller;
import controller.Player;
import logic.Position;
import model.Tile;
import util.Tuple;

import java.util.Objects;

public class PlacedTileEvent extends GameEvent{

    private Player player;
    private Position pos;
    private Tile tile;

    public PlacedTileEvent(Player player, Position pos, Tile tile) {
        super(player + " placed tile " + tile + " on position " + pos);
        this.pos = Objects.requireNonNull(pos);
        this.player = Objects.requireNonNull(player);
        this.tile = Objects.requireNonNull(tile);
    }

    public PlacedTileEvent(Player player, Tuple<Position, Tile> tuple){
        this(player, tuple.getFirst(), tuple.getSecond());
    }


    @Override
    public void handleEventOnController(Controller ctrl) {
        ctrl.player_placedTile(player, pos, tile);
    }
}
