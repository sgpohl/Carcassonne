package view.shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Collection;

import logic.Direction;
import logic.ResourceInformation;
import logic.Type;
import view.TileGraphic;
import view.UI;
import view.sprites.*;

public class Grass extends Path2D.Float implements TileShape {
	private static final long serialVersionUID = -9019873546477228036L;
	private static final Color grassColor = new Color(80,180,50); 
	private static final int minSprites = 0;
	private static final int maxSprites = 10;
	
	private ResourceInformation info;
	
	private boolean renderHighlighted;
	private Collection<Line2D> highlightStripes;
	private static final int highlightDensity = 4;
	
	private TileGraphic parent;
	private SpriteRenderer sprites;
	
	public Grass(Collection<Direction> directions, TileGraphic parent) {
		info = new ResourceInformation(Type.GRASS);
		for(var d : directions)
			info.addDirection(d);
		
		var border1 = TileGraphic.directionToBorder(Direction.NORTH);
		var border2 = TileGraphic.directionToBorder(Direction.SOUTH);
		
		this.moveTo(border1.getFirst().getX(), border1.getFirst().getY());
		this.lineTo(border1.getSecond().getX(), border1.getSecond().getY());
		this.lineTo(border2.getFirst().getX(), border2.getFirst().getY());
		this.lineTo(border2.getSecond().getX(), border2.getSecond().getY());
		this.closePath();
		
		highlightStripes = new ArrayList<Line2D>();
		float dy = (float)TileGraphic.size/(highlightDensity-1);
		float y = dy/2;
		for(int i = 0; i < highlightDensity; ++i, y+=dy)
			highlightStripes.add(new Line2D.Float(0, y, TileGraphic.size, y-dy));
		
		this.parent = parent;
		this.sprites = new SpriteRenderer();
		
	}
	
	@Override
	public void bakeInto(Graphics2D g) {
		g.setColor(grassColor);
		g.fill(this);
		
		if(renderHighlighted) {
			g.setColor(UI.highlightColor);
			g.setStroke(new BasicStroke(TileGraphic.resourceHighlightWidth/2));
			for(var stripe : highlightStripes)
				g.draw(stripe);
		}
	}

	@Override
	public boolean contains(Point p) {
		return super.contains(p);
	}

	@Override
	public ResourceInformation getInformation() {
		return info;
	}
	
	private boolean pointCollides(Point pos) {
		for(int dx=-10; dx<=10; dx+=10) {
			for(int dy=-10; dy<=10; dy+=10) {
				if(!this.getInformation().equals(this.parent.getResourceAt(new Point(pos.x+dx, pos.y+dy)))) {
					return false;
				}
			}
		}
		return true;
	}
	
	@Override
	public void bakeIntoForeground(Graphics2D g) {
		if(sprites.isEmpty()) {
			int targetSprites = minSprites + (int)(Math.random()*(maxSprites+1-minSprites));
			for(int i = 0; i<targetSprites; ++i)
				this.sprites.placeSpriteOnRandomPosition(GrassSprite::new, this::pointCollides);
		}
		sprites.bakeInto(g);
	}
	

	@Override
	public void highlight(boolean active) {
		renderHighlighted = active;
	}
}
