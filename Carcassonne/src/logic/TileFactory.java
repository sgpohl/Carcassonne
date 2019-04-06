package logic;

import model.Tile;
import model.TileImpl;

import java.util.Random;

public class TileFactory {

    public static Tile getStartTile() {
        return new StartTile();
    }

    /**
     * Returns an empty Tile
     * @return an empty Tile
     */
    public static Tile getEmptyTile() {
        return new TileImpl();
    }

    /**
     * Returns a random Tile using the current timestamp as seed
     * @return a random Tile using the current timestamp as seed
     */
    public static Tile getRandomTile(){
        return getRandomTile(new Random());
    }

    /**
     * Returns a random tile with the given RNG
     * @param ran the random number generator
     * @return a random tile with the given RNG
     */
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
