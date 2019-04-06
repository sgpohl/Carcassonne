package model;

import logic.Direction;
import logic.Type;

public interface Tile {
	
	public boolean isExpendable(Direction direction, Type type);
	
	public Map[Direction, List[Type]] getExpandableOptions();
	

}
