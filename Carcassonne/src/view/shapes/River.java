package view.shapes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import logic.Direction;
import logic.ResourceInformation;
import logic.Type;
import view.TileGraphic;

public class River implements TileShape {	
	private Collection<RiverSegment> segments;
	private ResourceInformation info;
	private Lake lake;
	
	private int rnd() { 
		return (int)((Math.random()-.5)*20);
	}
	
	public River(Collection<Direction> directions) {
		info = new ResourceInformation(Type.RIVER);
		segments = new HashSet<RiverSegment>();
		
		//two directions -> bezier ...
		if(directions.size() == 2) {
			var directionList = new ArrayList<Direction>(directions);
			Point from = TileGraphic.directionToCoordinate(directionList.get(0));
			Point to = TileGraphic.directionToCoordinate(directionList.get(1));
			
			boolean split = (Math.random()<0.25);
			var riverSegment = new RiverSegment(from, to, new Point(TileGraphic.size/2, TileGraphic.size/2), split);
			
			riverSegment.addDirectionInfo(directionList.get(0));
			riverSegment.addDirectionInfo(directionList.get(1));
			this.add(riverSegment);
		}
		//otherwise straight lines connecting in the middle
		else {
			//Point to = new Point(size/2+border, size/2+border);
			Point to = new Point(0,0);
			double weighting = 0;
			if(directions.size() == 3) {
				for(Direction dir : directions) {
					var d = TileGraphic.directionToCoordinate(dir);
					double w = 1;//Math.random()+0.1;
					to.translate((int)(d.x*w), (int)(d.y*w));
					weighting += w;
				}
				to.x /= weighting;
				to.y /= weighting;
			}
			else
				to.translate(TileGraphic.size/2, TileGraphic.size/2); 
			to.translate(rnd(), rnd()); 
			
			
			for(Direction dir : directions) {
				Point from = TileGraphic.directionToCoordinate(dir);
				
				var riverSegment = new RiverSegment(from, to);
				riverSegment.addDirectionInfo(dir);
				this.add(riverSegment);
			}
			
			//if(Math.random() < 0.2)
				lake = new Lake(to);
		}

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
		if(lake != null)
			return lake.contains(p);
		return false;
	}

	@Override
	public void bakeInto(Graphics2D g) {
		if(lake != null)
			lake.bakeInto(g);
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

	@Override
	public void highlight(boolean active) {
		for(var s : segments)
			s.highlight(active);
		if(lake != null)
			lake.highlight(active);
	}
}
