package main;

import logic.Position;
import logic.TileFactory;
import logic.TileLogic;
import model.GameField;
import model.GameFieldImpl;
import model.Tile;
import view.UI;

import java.util.*;

public class TestLogikWithGui {

    public static void main(String[] args) throws Exception {
        UI ui = new UI();

        GameField field = new GameFieldImpl();

        drawAll(ui, field);
        ui.highlight(new Position(0, 0), true);

        for (int i = 0; i < 50; i++) {
            Tile newtile = TileFactory.getRandomTile();

            Collection<Position> posCol = TileLogic.getValidExtensionPositions(field, newtile);

            for (Position curPos : posCol) {
                ui.highlight(curPos, true);
            }

            Thread.sleep(500);

            if (!posCol.isEmpty()) {
                Position setOnPos = new Position(0, 0).calcClosest(posCol);
                field.set(setOnPos, newtile);
                ui.draw(setOnPos, newtile);
            }

            for (Position curPos : posCol) {
                ui.highlight(curPos, false);
            }

            Thread.sleep(500);
        }


    }

    private static void drawAll(UI ui, GameField field) {
        for (var pos : field.getAllTiles().keySet())
            ui.draw(pos, field.getAllTiles().get(pos));
    }


}
