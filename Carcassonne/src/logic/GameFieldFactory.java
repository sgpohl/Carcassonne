package logic;

import model.GameField;
import model.GameFieldImpl;

import java.util.Random;

public class GameFieldFactory {


    public static GameField getStartField() {
        return new GameFieldImpl();
    }

    public static GameField getSampleField(Random ran, int tilese) {

        var field = getStartField();
        var center = new Position(0, 0);

        for (int i = 0; i < 100; i++) {
            var place = TileFactory.nearestFittingPositionAndTile(field, center);
            field.set(place.getFirst(), place.getSecond());
        }

        return field;
    }


}
