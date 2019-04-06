package view;
import java.awt.*;
import model.Tile;

import logic.*;

public class TileGraphic {
	private final int size = 100;
	
	
	public TileGraphic(model.Tile tile) {
	}

	public void paint(Graphics2D g, Position pos) {
		int xOff = pos.getX()*size;
		int yOff = pos.getY()*size;
		
		g.fillRect(xOff, yOff, size, size);
	}
}
