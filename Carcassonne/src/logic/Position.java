package logic;

public class Position {

    private int x;
    private int y;


    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position inDirection(Direction dir) {
        switch(dir){
            case NORTH:
                return new Position(x, y+1);
            case SOUTH:
                return new Position(x, y-1);
            case EAST:
                return new Position(x+1, y);
            case WEST:
                return new Position(x-1, y);
        }
        return null;
    }


    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }


    @Override
    public String toString() {
        return "Position [X=" + x + ", Y=" + y + "]";
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
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
        Position other = (Position) obj;
        if (x != other.x)
            return false;
        return y == other.y;
    }


}
