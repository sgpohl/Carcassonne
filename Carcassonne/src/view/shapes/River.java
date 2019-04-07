package view.shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Path2D;

import view.TileGraphic;

public class River extends Path2D.Float implements TileShape {
	public River(Point from, Point to) {
		super();
		this.moveTo(from.x, from.y);
		int mid = TileGraphic.size/2;
		this.curveTo((mid+from.x)/2, (mid+from.y)/2, (mid+to.x)/2, (mid+to.y)/2, to.x, to.y);
	}

	@Override
	public void bakeInto(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.setStroke(new BasicStroke(TileGraphic.streetWidth));
		g.draw(this);
	}
}
