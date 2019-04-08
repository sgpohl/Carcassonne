package logic;

import model.GameField;
import model.GameFieldImpl;

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
    public static Map<Type, Collection<Tile>> getClosedAreas(GameField field, Position startAtPos) {
    	Map<Type, Collection<Tile>> returnMap = new HashMap<>();
    	GameFieldExtensionSearch toSearch = new GameFieldExtensionSearch();
    	for(Type t: Type.values()) {
    		//for(Direction d: Direction.values()) // DIRECTIONS HIER? ODER IN REGRESSION?
    		// BRAUCHEN BEI GLEICHEM TYP GLEICHE HASHMAP!!!
    		// startDirection is dummy
    		// For all directions in a certain order
    		if(isSingleTileClosed(field.getTile(startAtPos), t, Direction.SOUTH)){
    			toSearch.isConnected(field, startAtPos, t, Direction.SOUTH);
    		}
    		// Falls Type für alle Directions false:
    		returnMap.put(t, null);
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
	
	// BRAUCHT NOCH EINE EBENE GRÖßERE HILFSMETHODE DIE EIGENES SET VERWALTET UND ABGESCHLOSSENE BEREICHE ZURÜCKGIBT
    
	
    public void findPositionsOfConnected(GameFieldImpl field, Position pos, Type type, Direction enteredFromDirection){
    	Collection<Position> tempConnected = new HashSet<>(); 
    	if(tempConnected.contains(pos)) {
    		return;
    	}
    	closed.add(pos);
    	if(isSingleTileClosed(field.getTile(pos), type, enteredFromDirection)) {
    		
    	}
    	// dummy
    	return;
    }
    
    public Collection<Position> getPositions() {
    	return this.closed;
    }
   

    // TODO: untested!
    public boolean isConnected(GameField gameField, Position pos, Type type, Direction enteredFromDirection) {

       return false;

    }

}
