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
import logic.Type;
import view.ResourceInformation;
import view.TileGraphic;
import view.UI;

public class Grass extends Path2D.Float implements TileShape {
	private static final long serialVersionUID = -9019873546477228036L;
	private static final Color grassColor = new Color(80,180,50); 
	
	private ResourceInformation info;
	
	private boolean renderHighlighted;
	private Collection<Line2D> highlightStripes;
	private static final int highlightDensity = 4;
	
	public Grass(Collection<Direction> directions) {

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

	@Override
	public void bakeIntoForeground(Graphics2D g) {
	}
	

	@Override
	public void highlight(boolean active) {
		renderHighlighted = active;
	}
}
