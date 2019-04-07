package view;
import logic.*;
import model.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

public class UI {
	
	private class GameBoardCanvas extends DoubleBufferedCanvas {
		private static final long serialVersionUID = -9035087671066013403L;

		private Map<Position, TileGraphic> gameBoardReference;
		private Set<Position> highlightReference;
		
		private int centerX, centerY;
		
		private final static int highlightWidth = 10;
		
		
		public GameBoardCanvas(Map<Position, TileGraphic> gameBoard, Set<Position> highlights) {
			this.gameBoardReference = gameBoard;
			this.highlightReference = highlights;
		}
		
		public void moveCenter(int x, int y) {
			centerX += x;
			centerY += y;
			
			this.repaint();
		}
		
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			
			Graphics2D g2 = (Graphics2D) g;
			
			Dimension dim = this.getSize();
			//g.clearRect(0, 0, dim.width, dim.height);
			
			int offsetX = dim.width/2	-centerX;
			int offsetY = dim.height/2	-centerY;
			
			synchronized(gameBoardReference) {
				for(Position pos : gameBoardReference.keySet()) {
					TileGraphic tile = gameBoardReference.get(pos);
					tile.paint(g2, pos, offsetX, offsetY);
				}
			}
			
			synchronized(highlightReference) {
				for(Position pos :  highlightReference) {
					int size = TileGraphic.size;
					int xCoord = pos.getX()*size -size/2;
					int yCoord = -pos.getY()*size -size/2;
					
					int x = xCoord+offsetX;
					int y = yCoord+offsetY;
					
					
					g2.setColor(Color.RED);
					g2.setStroke(new BasicStroke(highlightWidth));
					int halfsize =  highlightWidth/2;
					g2.drawRect(x+halfsize, y+halfsize, size-highlightWidth, size-highlightWidth);
				}
			}
		}
	}
	
	
	private JFrame frame;
	private GameBoardCanvas canvas;
	private Map<Position, TileGraphic> gameBoard;
	private Set<Position> highlights;
	
	public UI() {
		gameBoard = new HashMap<Position, TileGraphic>();
		highlights = new HashSet<Position>();
		
		frame = new JFrame();
		frame.setSize(1000, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		MouseMotionListener mouseMotion = new MouseMotionAdapter() {
			private int lastX;
			private int lastY;
			@Override
			public void mouseDragged(MouseEvent e) {
				int dx = e.getX()-lastX;
				int dy = e.getY()-lastY;
				
				canvas.moveCenter(-dx, -dy);
				
				mouseMoved(e);
	        }
			@Override
			public void mouseMoved(MouseEvent e) {
				lastX = e.getX();
				lastY = e.getY();
	        }
		};
		
		
		GridLayout layout = new GridLayout(1,1); 
		frame.setLayout(layout);
		canvas = new GameBoardCanvas(gameBoard, highlights);
		canvas.addMouseMotionListener(mouseMotion);
		
		frame.add(canvas);
		frame.setVisible(true);
	}
	
	/***
	 * Draws a tile at the given position. If there is already a tile present, it will be overridden.
	 * @param pos logical position to draw the tile
	 * @param tile model information of the tile
	 */
	public void draw(Position pos, model.Tile tile) {
		synchronized(gameBoard) {
			gameBoard.put(pos, new TileGraphic(tile));
		}
		canvas.repaint();
	}
	
	/**
	 * does absolutely not nothing
	 * @param pos
	 * @param isActive
	 */
	public void highlight(Position pos, boolean isActive) {
		synchronized(highlights) {
			if(isActive) {
				if(highlights.add(pos))
					canvas.repaint(); //TODO: only relevant area
			}
			else {
				if(highlights.remove(pos))
					canvas.repaint();
			}
		}
	}
	
	public static void main(String[] args) {
		UI ui = new UI();
		ui.draw(new Position(0, 0), TileFactory.getStartTile());
		for(int x = -1; x < 2; ++x) 
			for(int y = 1; y < 4; ++y) {
				Tile randTile = TileFactory.getRandomTile();
				ui.draw(new Position(x,y), randTile);
			}

		ui.highlight(new Position(4,4), true);
		ui.highlight(new Position(4,5), true);
		ui.highlight(new Position(4,4), false);
	}
}
