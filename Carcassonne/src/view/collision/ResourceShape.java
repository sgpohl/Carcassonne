package view.collision;

import java.awt.Point;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.Collection;

import util.Tuple;
import view.shapes.TileShape;

public class ResourceShape extends Tuple<Collection<TileShape>, ResourceInformation> {
	
	public ResourceShape(ResourceInformation b) {
		super(new ArrayList<TileShape>(), b);
	}
	
	public boolean addShape(TileShape s) {
		return this.getFirst().add(s);
	}
	
	public boolean contains(Point p) {
		for(TileShape shape : getFirst()) {
			if(shape.contains(p))
				return true;
		}
		return false;
	}

}
