package logic;

import model.Tile;

import java.util.*;

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
    	var rivers = getConnectedForegroundResources(Type.RIVER, info);
    	var forests = getConnectedBackgroundResources(Type.FOREST, info);
    	var grassland = getConnectedBackgroundResources(Type.GRASS, info);
    	
    	var result = new ArrayList<ResourceInformation>();
    	result.addAll(rivers);
    	result.addAll(forests);
    	result.addAll(grassland);
    	return result;
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
    		while(unmatchedDirections.contains(currentDir)) {
    			currentDir = currentDir.rotateCounterclockwise();
    		}
    		currentDir = currentDir.rotateClockwise();
    		
    		while(unmatchedDirections.contains(currentDir)) {
    			resource.addDirection(currentDir);
    			unmatchedDirections.remove(0);
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
