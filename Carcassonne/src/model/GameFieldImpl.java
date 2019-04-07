package model;

import logic.Position;
import logic.TileFactory;
import java.util.*;

public class GameFieldImpl implements GameField {
	
	private HashMap<Position,Tile> Tilemap;
	
	public GameFieldImpl() {
		this.Tilemap = new HashMap<>();
		this.Tilemap.put(new Position(0,0), TileFactory.getStartTile());
	}


    @Override
    public void set(Position p, Tile t) {
    	this.Tilemap.put(p, t);
    }

    @Override
    public Tile getTile(Position p) {
        return this.Tilemap.get(p);
    }

    @Override
    public Map<Position, Tile> getAllTiles() {
        return this.Tilemap;
    }
}
