package view.shapes;
import java.awt.Color;
import java.awt.Graphics2D;

import logic.Direction;
import view.sprites.SpriteRenderer;
import view.sprites.TreeSprite;

public class Forest extends BackgroundSpline {
	private static final long serialVersionUID = 2806951371081096281L;
	private SpriteRenderer sprites;
	
	private void init() {
		sprites = new SpriteRenderer();
	}
	
	public Forest() {
		super(new Color(120,80,20));
		super.generateAllDirection();
		init();

		placeTrees(40);
	}
	public Forest(Direction dir) {
		super(new Color(120,80,20));
		super.generateSingleDirection(dir);
		init();
		
		placeTrees(6);
	}
	
	public Forest(Direction clockwiseStart, Direction clockwiseEnd) {
		super(new Color(120,80,20));
		super.generateMultiDirection(clockwiseStart, clockwiseEnd);
		init();
		
		if(clockwiseStart.rotateClockwise() == clockwiseEnd)
			placeTrees(12);
		else
			placeTrees(24);
	}
	
	private void placeTrees(int number) {
		for(int i = 0; i<number; ++i)
			sprites.placeSpriteOnRandomPosition(TreeSprite::new, this::contains);
	}
	

	@Override
	public void bakeIntoForeground(Graphics2D g) {
		sprites.bakeInto(g);
	}
}
