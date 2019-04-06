package logic;

import model.GameField;
import model.Tile;

import java.util.*;
import java.util.stream.Collectors;

public class TileLogic {

    public static Tile getStartTile() {
        return new StartTile();
    }

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
                if (newTile.isExtendable(dir, type) && !tileInDir.isExtendable(dir.getOpposite(), type))
                    return false;
        }
        return true;
    }

    public static Map<Direction, List<Type>> getExtendableOptions(Tile tile) {
        Map<Direction, List<Type>> result = new HashMap<>();
        for (Direction dir : Direction.values()) {
            for (Type type : Type.values()) {
                if (tile.isExtendable(dir, type)) {
                    if (result.containsKey(dir)) {
                        result.put(dir, new ArrayList<>());
                    }
                    result.get(dir).add(type);
                }
            }
        }

        return result;
    }


}

class StartTile implements Tile {

    @Override
    public boolean isExtendable(Direction dir, Type type) {
        if (type == Type.GRASS && (dir == Direction.SOUTH || dir == Direction.WEST)) {
            return true;
        }
        if (type == Type.RIVER && dir == Direction.WEST) {
            return true;
        }
        return type == Type.FOREST && (dir == Direction.NORTH || dir == Direction.EAST);
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Tile)) {
            return false;
        }
        Tile otherTile = (Tile) other;
        for (var dir : Direction.values())
            for (var type : Type.values())
                if (otherTile.isExtendable(dir, type) != isExtendable(dir, type))
                    return false;
        return true;
    }

    @Override
    public String toString() {
        return "Dummy Start Tile (poorely implemented!)";
    }
}
