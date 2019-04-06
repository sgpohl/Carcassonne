package view;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import model.Tile;

import logic.*;

public class TileGraphic {
	private final int size = 100;
	private final int border = 1;
	
	private Image displayImage;
	
	public TileGraphic(model.Tile tile) {
		
		displayImage = new BufferedImage(size, size,  BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)displayImage.getGraphics();
		g.setColor(Color.gray);
		g.fillRect(border, border, size -2*border, size -2*border);
		

		g.setColor(Color.black);
		Path2D street = new Path2D.Float();
		street.moveTo(0, size/2);
		street.curveTo(size/2, size/2, size/2, size/2, size/2, size);
		g.draw(street);
	}

	public void paint(Graphics2D g, Position pos, int offsetX, int offsetY) {
		int xCoord = pos.getX()*size -size/2;
		int yCoord = pos.getY()*size -size/2;
		
		AffineTransform transform = new AffineTransform();
		transform.translate(xCoord+offsetX, yCoord+offsetY);
		g.drawImage(displayImage, transform, null);
	}
}
