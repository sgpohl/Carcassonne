package view.shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Path2D;
import java.util.Collection;

import logic.Direction;
import logic.Type;
import view.ResourceInformation;
import view.TileGraphic;

public class Grass extends Path2D.Float implements TileShape {
	private static final long serialVersionUID = -9019873546477228036L;
	
	private ResourceInformation info;
	
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
	}
	
	@Override
	public void bakeInto(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fill(this);
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
}
