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

    
    

	@Override
	public String toString() {
		return "GameFieldImpl [Tilemap=" + Tilemap + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Tilemap == null) ? 0 : Tilemap.hashCode());
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
		GameFieldImpl other = (GameFieldImpl) obj;
		if (Tilemap == null) {
			if (other.Tilemap != null)
				return false;
		} else if (!Tilemap.equals(other.Tilemap))
			return false;
		return true;
	}
    
    
}
