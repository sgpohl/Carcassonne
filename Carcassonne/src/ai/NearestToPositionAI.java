package ai;

import logic.Position;
import model.GameField;
import model.Tile;
import util.Tuple;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;

public class NearestToPositionAI implements Player {

    private final Position pos;

    public NearestToPositionAI(Position pos) {
        this.pos = Objects.requireNonNull(pos);
    }


    @Override
    public Tuple<Position, Tile> placeTile(GameField field, Collection<Tuple<Position, Tile>> placementOptions) {

        var comp = Comparator.comparing((Tuple<Position, Tile> option) -> option.getFirst().distTo(pos));
        return placementOptions.stream().min(comp).get();
    }
}
