package logic;

import model.GameField;
import model.Tile;
import util.Triple;
import util.Tuple;

import java.util.*;
import java.util.stream.Collectors;

public class TileLogic {


    public static Tile getStartTile() {
        return new Tile() {
            @Override
            public boolean isExpendable(Direction direction, Type type) {
                return false;
            }

            public String toString() {
                return "Dummy Start Tile (Not yet implemented!)";
            }
        };
    }

    public static Collection<Tile> getExtendableTiles(GameField gf) {
        //.filter(tile::isExpandable);
        return null;
    }

    public static Map<Direction, List<Type>> getExtendableOptions(Tile tile) {
        Map<Direction, List<Type>> result = new HashMap<>();
        for (Direction dir : Direction.values()) {
            for (Type type : Type.values()) {
                if (tile.isExpendable(dir, type)) {
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
