package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Map;
import java.util.Set;

import logic.Position;

public class GameBoardCanvas extends DoubleBufferedCanvas {
	private static final long serialVersionUID = -9035087671066013403L;

	private Map<Position, TileGraphic> gameBoardReference;
	private Set<Position> highlightReference;
	private TileGraphic mouseTile;
	private Object mouseTileLock;
	
	private int centerX, centerY;
	private double scale;
	
	private final static int highlightWidth = 10;
	
	
	public GameBoardCanvas(Map<Position, TileGraphic> gameBoard, Set<Position> highlights) {
		this.gameBoardReference = gameBoard;
		this.highlightReference = highlights;
		
		this.scale = 1;
		
		mouseTileLock = new Object();
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
	
	public void moveCenter(int dx, int dy) {
		centerX += dx;
		centerY += dy;
		
		this.repaint();
	}
	public void changeScale(double dz) {
		this.scale = Math.max(0.1, Math.min(2., this.scale*(1.+dz)));
		this.repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2 = (Graphics2D) g;
		
		Dimension dim = this.getSize();
		int offsetX = dim.width/2	-(int)(centerX*scale);
		int offsetY = dim.height/2	-(int)(centerY*scale);
		Point offset = new Point(offsetX, offsetY);
		
		synchronized(gameBoardReference) {
			for(Position pos : gameBoardReference.keySet()) {
				TileGraphic tile = gameBoardReference.get(pos);
				tile.paint(g2, pos, offset, scale);
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
					mouseTile.paint(g2, coord, scale);
				}
			}
		}
	}
}