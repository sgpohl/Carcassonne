package model;

import java.util.*;

import logic.Position;

public interface GameField {

    void set(Position p, Tile t);

    Tile getTile(Position p);

    Map<Position, Tile> getAllTiles();

}
