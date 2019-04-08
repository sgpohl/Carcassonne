package model;

import logic.GameFieldLogic;
import logic.Position;
import logic.TileFactory;

import java.util.*;

public class GameFieldImpl implements GameField {

    private HashMap<Position, Tile> Tilemap;

    private Collection<Position> posWithEmptyNeighbours = new HashSet<>();

    public GameFieldImpl() {
        this.Tilemap = new HashMap<>();
        this.set(new Position(0, 0), TileFactory.getStartTile());
    }

    public synchronized Collection<Position> getPositionsWithEmptyNeighbours() {
        Collection<Position> posWithEmptyNeighbours = new HashSet<>();
        for (Position pos : this.posWithEmptyNeighbours)
            if (GameFieldLogic.isPositionWithEmptyNeighbours(this, pos))
                posWithEmptyNeighbours.add(pos);

        this.posWithEmptyNeighbours = posWithEmptyNeighbours;
        return this.posWithEmptyNeighbours;
    }


    @Override
    public synchronized void set(Position p, Tile t) {
        this.Tilemap.put(p, t);
        this.posWithEmptyNeighbours.add(p);
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
            return other.Tilemap == null;
        } else return Tilemap.equals(other.Tilemap);
    }


}
