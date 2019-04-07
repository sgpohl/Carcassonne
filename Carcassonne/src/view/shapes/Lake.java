package view.shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;

import view.ResourceInformation;
import view.TileGraphic;

public class Lake extends Ellipse2D.Float implements TileShape {
	private static final long serialVersionUID = 9192036957975715607L;
	
	private final static int size = 20;

	public Lake() {
		super((TileGraphic.size-size)/2, (TileGraphic.size-size)/2, size, size);
	}
	
	@Override
	public boolean contains(Point p) {
		return false;
	}

	@Override
	public void bakeInto(Graphics2D g) {
		g.setColor(Color.CYAN);
		g.fill(this);
	}

	@Override
	public ResourceInformation getInformation() {
		return null;
	}

}
