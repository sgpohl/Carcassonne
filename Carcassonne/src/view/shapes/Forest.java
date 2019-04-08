package view.shapes;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;

import logic.Direction;
import view.sprites.*;

public class Forest extends BackgroundSpline {
	private static final long serialVersionUID = 2806951371081096281L;
	private static final Color forestColor = new Color(120,100,50);
	private SpriteRenderer sprites;
	
	private void init() {
		sprites = new SpriteRenderer();
	}
	
	public Forest(Collection<Direction> directions) {
		super(forestColor);
		
		var directionList = new ArrayList<Direction>(directions);
		switch(directions.size()) {
		case 1:		
			super.generateSingleDirection(directionList.get(0));
			break;
		case 2:
		case 3:
			Direction begin = null, end = null;
			for(var dir : directions) {
				if(!directions.contains(dir.rotateClockwise()))
					end = dir;
				if(!directions.contains(dir.rotateCounterclockwise()))
					begin = dir;
			}
			super.generateMultiDirection(begin, end);
			break;
		case 4:		
			super.generateAllDirection();		
			break;
		}
		init();

		//2^#dirs * 3 = 6,12,24,48
		int numTrees = (1<<directions.size())*3;
		placeTrees( numTrees );
	}
	
	private void placeTrees(int number) {
		for(int i = 0; i<number; ++i)
			sprites.placeSpriteOnRandomPosition(TreeSprite::new, this::contains);
			//sprites.placeSpriteOnRandomPosition(GrassSprite::new, this::contains);
	}
	
	@Override
	public void bakeIntoForeground(Graphics2D g) {
		sprites.bakeInto(g);
	}
}
