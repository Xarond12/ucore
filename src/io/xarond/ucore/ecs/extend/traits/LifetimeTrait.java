package io.xarond.ucore.ecs.extend.traits;

import io.xarond.ucore.core.Timers;
import io.xarond.ucore.ecs.Spark;
import io.xarond.ucore.ecs.Trait;

public class LifetimeTrait extends Trait{
	public float lifetime;
	public float life;
	
	public LifetimeTrait(){
		
	}
	
	public LifetimeTrait(float lifetime){
		this.lifetime = lifetime;
	}
	
	@Override
	public void update(Spark spark){
		life += Timers.delta();
		
		if(life > lifetime){
			spark.remove();
		}
	}
	
	public float fract(){
		return 1f-life/lifetime;
	}
	
	public float ifract(){
		return life/lifetime;
	}
	
	public float sfract(){
		return (0.5f-Math.abs(life/lifetime-0.5f))*2f;
	}
}
