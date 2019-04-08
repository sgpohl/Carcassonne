package controller;

import ai.NearestToPositionAI;
import ai.Player;
import logic.GameFieldLogic;
import logic.Position;
import logic.TileFactory;
import model.GameField;
import model.GameFieldImpl;
import model.Tile;
import util.IterationUtility;
import util.Tuple;
import view.UI;

import java.util.*;
import java.util.function.BiConsumer;

import controller.Controller;

public class Game {


    public static void main(String[] args) {
        var field = new GameFieldImpl();
        var player = Arrays.asList(new Player[]{new NearestToPositionAI(new Position(0, 0))});

        var game = new Game(field, player);

        var controller = new Controller();
        var ui = new UI(controller);
        ui.drawAll(field);

        game.addTilePlacedListener(ui::draw);
        //TODO: de-select all highlights at tile placement

        for (int i = 0; i < 1000; i++) {
            game.gameStep();
            try {
                Thread.sleep(100);
            } catch (Exception e) {
            }
        }

    }

    private GameField field;
    private Iterator<Player> playerIterator;


    public Game(GameField field, Collection<Player> player) {
        this.field = Objects.requireNonNull(field);
        playerIterator = IterationUtility.endlessIterator(player.iterator());
    }


    public Tile createNextTile() {
        if (Math.random() < 0.1)
            return TileFactory.nearestFittingTile(field, new Position(0, 0));
        return TileFactory.getRandomTile();
    }


    public void gameStep() {
        var player = playerIterator.next();

        var tile = createNextTile();
        var placementOptions = GameFieldLogic.getValidPlacementPositionsWithRotations(field, tile);
        while (placementOptions.isEmpty()) {
            tile = createNextTile();
            placementOptions = GameFieldLogic.getValidPlacementPositionsWithRotations(field, tile);
            System.out.println("tile could not be placed, creating next one!");
        }

        var placement = player.placeTile(field, placementOptions);
        field.set(placement.getFirst(), placement.getSecond());

        fireTilePlaced(placement);

    }

    // MAKE GAME OBSERVABLE
    private Collection<BiConsumer<Position, Tile>> tilePlacedListener = new ArrayList<>();
    private Collection<BiConsumer<Position, Tile>> tilePlacementOptionListener = new ArrayList<>();

    public void addTilePlacedListener(BiConsumer<Position, Tile> listener) {
        this.tilePlacedListener.add(listener);
    }

    public void addTilePlacementOptionListener(BiConsumer<Position, Tile> listener) {
        this.tilePlacementOptionListener.add(listener);
    }

    private void fireTilePlaced(Tuple<Position, Tile> placement) {
        tilePlacedListener.forEach(listener -> listener.accept(placement.getFirst(), placement.getSecond()));
    }

    private void fireTilePlacementOptions(Collection<Tuple<Position, Tile>> tilePlacementOptions) {
        tilePlacementOptionListener.forEach(listener -> tilePlacementOptions.forEach(placementTuple ->
                listener.accept(placementTuple.getFirst(), placementTuple.getSecond())
        ));
    }

}
