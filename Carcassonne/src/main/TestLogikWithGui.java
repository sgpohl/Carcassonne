package main;

import logic.Position;
import logic.TileFactory;
import logic.TileLogic;
import model.GameField;
import model.GameFieldImpl;
import model.Tile;
import view.UI;

import java.util.*;

import controller.Controller;

public class TestLogikWithGui {

    public static void main(String[] args) throws Exception {
    	Controller c = new Controller();
        UI ui = c.getUI();

        GameField field = new GameFieldImpl();

        drawAll(ui, field);
        
        int timeDelay = 1;
        
        for (int i = 0; i < 5000; i++) {
            Tile newtile = TileFactory.getRandomTile();

            Collection<Position> posCol = TileLogic.getValidExtensionPositions(field, newtile);

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
                field.set(setOnPos, newtile);
                ui.draw(setOnPos, newtile);
                ui.highlight(setOnPos, false);
            }
            
            Thread.sleep(timeDelay);
        }


    }

    private static void drawAll(UI ui, GameField field) {
        for (var pos : field.getAllTiles().keySet())
            ui.draw(pos, field.getAllTiles().get(pos));
    }


}
