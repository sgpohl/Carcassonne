package view.shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import logic.Direction;
import logic.Type;
import util.Tuple;
import view.ResourceInformation;
import view.TileGraphic;
import view.sprites.Sprite;

public abstract class BackgroundSpline extends Path2D.Float implements TileShape {
	private static final long serialVersionUID = 5068056862855701751L;
	
	protected ResourceInformation info;
	private Color backgroundColor;
	protected List<Sprite> sprites;
	
	boolean renderHighlighted;
	
	public BackgroundSpline(Color c) {
		info = new ResourceInformation(Type.FOREST);
		backgroundColor = c;
		sprites = new ArrayList<Sprite>();
	}

	/***
	 * generates an all-border resource
	 */
	protected void generateAllDirection() {
		info.addDirection(Direction.NORTH);
		info.addDirection(Direction.EAST);
		info.addDirection(Direction.SOUTH);
		info.addDirection(Direction.WEST);
		
		var border1 = TileGraphic.directionToBorder(Direction.NORTH);
		var border2 = TileGraphic.directionToBorder(Direction.SOUTH);
		
		this.moveTo(border1.getFirst().getX(), border1.getFirst().getY());
		this.lineTo(border1.getSecond().getX(), border1.getSecond().getY());
		this.lineTo(border2.getFirst().getX(), border2.getFirst().getY());
		this.lineTo(border2.getSecond().getX(), border2.getSecond().getY());
		this.closePath();
	}

	
	/***
	 * generates a single-border resource
	 * @param dir
	 */
	protected void generateSingleDirection(Direction dir) {
		info.addDirection(dir);
		
		Tuple<Point, Point> borders;
		borders = TileGraphic.directionToBorder(dir);
		int x1 = borders.getFirst().x;
		int y1 = borders.getFirst().y;
		
		int x2 = borders.getSecond().x;
		int y2 = borders.getSecond().y;
		
		this.moveTo(x1, y1);
		int middle = TileGraphic.size/2;// + TileGraphic.border;
		this.curveTo((x1+middle)/2, (y1+middle)/2, (x2+middle)/2, (y2+middle)/2, x2, y2);
		this.closePath();
	}

	/***
	 * generates a multi-border resource
	 * @param clockwiseStart
	 * @param clockwiseEnd
	 */
	protected void generateMultiDirection(Direction clockwiseStart, Direction clockwiseEnd) {
		for(var d = clockwiseStart; d != clockwiseEnd; d = d.rotateClockwise())
			info.addDirection(d);
		info.addDirection(clockwiseEnd);
		
		
		Tuple<Point, Point> startBorders;
		startBorders = TileGraphic.directionToBorder(clockwiseStart);
		Tuple<Point, Point> endBorders;
		endBorders = TileGraphic.directionToBorder(clockwiseEnd);

		int x1 = startBorders.getFirst().x;
		int y1 = startBorders.getFirst().y;
		
		int p11 = startBorders.getSecond().x;
		int p12 = startBorders.getSecond().y;		
		
		int p21 = endBorders.getFirst().x;
		int p22 = endBorders.getFirst().y;
		
		int x2 = endBorders.getSecond().x;
		int y2 = endBorders.getSecond().y;
		
		this.moveTo(x1, y1);
		int middle = TileGraphic.size/2;// + TileGraphic.border;
		
		this.curveTo((p11+middle)/2, (p12+middle)/2, (p21+middle)/2, (p22+middle)/2, x2, y2);
		this.lineTo(p21, p22);
		this.lineTo(p11, p12);
		this.closePath();
	}

	@Override
	public void bakeInto(Graphics2D g) {
		if(renderHighlighted) {
			g.setColor(Color.RED);
			g.setStroke(new BasicStroke(TileGraphic.resourceHighlightWidth));
			g.draw(this);
		}
		
		g.setColor(backgroundColor);
		g.fill(this);
	}
	
	@Override
	public void bakeIntoForeground(Graphics2D g) {
		for(var sprite : sprites)
			sprite.bakeInto(g);
	}


	@Override
	public boolean contains(Point p) {
		return super.contains(p);
	}

	@Override
	public ResourceInformation getInformation() {
		return info;
	}
	
	public void orderSprites() {
		Collections.sort(sprites, Comparator.comparing(sprite -> sprite.getPosition().y));
	}

	@Override
	public void highlight(boolean active) {
		renderHighlighted = active;
	}
}
