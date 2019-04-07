package view.collision;

import java.awt.Shape;
import java.util.ArrayList;
import java.util.Collection;

import util.Tuple;

public class ResourceShape extends Tuple<Collection<Shape>, ResourceInformation> {

	public ResourceShape(ResourceInformation b) {
		super(new ArrayList<Shape>(), b);
	}
	
	public boolean addShape(Shape s) {
		return this.getFirst().add(s);
	}

}
