package view;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import util.*;
import view.collision.*;
import view.shapes.*;
import logic.*;

public class TileGraphic {
	public final static int size = 100;
	public final static int border = 0;
	
	public final static int streetWidth = 10;
	public final static int villageSize = 20;
	
	private Image displayImage;
	
	private Deque<TileShape> collisionShapes;
	
	public static Point PosToCoord(Position pos, double scale) {
		int xCoord = (int)(pos.getX()*size*scale	-size/2);
		int yCoord = (int)(-pos.getY()*size*scale	-size/2);
		return new Point(xCoord, yCoord);
	}
	
	public static Point directionToCoordinate(Direction dir) {
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
	
	public static Tuple<Point, Point> directionToBorder(Direction dir) {
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
		
		collisionShapes = new LinkedList<TileShape>();
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
		
		var grass = new Grass(grassDirections);
		collisionShapes.addFirst(grass);
		
		
		drawForests(forestDirections);
		drawAllStreets(streetDirections);
		Graphics2D g = (Graphics2D)displayImage.getGraphics();
		
		var it = collisionShapes.descendingIterator();
		while(it.hasNext())
			it.next().bakeInto(g);
	}

	
	private void insertVillage() { //TODO
		Graphics2D g = (Graphics2D)displayImage.getGraphics();
		g.setColor(Color.CYAN);
		g.fillRect((size-villageSize)/2, (size-villageSize)/2, villageSize, villageSize);
	}
	
	private RiverSegment drawStreet(Point from, Point to) {
		return new RiverSegment(from, to);
	}
	
	private void drawAllStreets(List<Direction> directions) {
		if(directions.isEmpty())
			return;
		
		var river = new River();
		
		//two directions -> bezier ...
		if(directions.size() == 2) {
			
			Point from = directionToCoordinate(directions.get(0));
			Point to = directionToCoordinate(directions.get(1));
			var street = drawStreet(from, to);
			street.addDirectionInfo(directions.get(0));
			street.addDirectionInfo(directions.get(1));
			
			river.add(street);
		}
		//otherwise straight lines connecting in the middle
		else {
			Point to = new Point(size/2, size/2);
			for(Direction dir : directions) {
				
				Point from = directionToCoordinate(dir);
				var street = drawStreet(from, to);
				street.addDirectionInfo(dir);
				
				river.add(street);
			}
			
			insertVillage();
		}

		collisionShapes.addFirst(river);
	}
	
	private TileShape drawSingleForest(Direction dir) {
		return new Forest(dir);
	}
	
	private TileShape drawMultiForest(Direction clockwiseStart, Direction clockwiseEnd) {
		return new Forest(clockwiseStart, clockwiseEnd);
	}
	
	private void drawForests(List<Direction> directions) {
		
		if(directions.size() == 1) {
			
			var forest = drawSingleForest(directions.get(0));
			collisionShapes.addFirst(forest);
			
			return;
		}
		if(directions.size() == 2) {
			if(directions.get(0).equals(directions.get(1).getOpposite())) {
				var forest = drawSingleForest(directions.get(0));
				collisionShapes.addFirst(forest);
				
				forest = drawSingleForest(directions.get(1));
				collisionShapes.addFirst(forest);
				return;
			}
			else {
				Direction firstDir = directions.get(0);
				Direction secondDir = directions.get(1);
				if(firstDir.rotateClockwise() != directions.get(1)) {
					firstDir = directions.get(1);
					secondDir = directions.get(0);
				}
				var forest = drawMultiForest(firstDir, secondDir);
				
				collisionShapes.addFirst(forest);
				
				return;
			}
		}
		if(directions.size() == 3) {
			Direction firstDir = Direction.NORTH;
			while(directions.contains(firstDir))
				firstDir = firstDir.rotateClockwise();
			firstDir = firstDir.rotateClockwise();
			Direction thirdDir = firstDir.getOpposite();
			
			var forest = drawMultiForest(firstDir, thirdDir);
			
			collisionShapes.addFirst(forest);
			
			
			return;
		}
		if(directions.size() == 4) {
			var forest = new Forest();
			collisionShapes.addFirst(forest);
			
			return;
		}
	}
	
	public ResourceInformation getResourceAt(Point pos) {
		for(var resource : collisionShapes) {
			if(resource.contains(pos)) {
				return resource.getInformation();
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
