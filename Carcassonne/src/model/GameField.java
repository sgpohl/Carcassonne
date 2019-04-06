package model;

import java.util.*;
import logic.Position;

public interface GameField extends Tile{
	
	public void set(Position p, Tile t);
	
	public Tile getTile(Position p);
	
	public Map<Position, Tile>getAllTiles();

}
