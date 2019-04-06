package view;
import logic.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.Map;
import java.util.HashMap;

public class UI {
	
	private class GameBoardCanvas extends Canvas {
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
	
	
	JFrame frame;
	GameBoardCanvas canvas;
	private Map<Position, TileGraphic> gameBoard;
	
	Position center;
	
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
				//System.out.println(""+dx+"  "+dy);
				
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
	
	
	public void draw(Position pos, model.Tile tile) {
		gameBoard.put(pos, new TileGraphic(tile));
	}
	
	public void highlight(Position pos, boolean isEmpty) {
		
	}
	
	public static void main(String[] args) {
		UI ui = new UI();
		ui.draw(new Position(0,0), null);
		ui.draw(new Position(0,1), null);
		ui.draw(new Position(0,-1), null);
		ui.draw(new Position(1,0), null);
		ui.draw(new Position(-1,0), null);
	}
}