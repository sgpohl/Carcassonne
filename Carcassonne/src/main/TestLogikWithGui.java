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

    public static void main(String[] args) throws Exception{
        UI ui = new UI();

        GameField field = new GameFieldImpl();

        drawAll(ui, field);


        for(int i = 0; i < 1; i++){
            Tile newtile = TileFactory.getRandomTile();

            Collection<Position> pos = TileLogic.getValidExtensionPositions(field, newtile);
            System.out.println("new tile: " + newtile + ", valid pos: " + pos);

            Position setOnPos = new ArrayList<>(pos).get(0);
            field.set(setOnPos, newtile);
            ui.draw(setOnPos, newtile);
           // Thread.sleep(100);
        }


    }

    private static void drawAll(UI ui, GameField field) {
        for (var pos : field.getAllTiles().keySet())
            ui.draw(pos, field.getAllTiles().get(pos));
    }


}
