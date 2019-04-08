package logic;

import java.util.Collection;

public class Position {

    private int x;
    private int y;


    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position calcClosest(Collection<Position> posCol) {
        double minDist = Double.POSITIVE_INFINITY;
        Position minPos = null;

        for (Position curPos : posCol) {
            double curDist = this.distTo(curPos);
            if (curDist < minDist) {
                minDist = curDist;
                minPos = curPos;
            }
        }
        return minPos;

    }

    public double distTo(Position other) {
        double dx = other.x - x;
        double dy = other.y - y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public Position inDirection(Direction dir) {
        switch (dir) {
            case NORTH:
                return new Position(x, y + 1);
            case SOUTH:
                return new Position(x, y - 1);
            case EAST:
                return new Position(x + 1, y);
            case WEST:
                return new Position(x - 1, y);
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
        return x == other.x && y == other.y;
    }


}
