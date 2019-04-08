package controller.instances.ais;

import controller.instances.AIController;
import logic.Position;
import model.Tile;
import util.Tuple;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;

public class NearestToPositionAI extends AIController {

    private Position pos;

    public NearestToPositionAI(Position pos){
        this.pos = Objects.requireNonNull(pos);
    }

    @Override
    public Tuple<Position, Tile> choosePlacement(Collection<Tuple<Position, Tile>> placementOptions) {
        var comp = Comparator.comparing((Tuple<Position, Tile> option) -> option.getFirst().distTo(pos));
        return placementOptions.stream().min(comp).get();
    }

    public String toString(){
        return "AI (nearest to " + pos + ")";
    }

}
