package view.sprites;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GrassSprite extends Sprite {
	private static final int strawCount = 10;
	private static final float strawDensity = 0.4f;
	//private static final Color grassColor = new Color(180,180,80);
	private static final Color grassColor = new Color(80,180,80);
	
	private class StrawSprite extends Sprite {
		private float angle;
		
		public StrawSprite(Point pos, float angle) {
			super(pos);
			this.angle = angle;
		}
		
		@Override
		public void bakeInto(Graphics2D g) {
			int height = 10;
			
			var oldTransform = g.getTransform();
			
			g.translate(position.x, position.y);
			g.rotate(angle);
			
			//g.setColor(new Color(160,160,70));
			g.setColor(grassColor.darker());
			g.setStroke(new BasicStroke(3));
			g.drawLine(0,0,0,-height);
			g.fillOval(0,0-height-5, 3, 10);
			
			g.setColor(grassColor);
			g.setStroke(new BasicStroke(1));
			g.drawLine(0,0,0,-height);
			g.fillOval(0,0-height-4, 2, 8);
			
			g.setTransform(oldTransform);
		}
		
	};
	
	private List<StrawSprite> straws;
	
	public GrassSprite(Point pos) {
		super(pos);
		straws = new ArrayList<StrawSprite>();
		
		for(int i = 0; i < strawCount; ++i) {
			int x = (int)((Math.random()-.5)*strawCount/strawDensity);
			int y = (int)((Math.random()-.5)*strawCount/strawDensity/2);
			float angle = (float) (Math.random()-0.5f);
			straws.add(new StrawSprite(new Point(x,y), angle));
		}
		Collections.sort(straws, Comparator.comparing(straw -> straw.getPosition().y));
	}
	
	@Override
	public void bakeInto(Graphics2D g) {
		var oldTransform = g.getTransform();
		g.translate(position.x, position.y);
		
		for(var straw : straws)
			straw.bakeInto(g);
		
		g.setTransform(oldTransform);
	}

}
