package logic;

import model.GameField;
import model.Tile;
import util.IterationUtility;

import java.util.*;
import java.util.stream.Collectors;

public class TileLogic {


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
