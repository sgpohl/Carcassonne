package logic;

import model.Tile;
import model.TileImpl;

import java.util.Random;

public class TileFactory {

    public static Tile getStartTile() {
        return new StartTile();
    }

    public static Tile getEmptyTile() {
        return new TileImpl();
    }

    public static Tile getRandomTile(Random ran) {
        Tile tile = getEmptyTile();

        for (var dir : Direction.values())
            for (var type : Type.values())
                if (ran.nextBoolean())
                    tile.setDirection(dir, type);

        return tile;
    }

}


class StartTile implements Tile {

    @Override
    public boolean isExtendable(Direction dir, Type type) {
        if (type == Type.GRASS && (dir == Direction.SOUTH || dir == Direction.WEST)) {
            return true;
        }
        if (type == Type.RIVER && dir == Direction.WEST) {
            return true;
        }
        return type == Type.FOREST && (dir == Direction.NORTH || dir == Direction.EAST);
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Tile)) {
            return false;
        }
        Tile otherTile = (Tile) other;
        for (var dir : Direction.values())
            for (var type : Type.values())
                if (otherTile.isExtendable(dir, type) != isExtendable(dir, type))
                    return false;
        return true;
    }

    @Override
    public String toString() {
        return "Dummy Start Tile (poorely implemented!)";
    }
}
