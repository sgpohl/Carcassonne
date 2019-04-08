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
    	return null;
    }

}
