package view.shapes;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Path2D;

import logic.Direction;
import util.Tuple;
import view.TileGraphic;

public class Forest extends Path2D.Float implements TileShape {
	
	/***
	 * generates an all-border forest
	 */
	public Forest() {
		var border1 = TileGraphic.directionToBorder(Direction.NORTH);
		var border2 = TileGraphic.directionToBorder(Direction.SOUTH);
		
		this.moveTo(border1.getFirst().getX(), border1.getFirst().getY());
		this.lineTo(border1.getSecond().getX(), border1.getSecond().getY());
		this.lineTo(border2.getFirst().getX(), border2.getFirst().getY());
		this.lineTo(border2.getSecond().getX(), border2.getSecond().getY());
		this.closePath();
	}
	
	/***
	 * generates a single-border forest
	 * @param dir
	 */
	public Forest(Direction dir) {
		super();
		
		Tuple<Point, Point> borders;
		borders = TileGraphic.directionToBorder(dir);
		int x1 = borders.getFirst().x;
		int y1 = borders.getFirst().y;
		
		int x2 = borders.getSecond().x;
		int y2 = borders.getSecond().y;
		
		this.moveTo(x1, y1);
		int middle = TileGraphic.size/2;
		this.curveTo((x1+middle)/2, (y1+middle)/2, (x2+middle)/2, (y2+middle)/2, x2, y2);
		this.closePath();
	}
	
	/***
	 * generates a multi-border forest
	 * @param clockwiseStart
	 * @param clockwiseEnd
	 */
	public Forest(Direction clockwiseStart, Direction clockwiseEnd) {
		super();
		
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
		int middle = TileGraphic.size/2;
		
		this.curveTo((p11+middle)/2, (p12+middle)/2, (p21+middle)/2, (p22+middle)/2, x2, y2);
		this.lineTo(p21, p22);
		this.lineTo(p11, p12);
		this.closePath();
	}
	
	@Override
	public void bakeInto(Graphics2D g) {
		g.setColor(new Color(120,80,20));
		g.fill(this);
	}

	@Override
	public boolean contains(Point p) {
		return super.contains(p);
	}

}
