package logic;

import model.GameField;
import model.Tile;
import util.IterationUtility;

import java.util.*;
import java.util.stream.Collectors;

public class TileLogic {


    private static Set<Position> getValidExtensionPositions(GameField field) {
        var res = new HashSet<Position>();
        for (var pos : field.getAllTiles().keySet())
            for (var dir : Direction.values())
                if (field.getTile(pos.inDirection(dir)) == null)
                    res.add(pos.inDirection(dir));
        return res;
    }

    public static Set<Position> getValidExtensionPositions(GameField field, Tile newTile) {
        return getValidExtensionPositions(field).stream().filter(pos -> isValidAtPosition(field, pos, newTile)).collect(Collectors.toSet());
    }

    private static boolean isValidAtPosition(GameField field, Position position, Tile newTile) {
        for (var dir : Direction.values()) {

            var tileInDir = field.getTile(position.inDirection(dir));

            if (tileInDir == null)
                continue;

            for (var type : Type.values())
                if (newTile.isExtendable(dir, type) && !tileInDir.isExtendable(dir.getOpposite(), type)
                    ||tileInDir.isExtendable(dir.getOpposite(), type) && !newTile.isExtendable(dir, type))
                    return false;
        }
        return true;
    }

    public static Map<Direction, List<Type>> getExtendableOptions(Tile tile) {
        Map<Direction, List<Type>> result = new HashMap<>();
        for (Direction dir : Direction.values()) {
            for (Type type : Type.values()) {
                if (tile.isExtendable(dir, type)) {
                    if (!result.containsKey(dir)) {
                        result.put(dir, new ArrayList<>());
                    }
                    result.get(dir).add(type);
                }
            }
        }

        return result;
    }


}
