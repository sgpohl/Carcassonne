package logic;

import model.GameField;

import java.util.*;

import model.Tile;
import util.Tuple;
import logic.TileLogic;

//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Map;
import java.util.stream.Collectors;

public class GameFieldLogic {

    // startAtPos: Position des neu angelegten Tiles
    //Dummy
    // Directions.SOUTH == dummy
    public static Map<Type, Collection<Position>> getClosedAreas(GameField field, Position startAtPos) {
        Map<Type, Collection<Position>> returnMap = new HashMap<>();
        for (Type t : Type.values()) {
        	GameFieldExtensionSearch toSearch = new GameFieldExtensionSearch();
        	Tuple<Boolean, HashSet<Position>> result = toSearch.checkClosed(new HashSet<Position>(), field, startAtPos, t, Direction.NORTH);
            if(result.getFirst()) {
            	returnMap.get(t).addAll(result.getSecond());
            }
        }
        return returnMap;
    }

    public static Collection<Position> calcPositionsWithEmptyNeighbours(GameField field) {
        Collection<Position> posCol = new HashSet<>();
        for (var pos : field.getAllTiles().keySet())
            if (isPositionWithEmptyNeighbours(field, pos))
                posCol.add(pos);
        return posCol;
    }

    public static Collection<Tuple<Position, Tile>> getValidPlacementPositionsWithRotations(GameField field, Tile tile){
        var placementList = new HashSet<Tuple<Position, Tile>>();
        for(int i = 0; i < 4; i++){
            var curTile = TileFactory.rotateClockwise(tile, i);
            for(var pos: getValidPlacementPositions(field, curTile))
                placementList.add(new Tuple<>(pos, curTile));
        }
        return placementList;
    }

    public static Collection<Position> getValidPlacementPositions(GameField field) {
        Collection<Position> extensionPosition = new HashSet<>();
        for (var pos : field.getPositionsWithEmptyNeighbours())
            for (var dir : Direction.values())
                if (field.getTile(pos.inDirection(dir)) == null)
                    extensionPosition.add(pos.inDirection(dir));
        return extensionPosition;
    }

    public static boolean isPositionWithEmptyNeighbours(GameField field, Position pos) {
        for (var dir : Direction.values())
            if (field.getTile(pos.inDirection(dir)) == null)
                return true;
        return false;
    }

    public static Set<Position> getValidPlacementPositions(GameField field, Tile newTile) {
        return getValidPlacementPositions(field).stream().filter(pos -> isValidAtPosition(field, pos, newTile)).collect(Collectors.toSet());
    }

    private static boolean isValidAtPosition(GameField field, Position position, Tile newTile) {
        for (var dir : Direction.values()) {
            var tileInDir = field.getTile(position.inDirection(dir));
            if (tileInDir == null)
                continue;

            for (var type : Type.values())
                if (newTile.isExtendable(dir, type) && !tileInDir.isExtendable(dir.getOpposite(), type)
                        || tileInDir.isExtendable(dir.getOpposite(), type) && !newTile.isExtendable(dir, type))
                    return false;
        }
        return true;
    }
}

class GameFieldExtensionSearch {
    private Collection<Position> closed = new HashSet<>();
    

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
            return t.isExtendable(startDirection.getOpposite(), ty);
        }
        return false;
    }
    
    public static boolean hasNeighbourInDir(GameField field, Position pos, Direction dir) {
    	Tile toCheck = field.getTile(pos.inDirection(dir));
    			if(toCheck == null) {
    				return false;
    			}
    	return true;
    }
    
    // GEÄNDERT: checkClosed static (Rekursion)
    // Übergib HashSet, damit in Rekursion nicht jedes mal neues, aber bei komplett neuer Suche nicht selbes HashSet
	public Tuple<Boolean, HashSet<Position>> checkClosed(HashSet<Position> set, GameField field, Position pos, Type ty,
			Direction toCheck) {
		Tile thisTile = field.getTile(pos);
		if (thisTile == null) {
			return new Tuple(false, set);
		}
		if (set.contains(pos)) {
			return new Tuple(true, set);
		}

		set.add(pos);

		if (isSingleTileClosed(thisTile, ty, toCheck)) {
			return new Tuple(true, set);
		}  
		Collection<ResourceInformation> info = TileLogic.getResources(thisTile);
		for (ResourceInformation i : info) {
			if (i.getFirst() != ty) {
				continue;
			}
			for (Direction dir : i.getSecond()) {
				Position nextPos = pos.inDirection(dir);
				return this.checkClosed(set, field, nextPos, ty, dir);
			}

		}

		return null;
	}

    
    
    //TODO: untested!
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
