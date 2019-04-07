package view.shapes;

import java.awt.Graphics2D;
import java.awt.Shape;

public interface TileShape extends Shape {
	public void bakeInto(Graphics2D g);
}
