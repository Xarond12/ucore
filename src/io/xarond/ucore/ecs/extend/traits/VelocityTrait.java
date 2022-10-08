package io.xarond.ucore.ecs.extend.traits;

import com.badlogic.gdx.math.Vector2;

import io.xarond.ucore.core.Timers;
import io.xarond.ucore.ecs.Require;
import io.xarond.ucore.ecs.Spark;
import io.xarond.ucore.ecs.Trait;

@Require(PosTrait.class)
public class VelocityTrait extends Trait{
	public Vector2 vector = new Vector2();
	public float drag;
	
	public VelocityTrait(){}
	
	public VelocityTrait(float drag){
		this.drag = drag;
	}
	
	@Override
	public void update(Spark spark){
		PosTrait pos = spark.pos();
		pos.x += vector.x*Timers.delta();
		pos.y += vector.y*Timers.delta();
		vector.scl(1f-drag*Timers.delta());
	}
	
	public float angle(){
		return vector.angle();
	}
}
