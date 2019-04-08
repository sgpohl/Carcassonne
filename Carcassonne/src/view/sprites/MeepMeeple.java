package view.sprites;

import java.awt.BasicStroke;
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
	private static final Color[] playerColors = {	new Color(255,0,0), 
													new Color(255,255,0),
													new Color(0,255,0),
													new Color(0,0,255),
													new Color(255,0,255),
													new Color(0,255,255),
													new Color(255,255,255),
													new Color(0,0,0),
												};

	private static final Color[] playerShades = {	playerColors[0].darker(), 
													playerColors[1].darker(),
													playerColors[2].darker(),
													playerColors[3].darker(),
													playerColors[4].darker(),
													playerColors[5].darker(),
													playerColors[6].darker(),
													new Color(60,60,60)
												};
	private static int colorUsed = 0;
	
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
		g.setColor(playerShades[colorUsed]);
		g.setStroke(new BasicStroke(4));
		for(var s : body)
			g.draw(s);
		
		g.setColor(playerColors[colorUsed]);
		for(var s : body)
			g.fill(s);
		
		colorUsed = (colorUsed+1)%playerColors.length;
	}
	

}
