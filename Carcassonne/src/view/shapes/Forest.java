package view.shapes;
import java.awt.Color;
import java.awt.Point;

import logic.Direction;
import view.TileGraphic;
import view.sprites.TreeSprite;

public class Forest extends BackgroundSpline {
	private static final long serialVersionUID = 2806951371081096281L;
	

	public Forest() {
		super(new Color(120,80,20));
		super.generateAllDirection();
		placeTrees(40);
	}
	public Forest(Direction dir) {
		super(new Color(120,80,20));
		super.generateSingleDirection(dir);
		placeTrees(6);
	}
	
	public Forest(Direction clockwiseStart, Direction clockwiseEnd) {
		super(new Color(120,80,20));
		super.generateMultiDirection(clockwiseStart, clockwiseEnd);
		
		if(clockwiseStart.rotateClockwise() == clockwiseEnd)
			placeTrees(12);
		else
			placeTrees(24);
	}
	
	private void placeTrees(int number) {
		//number *= 100;
		while(number > 0) {

			var pos = new Point(0,0);
			pos.translate((int)(Math.random()*TileGraphic.size), (int)(Math.random()*TileGraphic.size));
			
			if(this.contains(pos))
			{
				pos.translate(TileGraphic.foregroundBorder, TileGraphic.foregroundBorder);
				var tree = new TreeSprite(pos);
				sprites.add(tree);
				number--;
			}
		}
		
		orderSprites();
	}
}
