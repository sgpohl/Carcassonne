package model;

import logic.Direction;
import logic.Type;
import logic.TileLogic;
import java.util.*;

public class TileImpl implements Tile {
	HashMap<Direction ,Set<Type>> DirectionType;
	
	public TileImpl() {
		this.DirectionType = new HashMap<>();
	}
	
	public void setDirection(Direction key, Type types) {
		if (!this.DirectionType.containsKey(key)) {
			Set<Type> toAdd = new HashSet<>(); 
			toAdd.add(types);
			this.DirectionType.put(key, toAdd);
		} else {
			this.DirectionType.get(key).add(types);
		}
	}
	

	
	
	
	@Override
	public boolean isExtendable(Direction direction, Type type) {
		Set alignableTypes = this.DirectionType.get(direction);
		return alignableTypes.contains(type);
	}
	


}
