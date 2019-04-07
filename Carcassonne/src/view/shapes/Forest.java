package view.shapes;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Path2D;
import java.util.Collection;

import logic.Direction;
import logic.Type;
import util.Tuple;
import view.ResourceInformation;
import view.TileGraphic;
import view.sprites.TreeSprite;

public class Forest extends BackgroundSpline {
	private static final long serialVersionUID = 2806951371081096281L;
	

	public Forest() {
		super(new Color(120,80,20));
		super.generateAllDirection();
		placeTrees(10);
	}
	public Forest(Direction dir) {
		super(new Color(120,80,20));
		super.generateSingleDirection(dir);
		placeTrees(3);
	}
	
	public Forest(Direction clockwiseStart, Direction clockwiseEnd) {
		super(new Color(120,80,20));
		super.generateMultiDirection(clockwiseStart, clockwiseEnd);
		placeTrees(6);
	}
	
	private void placeTrees(int number) {
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
		
	}
}
