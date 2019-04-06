package logic;

import model.GameField;
import model.Tile;
import util.Tuple;

import java.util.List;
import java.util.Map;

public class TileLogic {


    public static Tile getStartTile() {
        return new Tile(){
            @Override
            public boolean isExpendable(Direction direction, Type type) {
                return false;
            }

            public String toString(){
                return "Dummy Start Tile (Not yet implemented!)";
            }
        };
    }

    public static List<Tuple<Tile, Direction>> getPossibleExtension(Tile tile){
        return null;
    }
    public static Map<Direction, List<Type>> getPossibleExtension(GameField gf){
        return null;
    }





}
