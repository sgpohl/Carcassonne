package view.sprites;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Path2D;

public class TreeSprite extends Sprite {
	private Rectangle stump;
	private Path2D crown;
	
	public TreeSprite(Point pos) {
		super(pos);
		
		stump = new Rectangle(pos.x-2,pos.y-8, 4,8);
		crown = new Path2D.Float();
		crown.moveTo(pos.x, pos.y-30);
		crown.lineTo(pos.x+8, pos.y-5);
		crown.lineTo(pos.x-8, pos.y-5);
		crown.closePath();
	}
	
	@Override
	public void bakeInto(Graphics2D g) {
		g.setColor(new Color(80,50,12));
		g.fill(stump);
		g.setColor(new Color(40,120,40));
		g.fill(crown);
	}
}
