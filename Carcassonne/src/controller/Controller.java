package controller;

import logic.Position;

public class Controller {
	public void UI_clickedOnTile(Position pos) {
		System.out.println("clicked at "+pos.toString());
	}
	public void UI_rotateCurrentTile() {
		System.out.println("do teh rotato thing");
	}
}
