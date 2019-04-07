package controller;

import logic.Position;

public class Controller {
	public void UI_clickedOnTile(Position pos) {
		System.out.println("clicked at "+pos.toString());
	}
}
