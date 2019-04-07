package logic;

import model.Tile;
import model.TileImpl;
import util.IterationUtility;

import java.util.Random;

public class TileFactory {
    /**
     * Returns a clockwise rotated copy of the given tile
     *
     * @param tile the tile to be rotated
     * @return a clockwise rotated copy of the given tile
     */
    public static Tile rotateClockwise(Tile tile) {
        Tile res = getEmptyTile();

        for (var dir : Direction.values())
            for (var type : Type.values())
                if (res.isExtendable(dir, type))
                    res.setDirection(dir.rotateClockwise(), type);

        return res;
    }

    public static Tile rotateCounterclockwise(Tile tile) {
        Tile res = getEmptyTile();

        IterationUtility.forEachTypeDirection(
                (type, dir) -> {
                    if (res.isExtendable(dir, type))
                        res.setDirection(dir, type);
                }
        );

        return res;

    }


    /**
     * Returns the starting tile of the game
     *
     * @return the starting tile of the game
     */
    public static Tile getStartTile() {
        return new StartTile();
    }

    /**
     * Returns an empty Tile
     *
     * @return an empty Tile
     */
    public static Tile getEmptyTile() {
        return new TileImpl();
    }

    /**
     * Returns a random Tile using the current timestamp as seed
     *
     * @return a random Tile using the current timestamp as seed
     */
    public static Tile getRandomTile() {
        return getRandomTile(new Random());
    }

    /**
     * Returns a random tile with the given RNG
     *
     * @param ran the random number generator
     * @return a random tile with the given RNG
     */
    public static Tile getRandomTile(Random ran) {
        Tile tile = getEmptyTile();

        for (var dir : Direction.values()) {
            tile.setDirection(dir, ran.nextDouble() < 0.2 ? Type.FOREST : Type.GRASS);
            if (ran.nextDouble() < 0.1)
                tile.setDirection(dir, Type.RIVER);
        }
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
    public void setDirection(Direction key, Type types) {
        throw new UnsupportedOperationException("This is an immutable StartTile dummy!");
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
