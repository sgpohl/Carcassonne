package ai;

import logic.Position;
import model.GameField;
import model.Tile;
import util.Tuple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public interface Player {


    Tuple<Position, Tile> placeTile(GameField field, Collection<Tuple<Position, Tile>> placementOptions);



}
