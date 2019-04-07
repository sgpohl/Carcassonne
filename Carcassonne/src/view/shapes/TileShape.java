package view.shapes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;

public interface TileShape {
	public boolean contains(Point p);
	public void bakeInto(Graphics2D g);
}
