package view.collision;

import java.awt.Point;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.Collection;

import util.Tuple;
import view.shapes.TileShape;

public class ResourceShape extends Tuple<Collection<TileShape>, ResourceInformation> {
	
	private int clickRadius = 1;

	public ResourceShape(ResourceInformation b) {
		super(new ArrayList<TileShape>(), b);
	}
	
	public boolean addShape(TileShape s) {
		return this.getFirst().add(s);
	}
	
	public void setIntersectionRadius(int radius) {
		clickRadius = radius;
	}
	
	public boolean contains(Point p) {
		for(TileShape shape : getFirst()) {
			if(shape.intersects(p.x-clickRadius, p.y-clickRadius, 2*clickRadius, 2*clickRadius))
				return true;
		}
		return false;
	}

}
