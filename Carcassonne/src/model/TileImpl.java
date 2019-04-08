package model;

import logic.Direction;
import logic.Type;

import java.util.*;

public class TileImpl implements Tile {

    private HashMap<Direction, Set<Type>> directionMap;

    public TileImpl() {
        this.directionMap = new HashMap<>();
    }

    public void setDirection(Direction key, Type types) {
        if (!this.directionMap.containsKey(key))
            this.directionMap.put(key, new HashSet<>());

        this.directionMap.get(key).add(types);
    }

    @Override
    public boolean isExtendable(Direction direction, Type type) {
        Set<Type> alignableTypes = this.directionMap.get(direction);
        if(alignableTypes == null)
        	return false;
        return alignableTypes.contains(type);
    }
    
    

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((directionMap == null) ? 0 : directionMap.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;

		if(! (obj instanceof Tile)){
			return false;
		}
		Tile otherTile = (Tile) obj;

		for(var dir: Direction.values())
			for(var type: Type.values())
				if(this.isExtendable(dir, type) != otherTile.isExtendable(dir,type))
					return false;

		return true;
	}

	@Override
    public String toString() {
        return "Tile: " + directionMap;
    }


}
