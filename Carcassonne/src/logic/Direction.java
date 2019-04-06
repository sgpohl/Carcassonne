package logic;

public enum Direction {
    NORTH, EAST, SOUTH, WEST;

    public Direction getOpposite() {
        switch (this) {
            case NORTH: return SOUTH;
            case SOUTH: return NORTH;

            case EAST: return WEST;
            case WEST: return EAST;
        }
        return null;
    }

}
