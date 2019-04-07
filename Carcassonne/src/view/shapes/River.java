package view.shapes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Collection;
import java.util.HashSet;

public class River implements TileShape {
	private Collection<RiverSegment> segments;
	
	public River() {
		segments = new HashSet<RiverSegment>();
	}
	
	public River(Collection<RiverSegment> segments) {
		this.segments = segments;
	}
	
	public void add(RiverSegment s) {
		segments.add(s);
	}
	
	@Override
	public boolean contains(Point p) {
		for(var s : segments)
			if(s.contains(p))
				return true;
		return false;
	}

	@Override
	public void bakeInto(Graphics2D g) {
		for(var s : segments)
			s.bakeInto(g);
	}

}
