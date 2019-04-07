package view;
import logic.*;
import model.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.Map;
import java.util.HashMap;

public class UI {
	
	private class GameBoardCanvas extends Canvas {
		private static final long serialVersionUID = -9035087671066013403L;

		private Map<Position, TileGraphic> gameBoardReference;
		private int centerX, centerY;
		
		
		public GameBoardCanvas(Map<Position, TileGraphic> gameBoard) {
			this.gameBoardReference = gameBoard;
		}
		
		public void moveCenter(int x, int y) {
			centerX += x;
			centerY += y;
			
			this.repaint();
		}
		
		@Override
		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			
			Dimension dim = this.getSize();
			
			int offsetX = dim.width/2	-centerX;
			int offsetY = dim.height/2	-centerY;
			
			for(Position pos : gameBoardReference.keySet()) {
				TileGraphic tile = gameBoardReference.get(pos);
				tile.paint(g2, pos, offsetX, offsetY);
			}
		}
	}
	
	
	private JFrame frame;
	private GameBoardCanvas canvas;
	private Map<Position, TileGraphic> gameBoard;
	
	private Position center;
	
	public UI() {
		gameBoard = new HashMap<Position, TileGraphic>();
		
		frame = new JFrame();
		frame.setSize(1000, 700);
		
		
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
		
		
		frame.setLayout(new GridLayout(1,1));
		canvas = new GameBoardCanvas(gameBoard);
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
		gameBoard.put(pos, new TileGraphic(tile));
	}
	
	/**
	 * does absolutely nothing
	 * @param pos
	 * @param isEmpty
	 */
	public void highlight(Position pos, boolean isEmpty) {
		
	}
	
	public static void main(String[] args) {
		UI ui = new UI();
		ui.draw(new Position(0, 0), TileFactory.getStartTile());
		for(int x = -1; x < 2; ++x) 
			for(int y = 1; y < 4; ++y) {
				Tile randTile = TileFactory.getRandomTile();
				System.out.println(""+x+" "+y+" -> "+randTile.toString());
				ui.draw(new Position(x,y), randTile);
			}
	}
}
