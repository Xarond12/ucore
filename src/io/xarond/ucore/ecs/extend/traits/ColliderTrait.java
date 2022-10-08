package io.xarond.ucore.ecs.extend.traits;

import io.xarond.ucore.ecs.Require;
import io.xarond.ucore.ecs.Trait;

@Require(PosTrait.class)
public class ColliderTrait extends Trait{
	public float width, height;
	public float offsetx, offsety;
	
	public ColliderTrait(){
		this(10, 10);
	}
	
	public ColliderTrait(float size){
		this(size, size);
	}
	
	public ColliderTrait(float w, float h){
		width = w;
		height = h;
	}
	
	public ColliderTrait(float w, float h, float offsetx, float offsety){
		width = w;
		height = h;
		this.offsetx = offsetx;
		this.offsety = offsety;
	}
	
	public void setSize(float size){
		width = height = size;
	}
}
