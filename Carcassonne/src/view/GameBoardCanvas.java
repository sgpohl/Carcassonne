package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import logic.Position;
import util.Tuple;

public class GameBoardCanvas extends DoubleBufferedCanvas {
	private static final long serialVersionUID = -9035087671066013403L;

	private Map<Position, TileGraphic> gameBoardReference;
	private Set<Position> highlightReference;
	private TileGraphic mouseTile;
	private Object mouseTileLock;
	
	private Point center;
	private double scale;
	
	private List<Position> zOrderedKeys;
	
	private final static int highlightWidth = 10;
	
	
	public GameBoardCanvas(Map<Position, TileGraphic> gameBoard, Set<Position> highlights) {
		this.gameBoardReference = gameBoard;
		this.highlightReference = highlights;
		
		this.scale = 1;
		this.center = new Point(0,0);
		
		mouseTileLock = new Object();
		
		zOrderedKeys = new ArrayList<Position>();
	}

	public void setMouseTile(TileGraphic tile) {
		synchronized(mouseTileLock) {
			mouseTile = tile;
		}
		this.repaint();
	}
	public boolean hasMouseTile() {
		return mouseTile != null;
	}
	
	public Tuple<Position, Point> getPositionAtMouse() {
		Point mousePos = getMousePosition();
		if(mousePos == null)
			return null;

		Dimension dim = this.getSize();
		int offsetX = dim.width/2	-(int)(center.x*scale);
		int offsetY = dim.height/2	-(int)(center.y*scale);
		mousePos.translate(-offsetX, -offsetY);
		mousePos.translate(TileGraphic.size/2, TileGraphic.size/2);
		
		mousePos.x /= scale;
		mousePos.y /= scale;
		
		int px = Math.floorDiv(mousePos.x, TileGraphic.size);
		int py = -Math.floorDiv(mousePos.y, TileGraphic.size);
		
		int dx = Math.floorMod(mousePos.x, TileGraphic.size);
		int dy = Math.floorMod(mousePos.y, TileGraphic.size);
		
		return new Tuple<Position, Point>(new Position(px, py), new Point(dx,dy));
	}
	
	public void moveCenter(int dx, int dy) {
		center.translate(dx, dy);
		this.repaint();
	}
	public void changeScale(double dz) {
		this.scale = Math.max(0.1, Math.min(2., this.scale*(1.+dz)));
		this.repaint();
	}
	public double getScale() {
		return this.scale;
	}
	
	public void recalculateZOrder() {
		zOrderedKeys = new ArrayList<Position>(gameBoardReference.keySet());
		Collections.sort(zOrderedKeys, Comparator.comparing(key -> -key.getY()));
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2 = (Graphics2D) g;
		
		Dimension dim = this.getSize();
		int offsetX = dim.width/2	-(int)(center.x*scale);
		int offsetY = dim.height/2	-(int)(center.y*scale);
		Point offset = new Point(offsetX, offsetY);
		
		synchronized(gameBoardReference) {
			for(Position pos : zOrderedKeys) {
				TileGraphic tile = gameBoardReference.get(pos);
				tile.paintBackground(g2, pos, offset, scale);
			}
			
			for(Position pos : zOrderedKeys) {
				TileGraphic tile = gameBoardReference.get(pos);
				tile.paintForeground(g2, pos, offset, scale);
			}
		}
		
		synchronized(highlightReference) {
			for(Position pos :  highlightReference) {
				Point coord = TileGraphic.PosToCoord(pos, scale);
				coord.translate(offset.x, offset.y);
				int size = (int)(TileGraphic.size*scale);
				
				g2.setColor(Color.RED);
				g2.setStroke(new BasicStroke(highlightWidth));
				int halfsize = highlightWidth/2;
				g2.drawRect(coord.x+halfsize, coord.y+halfsize, size-highlightWidth, size-highlightWidth);
			}
		}
		
		if(hasMouseTile()) {
			synchronized (mouseTileLock) {
				Point coord = getMousePosition();
				if(coord != null)
				{
					int size = TileGraphic.size;
					coord.translate(-size/2, -size/2);
					
					int _border = 10;
					g2.setColor(Color.DARK_GRAY);
					g2.fillRect(coord.x-_border, coord.y-_border, size+2*_border, size+2*_border);
					
					mouseTile.paintBackground(g2, coord, scale);
				}
			}
		}
	}
}