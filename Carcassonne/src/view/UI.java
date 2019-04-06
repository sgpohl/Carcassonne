package view;
import logic.*;

import javax.swing.*;
import java.awt.*;

import java.util.Map;
import java.util.HashMap;

public class UI {
	
	private class GameBoardCanvas extends Canvas {
		private Map<Position, TileGraphic> gameBoardReference;
		
		public GameBoardCanvas(Map<Position, TileGraphic> gameBoard) {
			gameBoardReference = gameBoard;
		}

		@Override
		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			
			for(Position pos : gameBoardReference.keySet()) {
				TileGraphic tile = gameBoardReference.get(pos);
				tile.paint(g2, pos);
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
		frame.setSize(500, 500);
		
		frame.setLayout(new GridLayout(1,1));
		canvas = new GameBoardCanvas(gameBoard);
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
	}
}
