package view;
import logic.*;
import model.Tile;

import javax.swing.*;

import controller.Controller;

import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

public class UI {
	private JFrame frame;
	private GameBoardCanvas canvas;
	private Map<Position, TileGraphic> gameBoard;
	private Set<Position> highlights;
	private Controller controller;
	
	public UI(Controller controller) {
		this.controller = controller;
		
		gameBoard = new HashMap<Position, TileGraphic>();
		highlights = new HashSet<Position>();
		
		frame = new JFrame();
		frame.setSize(1000, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		GridLayout layout = new GridLayout(1,1); 
		frame.setLayout(layout);
		canvas = new GameBoardCanvas(gameBoard, highlights);
		canvas.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1) {
					var pos = canvas.getPositionAtMouse();
					if(pos != null) {
						controller.UI_clickedOnTile(pos.getFirst());
						
						TileGraphic currentTile = gameBoard.get(pos.getFirst());
						if(currentTile != null) {
							var info = currentTile.getResourceAt(pos.getSecond());
							if(info != null)
								System.out.println("Resource: "+info.getFirst());
						}
							
					}
				}
				if(e.getButton() == MouseEvent.BUTTON3) {
					controller.UI_rotateCurrentTile();
				}
			}
			@Override
			public void mousePressed(MouseEvent e) {
			}
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}
		});
		canvas.addMouseMotionListener(new MouseMotionAdapter() {
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
				if(canvas.hasMouseTile())
					canvas.repaint();
				lastX = e.getX();
				lastY = e.getY();
	        }
		});
		canvas.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				double zoomSpeed = 0.15;
				canvas.changeScale(zoomSpeed*e.getPreciseWheelRotation());
			}
		});
		
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
	 * Sets highlighting border at the given position
	 * @param pos position of the highlighting border
	 * @param isActive enables/disables highlighting
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
	
	/***
	 * Displays a card at the mouse cursor
	 * @param tile Tile to be drawn at the mouse cursor. Pass null to remove.
	 */
	public void setDrawnCard(Tile tile) {
		canvas.setMouseTile(new TileGraphic(tile));
	}
	
	public static void main(String[] args) {
		Controller controller = new Controller();
		UI ui = new UI(controller);
		ui.draw(new Position(0, 0), TileFactory.getStartTile());
		for(int x = -4; x < 4; ++x) 
			for(int y = 1; y < 6; ++y) {
				Tile randTile = TileFactory.getRandomTile();
				ui.draw(new Position(x,y), randTile);
			}

		ui.highlight(new Position(2,2), true);
		ui.highlight(new Position(2,3), true);
		ui.highlight(new Position(2,2), false);
		
		//ui.setDrawnCard(TileFactory.getRandomTile());
	}
}
