package view;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import util.*;
import view.collision.*;
import logic.*;

public class TileGraphic {
	public final static int size = 100;
	private final static int border = 0;
	
	private final static int streetWidth = 10;
	private final static int villageSize = 20;
	
	private Image displayImage;
	
	private Deque<ResourceShape> collisionShapes;
	
	public static Point PosToCoord(Position pos, double scale) {
		int xCoord = (int)(pos.getX()*size*scale	-size/2);
		int yCoord = (int)(-pos.getY()*size*scale	-size/2);
		return new Point(xCoord, yCoord);
	}
	
	private static Point directionToCoordinate(Direction dir) {
		switch(dir) {
		case NORTH:
			return new Point(size/2, border);
		case EAST:
			return new Point(size-border, size/2);
		case SOUTH:
			return new Point(size/2, size-border);
		case WEST:
			return new Point(border, size/2);
		default:
			return new Point(size/2, size/2);
		}
	}
	
	private static Tuple<Point, Point> directionToBorder(Direction dir) {
		Point first;
		Point second;
		switch(dir) {
		case NORTH:
			first = new Point(0,0);
			second = new Point(size,0);
			return new Tuple<Point, Point>(first, second);
		case EAST:
			first = new Point(size,0);
			second = new Point(size,size);
			return new Tuple<Point, Point>(first, second);
		case SOUTH:
			first = new Point(size,size);
			second = new Point(0,size);
			return new Tuple<Point, Point>(first, second);
		case WEST:
			first = new Point(0,size);
			second = new Point(0,0);
			return new Tuple<Point, Point>(first, second);
		}
		return null;
	}
	

	public TileGraphic(model.Tile tile) {
		displayImage = new BufferedImage(size, size,  BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)displayImage.getGraphics();
		g.setColor(Color.GREEN);
		g.fillRect(border, border, size -2*border, size -2*border);
		
		collisionShapes = new LinkedList<ResourceShape>();
		Map<Direction, List<Type>> information = TileLogic.getExtendableOptions(tile);
		
		List<Direction> streetDirections = new ArrayList<Direction>();
		List<Direction> forestDirections = new ArrayList<Direction>();
		List<Direction> grassDirections = new ArrayList<Direction>();
		for(Direction d : information.keySet()) {
			if(information.get(d).contains(Type.RIVER))
				streetDirections.add(d);
			if(information.get(d).contains(Type.FOREST))
				forestDirections.add(d);
			if(information.get(d).contains(Type.GRASS))
				grassDirections.add(d);
				
		}
		
		Shape grass = new Rectangle(border, border, size -2*border, size -2*border);
		g.draw(grass);
		
		if(!grassDirections.isEmpty()) {
			var info = new ResourceInformation(Type.GRASS);
			var shapes = new ResourceShape(info);
			shapes.addShape(grass);
			for(var dir : grassDirections) 
				info.addDirection(dir);
			collisionShapes.addFirst(shapes);
		}
		
		
		drawForests(forestDirections);
		drawAllStreets(streetDirections);
	}

	
	private void insertVillage() {
		Graphics2D g = (Graphics2D)displayImage.getGraphics();
		g.setColor(Color.CYAN);
		g.fillRect((size-villageSize)/2, (size-villageSize)/2, villageSize, villageSize);
	}
	
	private Shape drawStreet(Point from, Point to) {
		Graphics2D g = (Graphics2D)displayImage.getGraphics();
		g.setColor(Color.BLUE);
		g.setStroke(new BasicStroke(streetWidth));
		
		Path2D street = new Path2D.Float();
		street.moveTo(from.x, from.y);
		int mid = size/2;
		street.curveTo((mid+from.x)/2, (mid+from.y)/2, (mid+to.x)/2, (mid+to.y)/2, to.x, to.y);
		g.draw(street);

		return street;
	}
	
	private void drawAllStreets(List<Direction> directions) {
		if(directions.isEmpty())
			return;
		
		var info = new ResourceInformation(Type.RIVER);
		var shapes = new ResourceShape(info);
		shapes.setIntersectionRadius(streetWidth/2);
		
		//two directions -> bezier ...
		if(directions.size() == 2) {
			
			Point from = directionToCoordinate(directions.get(0));
			Point to = directionToCoordinate(directions.get(1));
			Shape street = drawStreet(from, to); 
			
			shapes.addShape(street);
			info.addDirection(directions.get(0));
			info.addDirection(directions.get(1));
		}
		//otherwise straight lines connecting in the middle
		else {
			Point to = new Point(size/2, size/2);
			for(Direction dir : directions) {
				
				Point from = directionToCoordinate(dir);
				Shape street = drawStreet(from, to);
				
				shapes.addShape(street);
				info.addDirection(dir);
			}
			
			insertVillage();
		}

		collisionShapes.addFirst(shapes);
	}
	
	private Shape drawSingleForest(Graphics2D g, Direction dir) {
		Tuple<Point, Point> borders;
		borders = directionToBorder(dir);
		int x1 = borders.getFirst().x;
		int y1 = borders.getFirst().y;
		
		int x2 = borders.getSecond().x;
		int y2 = borders.getSecond().y;
		
		Path2D forest = new Path2D.Float();
		forest.moveTo(x1, y1);
		int middle = size/2;
		forest.curveTo((x1+middle)/2, (y1+middle)/2, (x2+middle)/2, (y2+middle)/2, x2, y2);
		forest.closePath();
		g.fill(forest);

		return forest;
	}
	
	private Shape drawMultiForest(Graphics2D g, Direction clockwiseStart, Direction clockwiseEnd) {
		Tuple<Point, Point> startBorders;
		startBorders = directionToBorder(clockwiseStart);
		Tuple<Point, Point> endBorders;
		endBorders = directionToBorder(clockwiseEnd);

		int x1 = startBorders.getFirst().x;
		int y1 = startBorders.getFirst().y;
		
		int p11 = startBorders.getSecond().x;
		int p12 = startBorders.getSecond().y;		
		
		int p21 = endBorders.getFirst().x;
		int p22 = endBorders.getFirst().y;
		
		int x2 = endBorders.getSecond().x;
		int y2 = endBorders.getSecond().y;
		
		Path2D forest = new Path2D.Float();
		forest.moveTo(x1, y1);
		int middle = size/2;
		
		forest.curveTo((p11+middle)/2, (p12+middle)/2, (p21+middle)/2, (p22+middle)/2, x2, y2);
		forest.lineTo(p21, p22);
		forest.lineTo(p11, p12);
		forest.closePath();
		g.fill(forest);
		
		return forest;
	}
	
	private void drawForests(List<Direction> directions) {
		Graphics2D g = (Graphics2D)displayImage.getGraphics();
		g.setColor(new Color(120,80,20));

		
		if(directions.size() == 1) {
			
			Shape forest = drawSingleForest(g, directions.get(0));
			
			var info = new ResourceInformation(Type.FOREST);
			var shapes = new ResourceShape(info);
			shapes.addShape(forest);
			info.addDirection(directions.get(0));
			collisionShapes.addFirst(shapes);
			
			return;
		}
		if(directions.size() == 2) {
			if(directions.get(0).equals(directions.get(1).getOpposite())) {
				Shape forest = drawSingleForest(g, directions.get(0));

				var info = new ResourceInformation(Type.FOREST);
				var shapes = new ResourceShape(info);
				shapes.addShape(forest);
				info.addDirection(directions.get(0));
				collisionShapes.addFirst(shapes);
				
				
				forest = drawSingleForest(g, directions.get(1));

				info = new ResourceInformation(Type.FOREST);
				shapes = new ResourceShape(info);
				shapes.addShape(forest);
				info.addDirection(directions.get(1));
				collisionShapes.addFirst(shapes);
				return;
			}
			else {
				Direction firstDir = directions.get(0);
				Direction secondDir = directions.get(1);
				if(firstDir.rotateClockwise() != directions.get(1)) {
					firstDir = directions.get(1);
					secondDir = directions.get(0);
				}
				Shape forest = drawMultiForest(g, firstDir, secondDir);
				
				var info = new ResourceInformation(Type.FOREST);
				var shapes = new ResourceShape(info);
				shapes.addShape(forest);
				info.addDirection(firstDir);
				info.addDirection(secondDir);
				collisionShapes.addFirst(shapes);
				
				return;
			}
		}
		if(directions.size() == 3) {
			Direction firstDir = Direction.NORTH;
			while(directions.contains(firstDir))
				firstDir = firstDir.rotateClockwise();
			firstDir = firstDir.rotateClockwise();
			Direction secondDir = firstDir.rotateClockwise();
			Direction thirdDir = firstDir.getOpposite();
			Shape forest = drawMultiForest(g, firstDir, thirdDir);
			

			var info = new ResourceInformation(Type.FOREST);
			var shapes = new ResourceShape(info);
			shapes.addShape(forest);
			info.addDirection(firstDir);
			info.addDirection(secondDir);
			info.addDirection(thirdDir);
			collisionShapes.addFirst(shapes);
			
			
			return;
		}
		if(directions.size() == 4) {
			Shape forest = new Rectangle(border, border, size -2*border, size -2*border);
			g.draw(forest);

			var info = new ResourceInformation(Type.FOREST);
			var shapes = new ResourceShape(info);
			shapes.addShape(forest);
			info.addDirection(Direction.NORTH);
			info.addDirection(Direction.EAST);
			info.addDirection(Direction.SOUTH);
			info.addDirection(Direction.WEST);
			collisionShapes.addFirst(shapes);
			
			return;
		}
	}
	
	public ResourceInformation getResourceAt(Point pos) {
		for(var resource : collisionShapes) {
			if(resource.contains(pos)) {
				return resource.getSecond();
			}
		}
		return null;
	}
	
	public void paint(Graphics2D g, Point coord, double scale) {
		AffineTransform transform = new AffineTransform();
		transform.translate(coord.x, coord.y);
		transform.scale(scale, scale);
		g.drawImage(displayImage, transform, null);
	}
	
	public void paint(Graphics2D g, Position pos, Point offset, double scale) {
		Point coord = TileGraphic.PosToCoord(pos, scale); 
		coord.translate(offset.x, offset.y);
		paint(g, coord, scale);
	}
}
