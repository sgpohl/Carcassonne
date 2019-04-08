package controller;

import logic.Position;
import model.Tile;
import util.Tuple;

import java.util.Collection;

public interface Controller {


    default void player_placedTile(Player player, Position pos, Tile tile){
    }

    default void UI_rotateCurrentTile(){
    }

    default void UI_clickedOnTile(Position pos) {
    }

    default void game_startNewTurn(Player activePlayer, Collection<Tuple<Position, Tile>> placementOptions){
    }



}
