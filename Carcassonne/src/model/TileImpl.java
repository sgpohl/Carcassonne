package model;

import logic.Direction;
import logic.Type;
import logic.TileLogic;

import java.util.*;

public class TileImpl implements Tile {

    private HashMap<Direction, Set<Type>> DirectionType;

    public TileImpl() {
        this.DirectionType = new HashMap<>();
    }

    public void setDirection(Direction key, Type types) {
        if (!this.DirectionType.containsKey(key))
            this.DirectionType.put(key, new HashSet<>());

        this.DirectionType.get(key).add(types);
    }

    @Override
    public boolean isExtendable(Direction direction, Type type) {
        Set<Type> alignableTypes = this.DirectionType.get(direction);
        return alignableTypes.contains(type);
    }
    
    

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((DirectionType == null) ? 0 : DirectionType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TileImpl other = (TileImpl) obj;
		if (DirectionType == null) {
			if (other.DirectionType != null)
				return false;
		} else if (!DirectionType.equals(other.DirectionType))
			return false;
		return true;
	}

	@Override
    public String toString() {
        return "Tile: " + DirectionType;
    }


}
