package controller.instances;

import controller.Controller;
import controller.EventManager;
import controller.Player;
import logic.Position;
import model.Tile;
import util.Tuple;
import view.UI;

import java.util.Collection;
import java.util.Objects;

public class UIController implements Controller {

    private UI ui;

    public UIController(UI ui) {
        this.ui = Objects.requireNonNull(ui);
        EventManager.getInstance().registerController(this);
    }

    public void game_startNewTurn(Player activePlayer, Collection<Tuple<Position, Tile>> placementOptions) {
      //  placementOptions.stream().map(Tuple::getFirst).forEach(pos -> ui.highlight(pos, true));
    }

    public void player_placedTile(Player player, Position pos, Tile tile) {
        ui.draw(pos, tile);
        ui.clearHighlights();
    }
}
