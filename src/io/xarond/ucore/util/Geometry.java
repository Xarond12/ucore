package io.xarond.ucore.util;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.FloatArray;

import io.xarond.ucore.function.PositionConsumer;
import io.xarond.ucore.function.SegmentConsumer;

public class Geometry{
	private final static FloatArray floatArray = new FloatArray();
	private final static FloatArray floatArray2 = new FloatArray();
	private final static Vector2 ip = new Vector2();
	private final static Vector2 ep1 = new Vector2();
	private final static Vector2 ep2 = new Vector2();
	private final static Vector2 s = new Vector2();
	private final static Vector2 e = new Vector2();
	private final static Polygon poly = new Polygon(new float[8]);

	/**Points repesenting cardinal directions, starting at the left and going counter-clockwise.*/
	public final static GridPoint2[] d4 = {
		new GridPoint2(1, 0), 
		new GridPoint2(0, 1), 
		new GridPoint2(-1, 0), 
		new GridPoint2(0, -1)
	};

	public final static GridPoint2[] d8 = {
		new GridPoint2(1, 0), 
		new GridPoint2(0, 1), 
		new GridPoint2(-1, 0), 
		new GridPoint2(0, -1),
		
		new GridPoint2(1, 1), 
		new GridPoint2(-1, 1), 
		new GridPoint2(-1, -1),
		new GridPoint2(1, -1)
	};

	public final static GridPoint2[] d8edge = {
		new GridPoint2(1, 1), 
		new GridPoint2(-1, 1),
		new GridPoint2(-1, -1),
		new GridPoint2(1, -1)
	};
	
	
	/**returns a regular polygon with {amount} sides*/
	public static float[] regPoly(int amount, float size){
		float[] v = new float[amount*2];
		Vector2 vec = new Vector2(1,1);
		vec.setLength(size);
		for(int i = 0; i < amount; i ++){
			vec.setAngle((360f/amount) * i + 90);
			v[i*2] = vec.x;
			v[i*2+1] = vec.y;
		}
		return v;
	}

	public static boolean intersectRectSegment(Rectangle rect, Vector2 p1, Vector2 p2){
		float[] vertices = poly.getVertices();
		vertices[0] = rect.x;
		vertices[1] = rect.y;
		vertices[2] = rect.x + rect.width;
		vertices[3] = rect.y;
		vertices[4] = rect.x + rect.width;
		vertices[5] = rect.y + rect.height;
		vertices[6] = rect.x;
		vertices[7] = rect.y + rect.height;
		return Intersector.intersectSegmentPolygon(p1, p2, poly);
	}
	
	/**copied from the libGDX source*/
	public static boolean intersectPolygons (float[] p1, float[] p2) {
		// reusable points to trace edges around polygon
		floatArray2.clear();
		floatArray.clear();
		floatArray2.addAll(p1);
		if (p1.length == 0 || p2.length == 0) {
			return false;
		}
		for (int i = 0; i < p2.length; i += 2) {
			ep1.set(p2[i], p2[i + 1]);
			// wrap around to beginning of array if index points to end;
			if (i < p2.length - 2) {
				ep2.set(p2[i + 2], p2[i + 3]);
			} else {
				ep2.set(p2[0], p2[1]);
			}
			if (floatArray2.size == 0) {
				return false;
			}
			s.set(floatArray2.get(floatArray2.size - 2), floatArray2.get(floatArray2.size - 1));
			for (int j = 0; j < floatArray2.size; j += 2) {
				e.set(floatArray2.get(j), floatArray2.get(j + 1));
				// determine if point is inside clip edge
				if (Intersector.pointLineSide(ep2, ep1, e) > 0) {
					if (!(Intersector.pointLineSide(ep2, ep1, s) > 0)) {
						Intersector.intersectLines(s, e, ep1, ep2, ip);
						if (floatArray.size < 2 || floatArray.get(floatArray.size - 2) != ip.x
							|| floatArray.get(floatArray.size - 1) != ip.y) {
							floatArray.add(ip.x);
							floatArray.add(ip.y);
						}
					}
					floatArray.add(e.x);
					floatArray.add(e.y);
				} else if (Intersector.pointLineSide(ep2, ep1, s) > 0) {
					Intersector.intersectLines(s, e, ep1, ep2, ip);
					floatArray.add(ip.x);
					floatArray.add(ip.y);
				}
				s.set(e.x, e.y);
			}
			floatArray2.clear();
			floatArray2.addAll(floatArray);
			floatArray.clear();
		}
		
		if (!(floatArray2.size == 0)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static float iterateLine(float start, float x1, float y1, float x2, float y2, float segment, PositionConsumer pos){
		float len = Vector2.dst(x1, y1, x2, y2);
		int steps = (int)(len/segment);
		float step = 1f/steps;
		
		float offset = len;
		ep2.set(x2, y2);
		for(int i = 0; i < steps; i ++){
			float s = step*i;
			ep1.set(x1, y1);
			ep1.lerp(ep2, s);
			pos.accept(ep1.x, ep1.y);
			offset -= step;
		}
		
		return offset;
	}
	
	public static void iteratePolySegments(float[] vertices, SegmentConsumer it){
		for(int i = 0; i < vertices.length; i += 2){
			float x = vertices[i];
			float y = vertices[i+1];
			float x2 = 0, y2 = 0;
			if(i == vertices.length-2){
				x2 = vertices[0];
				y2 = vertices[1];
			}else{
				x2 = vertices[i+2];
				y2 = vertices[i+3];
			}
			
			it.accept(x, y, x2, y2);
		}
	}
	
	public static void iteratePolygon(PositionConsumer path, float[] vertices){
		for(int i = 0; i < vertices.length; i += 2){
			float x = vertices[i];
			float y = vertices[i+1];
			path.accept(x, y);
		}
	}
	
	public static GridPoint2[] getD4Points(){
		return d4;
	}
	
	public static GridPoint2[] getD8Points(){
		return d8;
	}
	
	public static GridPoint2[] getD8EdgePoints(){
		return d8edge;
	}
}
