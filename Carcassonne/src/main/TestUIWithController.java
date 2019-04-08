package main;

import logic.GameFieldLogic;
import logic.Position;
import logic.TileFactory;
import model.GameField;
import model.GameFieldImpl;
import model.Tile;
import view.UI;

import java.util.Collection;

public class TestUIWithController {


    public static void main(String[] args) throws Exception {
/*
        GameField field = new GameFieldImpl();

        ui.drawAll(field);

        int timeDelay = 1;

        for (int i = 0; i < 5000; i++) {
            Tile newTile = TileFactory.getRandomTile();
            if (i % 20 == 0)
                newTile = TileFactory.nearestFittingTile(field, new Position(0, 0));


            Collection<Position> posCol = GameFieldLogic.getValidPlacementPositions(field, newTile);

            for (Position curPos : posCol) {
                ui.highlight(curPos, true);
            }

            Thread.sleep(timeDelay);


            for (Position curPos : posCol) {
                ui.highlight(curPos, false);
            }

            if (!posCol.isEmpty()) {
                Position setOnPos = new Position(0, 0).calcClosest(posCol);
                ui.highlight(setOnPos, true);
                Thread.sleep(timeDelay);
                field.set(setOnPos, newTile);
                ui.draw(setOnPos, newTile);
                ui.highlight(setOnPos, false);
            }

            Thread.sleep(timeDelay);
        }

*/
    }

}
