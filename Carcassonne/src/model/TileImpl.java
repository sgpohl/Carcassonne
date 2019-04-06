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
		
		return false;
	}
	
	public static void main(String[] args) {
		TileImpl test1 = new TileImpl();
		test1.setDirection(Direction.NORTH, Type.GRASS);
		test1.setDirection(Direction.EAST, Type.RIVER);
		test1.setDirection(Direction.NORTH, Type.FOREST);
		test1.setDirection(Direction.NORTH, Type.FOREST);
		System.out.println(test1.DirectionType.toString());
	}

}
