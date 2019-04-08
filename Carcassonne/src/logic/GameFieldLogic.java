package logic;

import model.GameField;
import model.GameFieldImpl;

import java.util.*;

import model.Tile;
import util.Tuple;
import view.UI;
import logic.TileLogic;
import logic.TileFactory;
import logic.GameFieldFactory;

//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Map;
import java.util.stream.Collectors;

public class GameFieldLogic {

    // startAtPos: Position des neu angelegten Tiles
    public static Map<ResourceInformation, Collection<Position>> getClosedAreas(GameField field, Position startAtPos) {
        Map<ResourceInformation, Collection<Position>> returnMap = new HashMap<>();
        Tile tile = field.getTile(startAtPos);
        var resources = TileLogic.getResources(tile);
        for (ResourceInformation resource : resources) {
        	GameFieldExtensionSearch toSearch = new GameFieldExtensionSearch();
        	HashSet<Position> connectedSet = new HashSet<>();
        	boolean result = toSearch.checkClosed(connectedSet, field, startAtPos, resource.getFirst(), null);
            
        	if(result) {
        		returnMap.put(resource, connectedSet);
            }
        	//TODO: implement
        	// else {highlightRessource}
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
	public boolean checkClosed(HashSet<Position> set, GameField field, Position pos, Type ty,
			Direction toCheck) {
		Tile thisTile = field.getTile(pos);
		if (thisTile == null) {
			return false;
		}
		if (set.contains(pos)) {
			return true;
		}

		set.add(pos);

		if (isSingleTileClosed(thisTile, ty, toCheck)) {
			return true;
		}  
		
		boolean recursiveResult = true;
		Collection<ResourceInformation> info = TileLogic.getResources(thisTile);
		for (ResourceInformation i : info) {
			if (i.getFirst() != ty) {
				continue;
			}
			if(toCheck != null && !i.getSecond().contains(toCheck.getOpposite())) {
				continue;
			}
			for (Direction dir : i.getSecond()) {
				Position nextPos = pos.inDirection(dir);
				boolean neighbourResult = this.checkClosed(set, field, nextPos, ty, dir);
				if(!neighbourResult)
					recursiveResult = false;
			}

		}

		return recursiveResult;
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
    
    // TESTEN VON TOCHECK
    public static void main(String[] args) {
    	
    	//GameFieldImpl TEST = new GameFieldImpl();
    	Random run = new Random(3);
    	/*Tile test1 = TileFactory.getRandomTile(run);
    	Tile test2 = TileFactory.getRandomTile(run);
    	Tile test3 = TileFactory.getRandomTile(run);
    	Tile test4 = TileFactory.getRandomTile(run);
    	Tile test5 = TileFactory.getRandomTile(run);
    	Tile test6 = TileFactory.getRandomTile(run);
    	Tile test7 = TileFactory.getRandomTile(run);
    	Tile test8 = TileFactory.getRandomTile(run);
    	TEST.set(new Position(-1, 0),test1);
    	TEST.set(new Position(-1, -1),test2);
    	TEST.set(new Position(0, 1),test3);
    	TEST.set(new Position(1, 1), test4);
    	TEST.set(new Position(1, 0), test5);
    	TEST.set(new Position(0, -1), test6);
    	TEST.set(new Position(2, 1), test7);
    	TEST.set(new Position(1, 2), test8);*/
    	GameField TEST = GameFieldFactory.getSampleField(run, 20);
    	//System.out.println(TEST.toString());
    	System.out.println(GameFieldLogic.getClosedAreas(TEST, new Position(0,0)));
    	
    	UI ui = new UI();
    	ui.drawAll(TEST);
    	ui.highlight(new Position(0,0), true);
    	
    }


}
