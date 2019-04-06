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
		case EAST:
			return new Tuple<Integer, Integer>(size-border, size/2);
		case SOUTH:
			return new Tuple<Integer, Integer>(size/2, size-border);
		case WEST:
			return new Tuple<Integer, Integer>(border, size/2);
		default:
			return new Tuple<Integer, Integer>(size/2, size/2);
		}
	}
	
	private static Tuple<Tuple<Integer, Integer>, Tuple<Integer, Integer>> directionToBorder(Direction dir) {
		Tuple<Integer, Integer> first;
		Tuple<Integer, Integer> second;
		switch(dir) {
		case NORTH:
			first = new Tuple<Integer, Integer>(0,0);
			second = new Tuple<Integer, Integer>(size,0);
			return new Tuple<Tuple<Integer, Integer>, Tuple<Integer, Integer>>(first, second);
		case EAST:
			first = new Tuple<Integer, Integer>(size,0);
			second = new Tuple<Integer, Integer>(size,size);
			return new Tuple<Tuple<Integer, Integer>, Tuple<Integer, Integer>>(first, second);
		case SOUTH:
			first = new Tuple<Integer, Integer>(size,size);
			second = new Tuple<Integer, Integer>(0,size);
			return new Tuple<Tuple<Integer, Integer>, Tuple<Integer, Integer>>(first, second);
		case WEST:
			first = new Tuple<Integer, Integer>(0,size);
			second = new Tuple<Integer, Integer>(0,0);
			return new Tuple<Tuple<Integer, Integer>, Tuple<Integer, Integer>>(first, second);
		}
		return null;
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
	
	private Direction clockwiseNext(Direction dir) {
		switch(dir) {
		case NORTH:
			return Direction.EAST;
		case EAST:
			return Direction.SOUTH;
		case SOUTH:
			return Direction.WEST;
		case WEST:
			return Direction.NORTH;
		}
		return Direction.EAST;
	}

	private void drawSingleForest(Graphics2D g, Direction dir) {
		
		Tuple<Tuple<Integer, Integer>, Tuple<Integer, Integer>> borders;
		borders = directionToBorder(dir);
		int x1 = borders.getFirst().getFirst();
		int y1 = borders.getFirst().getSecond();
		
		int x2 = borders.getSecond().getFirst();
		int y2 = borders.getSecond().getSecond();
		
		Path2D forest = new Path2D.Float();
		forest.moveTo(x1, y1);
		int middle = size/2;
		forest.curveTo((x1+middle)/2, (y1+middle)/2, (x2+middle)/2, (y2+middle)/2, x2, y2);
		forest.closePath();
		g.fill(forest);
	}
	
	private void drawMultiForest(Graphics2D g, Direction clockwiseStart, Direction clockwiseEnd) {

		Tuple<Tuple<Integer, Integer>, Tuple<Integer, Integer>> startBorders;
		startBorders = directionToBorder(clockwiseStart);
		Tuple<Tuple<Integer, Integer>, Tuple<Integer, Integer>> endBorders;
		endBorders = directionToBorder(clockwiseEnd);

		int x1 = startBorders.getFirst().getFirst();
		int y1 = startBorders.getFirst().getSecond();
		
		int p11 = startBorders.getSecond().getFirst();
		int p12 = startBorders.getSecond().getSecond();		
		
		int p21 = endBorders.getFirst().getFirst();
		int p22 = endBorders.getFirst().getSecond();
		
		int x2 = endBorders.getSecond().getFirst();
		int y2 = endBorders.getSecond().getSecond();
		
		Path2D forest = new Path2D.Float();
		forest.moveTo(x1, y1);
		int middle = size/2;
		
		forest.curveTo((p11+middle)/2, (p12+middle)/2, (p21+middle)/2, (p22+middle)/2, x2, y2);
		forest.lineTo(p21, p22);
		forest.lineTo(p11, p12);
		forest.closePath();
		g.fill(forest);
		
	}
	
	private void drawForests(List<Direction> directions) {
		Graphics2D g = (Graphics2D)displayImage.getGraphics();
		g.setColor(new Color(120,80,20));

	
/*		for(Direction dir : directions) {
			drawSingleForest(g, dir);
		}*/
	
		if(directions.size() == 1) {
			drawSingleForest(g, directions.get(0));
			return;
		}
		if(directions.size() == 2) {
			if(directions.get(0).equals(directions.get(1).getOpposite())) {
				drawSingleForest(g, directions.get(0));
				drawSingleForest(g, directions.get(1));
				return;
			}
			else {
				Direction firstDir = directions.get(0);
				Direction secondDir = directions.get(1);
				if(clockwiseNext(firstDir) != directions.get(1)) {
					firstDir = directions.get(1);
					secondDir = directions.get(0);
				}
				drawMultiForest(g, firstDir, secondDir);
			}
			return;
		}
		if(directions.size() == 3) {
			Direction firstDir = Direction.NORTH;
			while(directions.contains(firstDir))
				firstDir = clockwiseNext(firstDir);
			firstDir = clockwiseNext(firstDir);
			Direction thirdDir = clockwiseNext(clockwiseNext(firstDir));
			drawMultiForest(g, firstDir, thirdDir);
			
			return;
		}
		if(directions.size() == 4) {
			g.fillRect(border, border, size -2*border, size -2*border);
			return;
		}
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
		drawForests(forestDirections);
		drawAllStreets(streetDirections);
		
		/*
		List<Direction> testDirection = new ArrayList<Direction>();
		testDirection.add(Direction.NORTH);
		testDirection.add(Direction.WEST);
		testDirection.add(Direction.EAST);
		testDirection.add(Direction.SOUTH);
		drawForests(testDirection);
		*/
	}

	public void paint(Graphics2D g, Position pos, int offsetX, int offsetY) {
		int xCoord = pos.getX()*size -size/2;
		int yCoord = pos.getY()*size -size/2;
		
		AffineTransform transform = new AffineTransform();
		transform.translate(xCoord+offsetX, yCoord+offsetY);
		g.drawImage(displayImage, transform, null);
	}
}
