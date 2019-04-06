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
            public boolean isExtendable(Direction direction, Type type) {
                return false;
            }

            public String toString() {
                return "Dummy Start Tile (Not yet implemented!)";
            }
        };
    }

    private static Set<Position> getValidExtensionPositions(GameField field){
        var res = new HashSet<Position>();
        for(var pos: field.getAllTiles().keySet())
            for(var dir: Direction.values())
                if(field.getTile(pos.inDirection(dir)) == null)
                    res.add(pos.inDirection(dir));
        return res;
    }

    public static List<Position> getValidExtensionPositions(GameField field, Tile newTile){
        return new ArrayList<>();
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
