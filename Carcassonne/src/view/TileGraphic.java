package view;
import java.awt.*;
import model.Tile;

import logic.*;

public class TileGraphic {
	private final int size = 100;
	private final int border = 1;
	
	public TileGraphic(model.Tile tile) {
	}

	public void paint(Graphics2D g, Position pos, int offsetX, int offsetY) {
		int xCoord = pos.getX()*size -size/2;
		int yCoord = pos.getY()*size -size/2;
		
		g.fillRect(xCoord +offsetX+border, yCoord +offsetY +border, size -2*border, size -2*border);
	}
}
