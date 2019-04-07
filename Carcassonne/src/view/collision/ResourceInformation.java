package view.collision;
import java.util.Collection;
import java.util.HashSet;

import logic.Direction;
import logic.Type;
import util.Tuple;

public class ResourceInformation extends Tuple<Type, Collection<Direction>>{
	public ResourceInformation(Type a) {
		super(a, new HashSet<Direction>());
	}
	
	public boolean addDirection(Direction dir) {
		return this.getSecond().add(dir);
	}
}
