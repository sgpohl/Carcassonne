package view;
import logic.*;
import model.GameField;
import model.Tile;

import javax.swing.*;

import controller.Controller;

import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class UI {
	private JFrame frame;
	private GameBoardCanvas canvas;
	private final Map<Position, TileGraphic> gameBoard;
	private final Set<Position> highlights;
	private Collection<Controller> controller;
	
	public UI() {
		controller = new ArrayList<Controller>();
		
		gameBoard = new HashMap<>();
		highlights = new HashSet<>();
		
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
						for(var c : controller)
							c.UI_clickedOnTile(pos.getFirst());
					}
				}
				if(e.getButton() == MouseEvent.BUTTON3) {
					for(var c : controller)
						c.UI_rotateCurrentTile();
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
				
				canvas.moveCenter((int)(-dx/canvas.getScale()), (int)(-dy/canvas.getScale()));
				
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
		canvas.addMouseWheelListener(e -> {
			double zoomSpeed = 0.15;
			canvas.changeScale(zoomSpeed*e.getPreciseWheelRotation());
		});
		
		frame.add(canvas);
		frame.setVisible(true);
	}
	
	public void addController(Controller c) {
		controller.add(c);
	}
	
	public boolean removeController(Controller c) {
		return controller.remove(c);
	}
	
	/***
	 * Draws a tile at the given position. If there is already a tile present, it will be overridden.
	 * @param pos logical position to draw the tile
	 * @param tile model information of the tile
	 */
	public void draw(Position pos, model.Tile tile) {
		synchronized(gameBoard) {
			gameBoard.put(pos, new TileGraphic(tile));
			canvas.recalculateZOrder();
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
	 * Removes all highlighting borders
	 */
	public void clearHighlights(){
		synchronized (highlights) {
			highlights.clear();
			canvas.repaint();
		}
	}
	
	/***
	 * Highlights the specified resource at position. Removes all other highlights on resources.
	 * @param pos position of the target tile
	 * @param resource resource information relative to the target tile
	 */
	public void highlightResource(Position pos, ResourceInformation resource) {
		synchronized(gameBoard) {
			var tile = gameBoard.get(pos);
			if(tile != null) {
				tile.clearResourceHighlights();
				tile.setResourceHighlight(resource);
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
	
	/***
	 * Returns resource under the cursor
	 * @return ResourceInformation corresponding to the resource under the cursor. Null, if none found.
	 */
	public ResourceInformation getResourceSelection() {
		var pos = canvas.getPositionAtMouse();
		if(pos != null) {
			TileGraphic currentTile = gameBoard.get(pos.getFirst());
			if(currentTile != null) {
				return currentTile.getResourceAt(pos.getSecond());
			}
		}
		return null;
	}

	public void drawAll( GameField field) {
		for (var pos : field.getAllTiles().keySet())
			draw(pos, field.getAllTiles().get(pos));
	}

	/***
	 * removes all tiles from the UI
	 */
	public void clearField(){
		synchronized (gameBoard) {
			gameBoard.clear();
			canvas.recalculateZOrder();
			canvas.repaint();
		}
	}
	

	
	public static void main(String[] args) {
		UI ui = new UI();
		//ui.addController(new Controller());
		
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
		
		//ui.clearField();
	}

}
