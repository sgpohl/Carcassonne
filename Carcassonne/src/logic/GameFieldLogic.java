package logic;

import model.GameField;
import java.util.*;
import model.Tile;
import logic.Direction;

//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Map;
import java.util.stream.Collectors;

public class GameFieldLogic {

	// startAtPos: Position des neu angelegten Tiles
	//Dummy
	// Directions.SOUTH == dummy
    public static Map<Type, Collection<Tile>> getClosedAreas(GameField field, Position startAtPos) {
    	Map<Type, Collection<Tile>> returnMap = new HashMap<>();
    	GameFieldExtensionSearch toSearch = new GameFieldExtensionSearch();
    	for(Type t: Type.values()) {
    		toSearch.isConnected(field, startAtPos, t, Direction.SOUTH);
    	}
    	
        return null;
    }


}

class GameFieldExtensionSearch {
    private Collection<Position> closed = new HashSet<Position>();
    
    public GameFieldExtensionSearch() {
    }
    
    // Hilfsfunktion die Testet ob INNERHALB eines gegebenen Tiles ein bestimmter Type abgeschlossen ist
    // (aus bestimmter Richtung gesehen)
	public static boolean isSingleTileClosed(Tile t, Type ty, Direction startDirection) {
		if (t.isExtendable(startDirection, ty)) { // has this direction the asked type?
			boolean clockwiseEqualType = t.isExtendable(startDirection.rotateClockwise(), ty);
			boolean counterClockwiseEqualType = t.isExtendable(startDirection.rotateCounterclockwise(), ty);
			if (clockwiseEqualType || counterClockwiseEqualType) {
				return false;
			}
			if (ty == Type.FOREST) {
				return true;
			}
			if (t.isExtendable(startDirection.getOpposite(), ty)) {
				return true;
			}
		}
		return false;
	}

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
