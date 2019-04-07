package controller;

import logic.Position;

public class Controller {
	view.UI ui;
	
	public Controller() {
		ui = new view.UI(this);
	}
	
	public view.UI getUI() {
		return ui;
	}
	
	public void UI_clickedOnTile(Position pos) {
		System.out.print("clicked at "+pos.toString());
		var resourceInfo = ui.getResourceSelection();
		if(resourceInfo != null)
			System.out.print(", found Resource: "+resourceInfo.getFirst());
		System.out.println();
	}
	public void UI_rotateCurrentTile() {
		System.out.println("do teh rotato thing");
	}
}
