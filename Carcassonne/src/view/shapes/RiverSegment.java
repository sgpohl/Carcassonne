package view.shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Path2D;

import logic.Direction;
import logic.ResourceInformation;
import logic.Type;
import view.TileGraphic;
import view.UI;

public class RiverSegment extends Path2D.Float implements TileShape {
	private static final long serialVersionUID = -8284368756045162616L;	
	public static final Color waterColor = new Color(40,40,150);

	public final static int riverWidth = 10;
	public final static double randomWiggle = 0.3;
	public final static double splitDeviation = 0.2;
	ResourceInformation info;
	
	boolean isSplit;
	boolean renderHighlighted;
	
	private int rnd(double dst) {
		return (int)((Math.random()-.5)*randomWiggle*dst);
	}
	
	public RiverSegment(Point from, Point to, Point mid, boolean split) {
		super();

		info = new ResourceInformation(Type.RIVER);
		
		double dst = from.distance(to);
		this.moveTo(from.x, from.y);
		
		isSplit = split;
		if(!split) {
			this.curveTo((mid.x+from.x)/2 +rnd(dst), (mid.y+from.y)/2+rnd(dst), (mid.x+to.x)/2+rnd(dst), (mid.y+to.y)/2+rnd(dst), to.x, to.y);
		}
		else {
			Point difference = new Point(from.x-to.x, from.y-to.y);
			Point othorgonal = new Point(difference.y, -difference.x);
			othorgonal.x *= splitDeviation;
			othorgonal.y *= splitDeviation;
			
			int p1x = (mid.x+from.x)/2	+rnd(dst);
			int p1y = (mid.x+from.y)/2	+rnd(dst);
			int p2x = (mid.x+to.x)/2	+rnd(dst);
			int p2y = (mid.x+to.x)/2	+rnd(dst);
			
			this.curveTo(p1x+othorgonal.x, p1y+othorgonal.y, p2x+othorgonal.x, p2y+othorgonal.y, to.x, to.y);
			this.curveTo(p2x-othorgonal.x, p2y-othorgonal.y, p1x-othorgonal.x, p1y-othorgonal.y, from.x, from.y);
		}
	}
	public RiverSegment(Point from, Point to, Point mid) {
		this(from, to, mid, false);
	}
	public RiverSegment(Point from, Point to, boolean split) {
		this(from, to, new Point((from.x+to.x)/2, (from.y+to.y)/2), false);
	}
	public RiverSegment(Point from, Point to) {
		this(from, to, false);
	}
	
	public void addDirectionInfo(Direction dir) {
		info.addDirection(dir);
	}

	@Override
	public void bakeInto(Graphics2D g) {
		if(renderHighlighted) {
			g.setColor(UI.highlightColor);
			g.setStroke(new BasicStroke(riverWidth+TileGraphic.resourceHighlightWidth));
			g.draw(this);	
		}
		
		g.setColor(waterColor);
		if(isSplit)
			g.setStroke(new BasicStroke((int)(riverWidth/1.5)));
		else
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
