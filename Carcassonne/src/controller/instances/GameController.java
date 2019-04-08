package controller.instances;

import controller.Controller;
import controller.EventManager;
import controller.Player;
import controller.event.StartNewTurnEvent;
import logic.GameFieldLogic;
import logic.Position;
import logic.TileFactory;
import model.GameField;
import model.Tile;
import util.IterationUtility;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

public class GameController implements Controller {

    private GameField field;
    private Iterator<Player> playerIterator;

    public GameController(GameField field, Collection<Player> player) {
        this.field = Objects.requireNonNull(field);
        playerIterator = IterationUtility.endlessIterator(player.iterator());
        EventManager.getInstance().registerController(this);
    }


    public void player_placedTile(Player player, Position pos, Tile tile) {
        startNextTurn();
    }

    private void startNextTurn() {
        var player = playerIterator.next();
        var tile = createNextTile();
        var placementOptions = GameFieldLogic.getValidPlacementPositionsWithRotations(field, tile);
        while (placementOptions.isEmpty()) {
            tile = createNextTile();
            placementOptions = GameFieldLogic.getValidPlacementPositionsWithRotations(field, tile);
            System.out.println("tile could not be placed, creating next one!");
        }

        EventManager.getInstance().fire(new StartNewTurnEvent(player, placementOptions));
    }


    private Tile createNextTile() {
        if (Math.random() < 0.1)
            return TileFactory.nearestFittingTile(field, new Position(0, 0));
        return TileFactory.getRandomTile();
    }


}
