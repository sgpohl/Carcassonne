package controller.instances;

import controller.Controller;
import controller.EventManager;
import controller.Player;
import controller.event.PlacedTileEvent;
import logic.Position;
import logic.TileFactory;
import model.Tile;
import util.Tuple;
import view.UI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public class PlayerController implements Player, Controller {


    private Optional<Tile> activeTile = Optional.empty();
    private Collection<Tuple<Position, Tile>> possibleOptions = new ArrayList<>();
    private UI ui;

    public PlayerController(UI ui) {
        this.ui = Objects.requireNonNull(ui);
        EventManager.getInstance().registerController(this);
    }

    public synchronized void UI_rotateCurrentTile() {

        ui.clearHighlights();

        if (activeTile.isPresent()) {
            System.out.print(activeTile);
            activeTile = activeTile.map(TileFactory::rotateClockwise);
            System.out.println(" -> " + activeTile);
            setUIHiglights();
        }

    }

    public synchronized void UI_clickedOnTile(Position pos) {
        if (activeTile.isPresent()) {
            var chosenPlacement = new Tuple<>(pos, activeTile.get());
            if (possibleOptions.contains(chosenPlacement)) {
                EventManager.getInstance().fire(new PlacedTileEvent(this, chosenPlacement));
            }
        }
    }

    public synchronized void game_startNewTurn(Player activePlayer, Collection<Tuple<Position, Tile>> placementOptions) {
        if (activePlayer != this) {
            deselect();
            return;
        }

        this.possibleOptions = placementOptions;
        this.activeTile = Optional.of(placementOptions.iterator().next().getSecond());

        ui.clearHighlights();
        setUIHiglights();
    }

    private void deselect() {
        this.activeTile = Optional.empty();
        ui.setDrawnCard(null);
    }

    private void setUIHiglights() {
        ui.clearHighlights();

        if (activeTile.isPresent()) {
            ui.setDrawnCard(activeTile.get());
            possibleOptions.stream()
                    .filter(option -> option.getSecond().equals(activeTile.get()))
                    .map(Tuple::getFirst)
                    .forEach(pos -> ui.highlight(pos, true));
        }
    }

}
