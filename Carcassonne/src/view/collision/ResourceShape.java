package view.collision;

import java.awt.Point;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.Collection;

import util.Tuple;

public class ResourceShape extends Tuple<Collection<Shape>, ResourceInformation> {
	
	private int clickRadius = 1;

	public ResourceShape(ResourceInformation b) {
		super(new ArrayList<Shape>(), b);
	}
	
	public boolean addShape(Shape s) {
		return this.getFirst().add(s);
	}
	
	public void setIntersectionRadius(int radius) {
		clickRadius = radius;
	}
	
	public boolean contains(Point p) {
		for(var shape : getFirst()) {
			if(shape.intersects(p.x-clickRadius, p.y-clickRadius, 2*clickRadius, 2*clickRadius))
				return true;
		}
		return false;
	}

}
