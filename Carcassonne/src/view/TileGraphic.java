package view;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.nio.charset.CoderMalfunctionError;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.Tile;
import util.*;

import logic.*;

public class TileGraphic {
	private final static int size = 100;
	private final static int border = 1;
	
	private final static int streetWidth = 10;
	private final static int villageSize = 20;
	
	private Image displayImage;
	
	
	
	private static Tuple<Integer, Integer> directionToCoordinate(Direction dir) {
		switch(dir) {
		case NORTH:
			return new Tuple<Integer, Integer>(size/2, border);
		case WEST:
			return new Tuple<Integer, Integer>(border, size/2);
		case SOUTH:
			return new Tuple<Integer, Integer>(size/2, size-border);
		case EAST:
			return new Tuple<Integer, Integer>(size-border, size/2);
		default:
			return new Tuple<Integer, Integer>(size/2, size/2);
		}
	}
	
	private void insertVillage() {
		Graphics2D g = (Graphics2D)displayImage.getGraphics();
		g.setColor(Color.CYAN);
		g.fillRect((size-villageSize)/2, (size-villageSize)/2, villageSize, villageSize);
	}
	
	private void drawStreet(Tuple<Integer, Integer> from, Tuple<Integer, Integer> to) {
		Graphics2D g = (Graphics2D)displayImage.getGraphics();
		g.setColor(Color.BLUE);
		g.setStroke(new BasicStroke(streetWidth));
		
		Path2D street = new Path2D.Float();
		street.moveTo(from.getFirst(), from.getSecond());
		street.curveTo(size/2, size/2, size/2, size/2, to.getFirst(), to.getSecond());
		g.draw(street);
	}
	
	private void drawAllStreets(List<Direction> directions) {
		Graphics2D g = (Graphics2D)displayImage.getGraphics();
		
		//two directions -> bezier ...
		if(directions.size() == 2) {
			Tuple<Integer, Integer> from = directionToCoordinate(directions.get(0));
			Tuple<Integer, Integer> to = directionToCoordinate(directions.get(1));
			drawStreet(from, to);
		}
		//otherwise straight lines connecting in the middle
		else {
			Tuple<Integer, Integer> to = new Tuple<Integer, Integer>(size/2, size/2);
			for(Direction dir : directions) {
				Tuple<Integer, Integer> from = directionToCoordinate(dir);
				drawStreet(from, to);
			}
			
			insertVillage();
		}
	}
	
	private void drawForests(List<Direction> directions) {
		Graphics2D g = (Graphics2D)displayImage.getGraphics();
		g.setColor(new Color(120,80,20));

		
		for(Direction dir : directions) {
			Tuple<Integer, Integer> from = directionToCoordinate(dir);
			g.fillRect(from.getFirst()-20, from.getSecond()-20, 40, 40);
		}
		
		
		
/*		if(directions.size() == 2 && directions.get(0).equals(directions.get(1).getOpposite())) {
			//TODO shite
		}
		else {
			
		}*/
	}
	
	public TileGraphic(model.Tile tile) {
		displayImage = new BufferedImage(size, size,  BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)displayImage.getGraphics();
		g.setColor(Color.GREEN);
		g.fillRect(border, border, size -2*border, size -2*border);
		
		
		
		
		Map<Direction, List<Type>> information = TileLogic.getExtendableOptions(tile);
		
		List<Direction> streetDirections = new ArrayList<Direction>();
		List<Direction> forestDirections = new ArrayList<Direction>();
		for(Direction d : information.keySet()) {
			if(information.get(d).contains(Type.RIVER))
				streetDirections.add(d);
			if(information.get(d).contains(Type.FOREST))
				forestDirections.add(d);
				
		}
		drawAllStreets(streetDirections);
		drawForests(forestDirections);
		
/*		
		List<Direction> testDirection = new ArrayList<Direction>();
		testDirection.add(Direction.NORTH);
		testDirection.add(Direction.EAST);
		drawForests(testDirection);*/
		
	}

	public void paint(Graphics2D g, Position pos, int offsetX, int offsetY) {
		int xCoord = pos.getX()*size -size/2;
		int yCoord = pos.getY()*size -size/2;
		
		AffineTransform transform = new AffineTransform();
		transform.translate(xCoord+offsetX, yCoord+offsetY);
		g.drawImage(displayImage, transform, null);
	}
}
