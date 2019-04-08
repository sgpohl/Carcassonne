package view.shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;

import logic.ResourceInformation;
import view.TileGraphic;

public class Lake extends Ellipse2D.Float implements TileShape {
	private static final long serialVersionUID = 9192036957975715607L;
	private static final Color lakeColor = new Color(100,100,200);
	
	private final static int size = 20;

	public Lake() {
		//super((TileGraphic.size-size)/2+TileGraphic.border, (TileGraphic.size-size)/2+TileGraphic.border, size, size);
		super((TileGraphic.size-size)/2, (TileGraphic.size-size)/2, size, size);
	}
	
	@Override
	public boolean contains(Point p) {
		return false;
	}

	@Override
	public void bakeInto(Graphics2D g) {
		g.setColor(lakeColor);
		g.fill(this);
	}

	@Override
	public ResourceInformation getInformation() {
		return null;
	}

	@Override
	public void bakeIntoForeground(Graphics2D g) {
	}

	@Override
	public void highlight(boolean active) {
		throw new UnsupportedOperationException();
	}
}
