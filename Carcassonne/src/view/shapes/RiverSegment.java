package view.shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Path2D;

import logic.Direction;
import logic.Type;
import view.ResourceInformation;
import view.TileGraphic;

public class RiverSegment extends Path2D.Float implements TileShape {
	private static final long serialVersionUID = -8284368756045162616L;	
	private final Color waterColor = new Color(50,50,200);

	public final static int riverWidth = 10;
	ResourceInformation info;
	
	boolean renderHighlighted;
	
	public RiverSegment(Point from, Point to) {
		super();

		info = new ResourceInformation(Type.RIVER);
		
		this.moveTo(from.x, from.y);
		int mid = TileGraphic.size/2;//+TileGraphic.border;
		this.curveTo((mid+from.x)/2, (mid+from.y)/2, (mid+to.x)/2, (mid+to.y)/2, to.x, to.y);
	}
	
	public void addDirectionInfo(Direction dir) {
		info.addDirection(dir);
	}

	@Override
	public void bakeInto(Graphics2D g) {
		if(renderHighlighted) {
			g.setColor(Color.RED);
			g.setStroke(new BasicStroke(riverWidth+TileGraphic.resourceHighlightWidth));
			g.draw(this);	
		}
		
		g.setColor(waterColor);
		g.setStroke(new BasicStroke(riverWidth));
		g.draw(this);
	}
	
	@Override
	public boolean contains(Point p) {
		int w = riverWidth/2;
		return super.intersects(p.getX()-w, p.getY()-w, 2*w, 2*w);
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
