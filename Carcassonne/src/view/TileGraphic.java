package view;
import java.awt.*;
import model.Tile;

import logic.*;

public class TileGraphic {
	private final int size = 100;
	
	
	public TileGraphic(model.Tile tile) {
	}

	public void paint(Graphics2D g, Position pos, int offsetX, int offsetY) {
		int xCoord = pos.getX()*size -size/2;
		int yCoord = pos.getY()*size -size/2;
		
		g.fillRect(xCoord +offsetX, yCoord +offsetY, size, size);
	}
}
