package logic;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

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
	

	public ResourceInformation(Type a, Collection<Direction> directions) {
		super(a, new HashSet<Direction>());
		this.getSecond().addAll(directions);
	}
	
	public boolean addDirection(Direction dir) {
		return this.getSecond().add(dir);
	}
}
