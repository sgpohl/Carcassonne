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
import view.shapes.*;
import logic.*;

public class TileGraphic {
	public final static int size = 100;
	public final static int border = 0;
	
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
		
		collisionShapes = new LinkedList<TileShape>();
		
		drawGrass(grassDirections);
		drawForests(forestDirections);
		drawRivers(streetDirections);

		displayImage = new BufferedImage(size, size,  BufferedImage.TYPE_INT_ARGB);
		bakeImage();
	}

	
	private void drawGrass(List<Direction> directions) {
		var grass = new Grass(directions);
		collisionShapes.addFirst(grass);
	}
	private void drawLake() { 
		collisionShapes.addFirst(new Lake());
	}
	private void drawRivers(List<Direction> directions) {
		if(directions.isEmpty())
			return;
		
		var river = new River();
		collisionShapes.addFirst(river);
		
		//two directions -> bezier ...
		if(directions.size() == 2) {
			Point from = directionToCoordinate(directions.get(0));
			Point to = directionToCoordinate(directions.get(1));
			
			var riverSegment = new RiverSegment(from, to);
			riverSegment.addDirectionInfo(directions.get(0));
			riverSegment.addDirectionInfo(directions.get(1));
			river.add(riverSegment);
		}
		//otherwise straight lines connecting in the middle
		else {
			Point to = new Point(size/2, size/2);
			for(Direction dir : directions) {
				Point from = directionToCoordinate(dir);
				
				var riverSegment = new RiverSegment(from, to);
				riverSegment.addDirectionInfo(dir);
				river.add(riverSegment);
			}
			
			drawLake();
		}

	}
	private void drawForests(List<Direction> directions) {
		
		if(directions.size() == 1) {
			collisionShapes.addFirst(new Forest(directions.get(0)));
			return;
		}
		if(directions.size() == 2) {
			if(directions.get(0).equals(directions.get(1).getOpposite())) {
				collisionShapes.addFirst(new Forest(directions.get(0)));
				collisionShapes.addFirst(new Forest(directions.get(1)));
				return;
			}
			else {
				Direction firstDir = directions.get(0);
				Direction secondDir = directions.get(1);
				if(firstDir.rotateClockwise() != directions.get(1)) {
					firstDir = directions.get(1);
					secondDir = directions.get(0);
				}
				
				collisionShapes.addFirst(new Forest(firstDir, secondDir));
				return;
			}
		}
		if(directions.size() == 3) {
			Direction firstDir = Direction.NORTH;
			while(directions.contains(firstDir))
				firstDir = firstDir.rotateClockwise();
			firstDir = firstDir.rotateClockwise();
			Direction thirdDir = firstDir.getOpposite();
			
			collisionShapes.addFirst(new Forest(firstDir, thirdDir));
			return;
		}
		if(directions.size() == 4) {
			collisionShapes.addFirst(new Forest());
			return;
		}
	}
	
	private void bakeImage() {
		Graphics2D g = (Graphics2D)displayImage.getGraphics();
		g.clearRect(0, 0, displayImage.getWidth(null), displayImage.getHeight(null));
			
		var it = collisionShapes.descendingIterator();
		while(it.hasNext())
			it.next().bakeInto(g);
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
