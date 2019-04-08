package logic;

import model.Tile;

import java.util.*;
import java.util.function.Function;

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
    
    public static Map<Type, List<Direction>> getResourceDirections(Tile tile) {
		var information = TileLogic.getExtendableOptions(tile);
		var reformattedInformation = new HashMap<Type, List<Direction>>();
		
		for(Type t : Type.values())
			reformattedInformation.put(t, new ArrayList<Direction>());
		for(Direction d : information.keySet()) {
			var types = information.get(d);
			if(types != null)
				for(Type t : types)
					reformattedInformation.get(t).add(d);
		}
		
		return reformattedInformation;
    }
    
    public static Collection<ResourceInformation> getResources(Tile tile) {
    	var info = getResourceDirections(tile);
    	
    	var result = new ArrayList<ResourceInformation>();
    	for(Type t : Type.values())
    		result.addAll(applyResourceSelector(t, info));
    		
    	return result;
    }

    private static Collection<ResourceInformation> applyResourceSelector(Type t, Map<Type, List<Direction>> info) {
    	switch(t) {
    	case GRASS:   		return getConnectedBackgroundResources(t, info);
    	case FOREST:   		return getConnectedBackgroundResources(t, info);
    	case RIVER:    		return getConnectedForegroundResources(t, info);
    	default: 			throw new UnsupportedOperationException("Tried to connect unknown Resource Type (TileLogic.getResources)");
    	}
    }
    
    private static Collection<ResourceInformation> getConnectedBackgroundResources(Type t, Map<Type, List<Direction>> info) {
    	Collection<Direction> directions = info.get(t);
    	
    	var result = new ArrayList<ResourceInformation>();
    	if(directions == null)
    		return result;
    	
    	var unmatchedDirections = new ArrayList<Direction>();
    	unmatchedDirections.addAll(directions);
    	while(!unmatchedDirections.isEmpty()) {
    		var resource = new ResourceInformation(t);
    		
    		Direction currentDir = unmatchedDirections.get(0);
    		int max = 4;
    		while(unmatchedDirections.contains(currentDir) && max > 0) {
    			currentDir = currentDir.rotateCounterclockwise();
    			max--;
    		}
    		currentDir = currentDir.rotateClockwise();
    		
    		while(unmatchedDirections.contains(currentDir)) {
    			resource.addDirection(currentDir);
    			unmatchedDirections.remove(currentDir);
    			currentDir = currentDir.rotateClockwise();
    		}
    		result.add(resource);
    	}
    	
    	return result;
    }

    private static Collection<ResourceInformation> getConnectedForegroundResources(Type t, Map<Type, List<Direction>> info) {
    	var result = new ArrayList<ResourceInformation>();
    	Collection<Direction> directions = info.get(t);
    	if(directions != null) {
    		var res = new ResourceInformation(t, directions);
    		result.add(res);
    	}
    	return result;
    }
}
