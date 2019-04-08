package view.sprites;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Collection;

public class MeepMeeple extends Sprite {
	Collection<Shape> body;
	
	private int scale(int value) {
		return (int)(value*1.);
	}
	
	public MeepMeeple(Point p) {
		super(p);
		body = new ArrayList<Shape>();
		
		
		body.add(new Rectangle(p.x-scale(8),p.y-scale(30),scale(16),scale(30)));
		body.add(new Rectangle(p.x-scale(15),p.y-scale(30),scale(30),scale(10)));
		body.add(new Ellipse2D.Float(p.x-scale(10),p.y-scale(52),scale(20),scale(20)));
	}

	@Override
	public void bakeInto(Graphics2D g) {
		g.setColor(Color.YELLOW);
		for(var s : body)
			g.fill(s);
	}
	

}
