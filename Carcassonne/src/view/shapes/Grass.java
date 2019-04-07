package view.shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import logic.Direction;
import view.TileGraphic;

public class Grass extends Path2D.Float implements TileShape {
	
	public Grass() {
		var border1 = TileGraphic.directionToBorder(Direction.NORTH);
		var border2 = TileGraphic.directionToBorder(Direction.SOUTH);
		
		this.moveTo(border1.getFirst().getX(), border1.getFirst().getY());
		this.lineTo(border1.getSecond().getX(), border1.getSecond().getY());
		this.lineTo(border2.getFirst().getX(), border2.getFirst().getY());
		this.lineTo(border2.getSecond().getX(), border2.getSecond().getY());
		this.closePath();
	}
	
	@Override
	public void bakeInto(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fill(this);
	}

}
