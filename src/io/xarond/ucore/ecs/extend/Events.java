package io.xarond.ucore.ecs.extend;

import io.xarond.ucore.ecs.Spark;
import io.xarond.ucore.ecs.SparkEvent;

public class Events{
	
	/**Collision event.*/
	public interface Collision extends SparkEvent{
		public void handle(Spark spark, Spark other);
	}
	
	/**Collision filter. Returns true if two sparks should collide.*/
	public interface CollisionFilter extends SparkEvent{
		public boolean handle(Spark spark, Spark other);
	}
	
	/**Tile collision event.*/
	public interface TileCollision extends SparkEvent{
		public void handle(Spark spark, int x, int y);
	}
	
	public interface Damaged extends SparkEvent{
		public void handle(Spark spark, Spark source, int damage);
	}
	
	public interface Death extends SparkEvent{
		public void handle(Spark spark);
	}
}
