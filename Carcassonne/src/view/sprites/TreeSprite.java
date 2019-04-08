package view.sprites;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Path2D;

public class TreeSprite extends Sprite {
	private static final Color treeStumpColor = new Color(80,50,12);
	private static final Color treeCrownColor = new Color(40,120,40);
	
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

		g.setStroke(new BasicStroke(2));
		g.setColor(treeCrownColor.darker());
		g.draw(crown);
		g.setColor(treeStumpColor.darker());
		g.draw(stump);
		
		g.setColor(treeStumpColor);
		g.fill(stump);
		g.setColor(treeCrownColor);
		g.fill(crown);
		
	}
}
