package model;

//import java.util.Map;

import logic.Direction;
import logic.Position;
import logic.Type;
import java.util.*;

public class GameFieldImpl implements GameField{
	
	

	@Override
	public boolean isExtendable(Direction direction, Type type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setDirection(Direction key, Type types) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void set(Position p, Tile t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Tile getTile(Position p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Position, Tile> getAllTiles() {
		// TODO Auto-generated method stub
		return null;
	}

}
