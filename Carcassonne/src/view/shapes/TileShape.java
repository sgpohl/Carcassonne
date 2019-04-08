package view.shapes;

import java.awt.Graphics2D;
import java.awt.Point;

import logic.ResourceInformation;

public interface TileShape {
	public boolean contains(Point p);
	
	public void bakeInto(Graphics2D g);
	public void bakeIntoForeground(Graphics2D g);
	
	public ResourceInformation getInformation();
	
	public void highlight(boolean active);
	
	//public Point getCenterPoint();
}
