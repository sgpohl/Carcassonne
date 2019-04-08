package controller.instances;

import controller.Controller;
import controller.EventManager;
import controller.Player;
import controller.event.PlacedTileEvent;
import logic.Position;
import model.Tile;
import util.Tuple;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

public abstract class AIController implements Player, Controller {


    public AIController(){
        EventManager.getInstance().registerController(this);
    }

    public void game_startNewTurn(Player activePlayer, Collection<Tuple<Position, Tile>> placementOptions) {
        if (activePlayer == this) {
            var choice = choosePlacement(placementOptions);
            EventManager.getInstance().fire(new PlacedTileEvent(this, choice));
        }
    }

    public abstract Tuple<Position, Tile> choosePlacement(Collection<Tuple<Position, Tile>> placementOptions);


}
