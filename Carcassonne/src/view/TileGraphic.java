package view;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import util.*;
import view.shapes.*;
import logic.*;

public class TileGraphic {
	public final static int size = 100;
	public final static int foregroundBorder = 50;
	public final static int foregroundSize = size+2*foregroundBorder;
	public final static int resourceHighlightWidth = 10;
	
	private Image backgroundImage;
	private Image foregroundImage;
	
	private Deque<TileShape> collisionShapes;
	private Collection<TileShape> highlightedShapes;
	
	public static Point PosToCoord(Position pos, double scale) {
		int xCoord = (int)(pos.getX()*size*scale	-size/2);
		int yCoord = (int)(-pos.getY()*size*scale	-size/2);
		var p = new Point(xCoord, yCoord);
		return p;
	}	
	public static Point directionToCoordinate(Direction dir) {
		Point result = new Point(0,0);//(border, border);
		switch(dir) {
		case NORTH:
			result.translate(size/2, 0);
			break;
		case EAST:
			result.translate(size, size/2);
			break;
		case SOUTH:
			result.translate(size/2, size);
			break;
		case WEST:
			result.translate(0, size/2);
			break;
		default:
			result.translate(size/2, size/2);
			break;
		}
		return result;
	}
	public static Tuple<Point, Point> directionToBorder(Direction dir) {
		Point first = new Point(0,0);//(border, border);
		Point second = new Point(0,0);//(border, border);
		switch(dir) {
		case NORTH:
			first.translate(0,0);
			second.translate(size,0);
			return new Tuple<Point, Point>(first, second);
		case EAST:
			first.translate(size,0);
			second.translate(size,size);
			return new Tuple<Point, Point>(first, second);
		case SOUTH:
			first.translate(size,size);
			second.translate(0,size);
			return new Tuple<Point, Point>(first, second);
		case WEST:
			first.translate(0,size);
			second.translate(0,0);
			return new Tuple<Point, Point>(first, second);
		}
		return null;
	}
	

	public TileGraphic(model.Tile tile) {
		highlightedShapes = new LinkedList<TileShape>();
		
		var information = TileLogic.getExtendableOptions(tile);
		var reformattedInformation = new HashMap<Type, List<Direction>>();
		
		for(Type t : Type.values())
			reformattedInformation.put(t, new ArrayList<Direction>());
		for(Direction d : information.keySet()) {
			var types = information.get(d);
			if(types != null)
				for(Type t : types)
					reformattedInformation.get(t).add(d);
		}
		
		collisionShapes = new LinkedList<TileShape>();
		
		drawGrass(reformattedInformation.get(Type.GRASS));
		drawForests(reformattedInformation.get(Type.FOREST));
		drawRivers(reformattedInformation.get(Type.RIVER));

		backgroundImage = new BufferedImage(size, size,  BufferedImage.TYPE_INT_ARGB);
		foregroundImage = new BufferedImage(foregroundSize, foregroundSize,  BufferedImage.TYPE_INT_ARGB);
		
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
			//Point to = new Point(size/2+border, size/2+border);
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
		Graphics2D g = (Graphics2D)backgroundImage.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setBackground(new Color(0,0,0,0));
		g.clearRect(0, 0, backgroundImage.getWidth(null), backgroundImage.getHeight(null));
			
		var it = collisionShapes.descendingIterator();
		while(it.hasNext())
			it.next().bakeInto(g);
		

		g = (Graphics2D)foregroundImage.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setBackground(new Color(0,0,0,0));
		g.clearRect(0, 0, foregroundImage.getWidth(null), foregroundImage.getHeight(null));
			
		it = collisionShapes.descendingIterator();
		while(it.hasNext())
			it.next().bakeIntoForeground(g);
	}
	
	
	public ResourceInformation getResourceAt(Point pos) {
		//pos.translate(border, border);
		for(var resource : collisionShapes) {
			if(resource.contains(pos)) {
				return resource.getInformation();
			}
		}
		return null;
	}
	
	public void setResourceHighlight(ResourceInformation info) {
		if(info == null)
			return;
		List<TileShape> matches = 
							collisionShapes.stream()
							.filter(shape ->  ResourceInformation.matches(shape.getInformation(), info)
									).collect(Collectors.toList());
		for(var match : matches) {
			match.highlight(true);
			highlightedShapes.add(match);
		}
		
		bakeImage();
	}
	
	public void clearResourceHighlights() {
		for(var shape : highlightedShapes)
			shape.highlight(false);
		highlightedShapes.clear();
		
		bakeImage();
	}
	
	
	public void paintBackground(Graphics2D g, Point coord, double scale) {
		AffineTransform transform = new AffineTransform();
		transform.translate(coord.x, coord.y);
		//transform.translate(-border, -border);
		transform.scale(scale, scale);
		g.drawImage(backgroundImage, transform, null);
		
		transform.translate(-foregroundBorder, -foregroundBorder);
	}
	public void paintBackground(Graphics2D g, Position pos, Point offset, double scale) {
		Point coord = TileGraphic.PosToCoord(pos, scale); 
		coord.translate(offset.x, offset.y);
		paintBackground(g, coord, scale);
	}
	

	public void paintForeground(Graphics2D g, Point coord, double scale) {
		AffineTransform transform = new AffineTransform();
		transform.translate(coord.x, coord.y);
		transform.scale(scale, scale);
		transform.translate(-foregroundBorder, -foregroundBorder);
		g.drawImage(foregroundImage, transform, null);
		
	}
	public void paintForeground(Graphics2D g, Position pos, Point offset, double scale) {
		Point coord = TileGraphic.PosToCoord(pos, scale); 
		coord.translate(offset.x, offset.y);
		paintForeground(g, coord, scale);
	}
}
