package view;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

import logic.Direction;
import logic.Type;
import util.Tuple;

public class ResourceInformation extends Tuple<Type, Collection<Direction>>{
	public static boolean matches(ResourceInformation superset, ResourceInformation subset) {
		if(superset == null)
			return false;
		if(subset == null)
			return false;
		return (superset.getFirst() == subset.getFirst()) 
			&& (superset.getSecond().containsAll(subset.getSecond()));
	}
	
	
	public ResourceInformation(Type a) {
		super(a, new HashSet<Direction>());
	}
	
	public boolean addDirection(Direction dir) {
		return this.getSecond().add(dir);
	}
}
