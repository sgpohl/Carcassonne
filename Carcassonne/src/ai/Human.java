package ai;

import controller.Controller;
import logic.Position;
import model.GameField;
import model.Tile;
import util.Tuple;
import view.UI;

import java.util.Collection;


public class Human implements Player {


    private UI ui;
    private Controller controller;

    @Override
    public Tuple<Position, Tile> placeTile(GameField field, Collection<Tuple<Position, Tile>> placementOptions) {

        return null;

    }
}
