package view.shapes;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Path2D;
import java.awt.geom.Path2D.Float;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import logic.Direction;
import logic.ResourceInformation;
import logic.Type;
import util.Tuple;
import view.TileGraphic;

public class River implements TileShape {	
	private Collection<RiverSegment> segments;
	private ResourceInformation info;
	private Lake lake;
	private Collection<Tuple<Integer, Path2D>> delta;
	
	private int rnd() { 
		return (int)((Math.random()-.5)*30);
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
				
				

				if(directions.size() == 1 && Math.random() < 1.2) {
					if(Math.random() < 0.4) {
						delta = new ArrayList<Tuple<Integer, Path2D>>();
						for(int i = 0; i<4; ++i) {
							var path = new Path2D.Float();
							
							double shiftX = (Math.random()-.5);
							double shiftY = (Math.random()-.5);
							
							path.moveTo(to.x + (int)(shiftX*RiverSegment.riverWidth), to.y + (int)(shiftY*RiverSegment.riverWidth));
							
							int dx = (int)((to.x-from.x)*1);
							int dy = (int)((to.y-from.y)*1);
							
							
							int endX = to.x + (dx + rnd())/2;
							int endY = to.y + (dy + rnd())/2;
							
							int orthogonalX = (int)((endY-dy)*0.8);
							int orthogonalY = (int)((endX-dx)*0.8);
							
							int p1x = (to.x*2 + endX)/3 + (int)((Math.random()-.5+shiftX)/3*orthogonalX);
							int p1y = (to.y*2 + endY)/3 + (int)((Math.random()-.5+shiftY)/3*orthogonalY);
							int p2x = (to.x + endX*2)/3 + (int)((Math.random()-.5)/3*orthogonalX);
							int p2y = (to.y + endY*2)/3 + (int)((Math.random()-.5)/3*orthogonalY);
	
							
							double size = 1-Math.abs(shiftX)+Math.abs(shiftY);
							
							path.curveTo(p1x,p1y,p2x,p2y,endX,endY);
							delta.add(new Tuple((int)(size*3+2),path));
							
						}
					
					}
					else
					//if(Math.random() < 0.2)
						lake = new Lake(to);
						
				}
			}
			
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
		if(delta != null) {
			g.setColor(RiverSegment.waterColor);
			for(var p : delta) {
				g.setStroke(new BasicStroke(p.getFirst()));
				g.draw(p.getSecond());
			}
		}
		for(var s : segments)
			s.bakeInto(g);
		if(lake != null)
			lake.bakeInto(g);
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
