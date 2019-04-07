package view.shapes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Collection;
import java.util.HashSet;

import logic.Type;
import view.ResourceInformation;

public class River implements TileShape {
	private Collection<RiverSegment> segments;
	private ResourceInformation info;
	
	public River() {
		info = new ResourceInformation(Type.RIVER);
		segments = new HashSet<RiverSegment>();
	}
	
	public River(Collection<RiverSegment> segments) {
		info = new ResourceInformation(Type.RIVER);
		for(var s : segments)
			for(var d : s.getInformation().getSecond())
				info.addDirection(d);
		
		this.segments = segments;
	}
	
	public void add(RiverSegment s) {
		for(var d : s.getInformation().getSecond())
			info.addDirection(d);
		
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

	@Override
	public ResourceInformation getInformation() {
		return info;
	}

	@Override
	public void bakeIntoForeground(Graphics2D g) {
	}

}
