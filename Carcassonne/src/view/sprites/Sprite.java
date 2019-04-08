package view.sprites;

import java.awt.Graphics2D;
import java.awt.Point;

public abstract class Sprite {
	protected Point position;
	
	public Sprite(Point p) {
		position = p;
	}
	
	public Point getPosition() {
		return position;
	}
	
	public abstract void bakeInto(Graphics2D g);
}
