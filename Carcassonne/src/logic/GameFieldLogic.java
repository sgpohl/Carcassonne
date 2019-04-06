package logic;

import model.GameField;
import model.Tile;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

public class GameFieldLogic {


    public static Map<Type, Collection<Tile>> getClosedAreas(GameField field, Position startAtPos) {
        return null;
    }


}

class GameFieldExtensionSearch {
    private Collection<Position> closed = new HashSet<Position>();

    // TODO: untested!
    public boolean isConnected(GameField gameField, Position pos, Type type, Direction enteredFromDirection) {
        if (closed.contains(pos))
            return true;

        closed.add(pos);

        var tile = gameField.getTile(pos);
        var extensionMap = TileLogic.getExtendableOptions(tile);

        var possibleDirs = extensionMap.keySet().stream()
                .filter(dir -> dir != enteredFromDirection.getOpposite())
                .filter(dir -> extensionMap.get(dir).contains(type))
                .collect(Collectors.toSet());


        for (Direction dir : possibleDirs)
            if (!isConnected(gameField, pos.inDirection(dir), type, dir))
                return false;

        return true;

    }

}
