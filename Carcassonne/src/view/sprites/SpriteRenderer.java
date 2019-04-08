package view.sprites;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import view.TileGraphic;

public class SpriteRenderer {
	private List<Sprite> sprites;
	
	public SpriteRenderer() {
		sprites = new ArrayList<Sprite>();
	}
	
	public void bakeInto(Graphics2D g) {
		for(var sprite : sprites)
			sprite.bakeInto(g);
	}

	private void zOrderSprites() {
		Collections.sort(sprites, Comparator.comparing(sprite -> sprite.getPosition().y));
	}
	

	public void placeSpriteOnRandomPosition(Function<Point, Sprite> constructor, Function<Point, Boolean> collision) {
		while(true) {
			var pos = new Point(0,0);
			pos.translate((int)(Math.random()*TileGraphic.size), (int)(Math.random()*TileGraphic.size));
			
			if(collision.apply(pos))
			{
				pos.translate(TileGraphic.foregroundBorder, TileGraphic.foregroundBorder);
				var tree = constructor.apply(pos);
				//var tree = new TreeSprite(pos);
				sprites.add(tree);
				break;
			}
		}
		
		zOrderSprites();
	}
	
	public boolean isEmpty() {
		return sprites.isEmpty();
	}
}
