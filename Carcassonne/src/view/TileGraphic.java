package view;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
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
		
		collisionShapes = new LinkedList<TileShape>();
		var resourceInformation = new LinkedList<>(TileLogic.getResources(tile));
		resourceInformation.sort(Comparator.comparing(res -> -resourceZOrder(res.getFirst())));
		
		for(var resource : resourceInformation) {
			collisionShapes.add(createResource(resource));
		}

		backgroundImage = new BufferedImage(size, size,  BufferedImage.TYPE_INT_ARGB);
		foregroundImage = new BufferedImage(foregroundSize, foregroundSize,  BufferedImage.TYPE_INT_ARGB);
		
		bakeImage();
	}
	
	private static int resourceZOrder(Type t) {
		switch(t) {
		case GRASS:		return 0;
		case FOREST:	return 1;
		case RIVER:		return 2;
		}
		throw new UnsupportedOperationException("Z-Ordering requested for unknown resource");
	}
	
	private TileShape createResource(ResourceInformation resource) {
		if(resource.getSecond().isEmpty())
			throw new RuntimeException("created empty ResourceInformation: "+resource.getFirst());
		switch(resource.getFirst()) {
		case GRASS:		return new Grass(resource.getSecond(), this);
		case FOREST:	return new Forest(resource.getSecond());
		case RIVER:		return new River(resource.getSecond());
		default:		throw new UnsupportedOperationException("Tried resource creation for unhandled resource type (TileGraphic::new)");
		}
	}
	
	private void bakeImage() {
		bakeImageForeground();
		bakeImageBackground();
	}
	private void bakeImageForeground() {
		Graphics2D g = (Graphics2D)foregroundImage.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setBackground(new Color(0,0,0,0));
		g.clearRect(0, 0, foregroundImage.getWidth(null), foregroundImage.getHeight(null));
			
		var it = collisionShapes.descendingIterator();
		while(it.hasNext())
			it.next().bakeIntoForeground(g);
	}
	private void bakeImageBackground() {
		Graphics2D g = (Graphics2D)backgroundImage.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setBackground(new Color(0,0,0,0));
		g.clearRect(0, 0, backgroundImage.getWidth(null), backgroundImage.getHeight(null));
			
		var it = collisionShapes.descendingIterator();
		while(it.hasNext())
			it.next().bakeInto(g);
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
		
		bakeImageBackground();
	}
	
	public void clearResourceHighlights() {
		for(var shape : highlightedShapes)
			shape.highlight(false);
		highlightedShapes.clear();
		
		bakeImageBackground();
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
