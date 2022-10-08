package io.xarond.ucore.ecs.extend.traits;

import com.badlogic.gdx.math.Vector2;

import io.xarond.ucore.ecs.Spark;
import io.xarond.ucore.ecs.Trait;
import io.xarond.ucore.util.Mathf;

public class PosTrait extends Trait{
	public float x, y;
	
	public PosTrait set(float x, float y){
		this.x = x;
		this.y = y;
		return this;
	}
	
	public PosTrait set(PosTrait other){
		return set(other.x, other.y);
	}
	
	public float dst(float x, float y){
		return Vector2.dst(this.x, this.y, x, y);
	}
	
	public float dst(PosTrait pos){
		return dst(pos.x, pos.y);
	}
	
	/**Returns "rectangular" distance*/
	public float rdist(float x, float y){
		return Math.max(Math.abs(x-this.x), Math.abs(y-this.y));
	}
	
	public float angleTo(float x, float y){
		return Mathf.atan2(this.x-x, this.y-y);
	}
	
	public float angleTo(Spark other){
		return angleTo(other.pos().x, other.pos().y);
	}
	
	public float angleTo(Spark other, float offsetx, float offsety){
		return angleTo(other.pos().x + offsetx, other.pos().y + offsety);
	}
	
	public void translate(float x, float y){
		this.x += x;
		this.y += y;
	}
	
	public void translate(Vector2 v){
		translate(v.x, v.y);
	}

}
